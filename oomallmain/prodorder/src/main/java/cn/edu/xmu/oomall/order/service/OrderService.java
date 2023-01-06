//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.controller.vo.OrderModifyVo;
import cn.edu.xmu.oomall.order.controller.vo.PayInfoVo;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.ProductDto;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPo;
import cn.edu.xmu.oomall.order.service.dto.*;
import cn.edu.xmu.oomall.shop.controller.vo.FreightPriceVo;
import cn.edu.xmu.oomall.shop.controller.vo.TemplateVo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
public class OrderService {

    @Value("${oomall.order.server-num}")
    private int serverNum;

    private GoodsDao goodsDao;

    private ShopDao shopDao;

    private OrderDao orderDao;

    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    public OrderService(GoodsDao goodsDao, OrderDao orderDao, RocketMQTemplate rocketMQTemplate) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Transactional
    public Map<Long, List<OrderItem>> packOrder(List<OrderItemDto> items, UserDto customer){
        Map<Long, List<OrderItem>> packs = new HashMap<>();
        items.stream().forEach(item -> {
            OnsaleDto onsaleDto = this.goodsDao.getOnsaleById(PLATFORM, item.getOnsaleId()).getData();
            OrderItem orderItem = OrderItem.builder().onsaleId(onsaleDto.getId()).price(onsaleDto.getPrice()).name(onsaleDto.getProduct().getName()).creatorId(customer.getId()).creatorName(customer.getName()).build();
            if (null != onsaleDto.getActList() && null != item.getActId()){
                if (onsaleDto.getActList().stream().filter(activity -> activity.getId() == item.getActId()).count()  > 0){
                    orderItem.setActId(item.getActId());
                    //TODO: 需要查看优惠卷id所属的活动是否在onsale的活动列表中，并且优惠卷是有效的，才能设置到orderItem中
                }
            }
            if (item.getQuantity() <= onsaleDto.getMaxQuantity()){
                //不能超过最大可购买数量
                orderItem.setQuantity(item.getQuantity());
            }else{
                throw new BusinessException(ReturnNo.ITEM_OVERMAXQUANTITY, String.format(ReturnNo.ITEM_OVERMAXQUANTITY.getMessage(), onsaleDto.getId(), item.getQuantity(), onsaleDto.getMaxQuantity()));
            }
            Long shopId = onsaleDto.getShop().getId();
            List<OrderItem> pack = packs.get(shopId);
            if (null == pack){
                packs.put(shopId, new ArrayList<>(){
                    {
                        add(orderItem);
                    }
                });
            }else{
                pack.add(orderItem);
            }
        });
        return packs;
    }

    @Transactional
    public void saveOrder(Map<Long, List<OrderItem>> packs, ConsigneeDto consignee, String message, UserDto customer){
        packs.keySet().stream().forEach(shopId -> {
           // ProductDto productDto=this.goodsDao.findProductById(packs.get(productId));
            //TemplateVo templateVo = this.shopDao.retrieveTemplateByName(shopId).getData();
           // FreightPriceVo freightPriceVo=this.shopDao.getFreight(templateVo.getId(), consignee.getRegionId());
            Order order = Order.builder().creatorId(customer.getId()).customerId(customer.getId()).creatorName(customer.getName()).gmtCreate(LocalDateTime.now()).shopId(shopId).
                    consignee(consignee.getConsignee()).address(consignee.getAddress()).mobile(consignee.getMobile()).regionId(consignee.getRegionId()).
                    orderSn(Common.genSeqNum(serverNum)).message(message).orderItems(packs.get(shopId)).build();
                    this.orderDao.createOrder(order);
                }
        );

    }

    @Transactional
    public void findOrderByOrderSn(String orderSn){
        this.orderDao.findOrderByOrderSn(orderSn);

    }

    public void createOrder(List<OrderItemDto> items, ConsigneeDto consignee, String message, UserDto customer) {
        Map<Long, List<OrderItem>> packs = this.packOrder(items, customer);

        String packStr = JacksonUtil.toJson(packs);
        Message msg = MessageBuilder.withPayload(packStr).setHeader("consignee", consignee).setHeader("message",message).setHeader("user", customer).build();
        rocketMQTemplate.sendMessageInTransaction("order-topic:1", msg, null);
    }

    public PageDto<SimpleOrderDto> retrieveOrderByShopId(Long shopId, Integer page, Integer pageSize) {
        List<SimpleOrderDto> ret=null;
        List<OrderPo> list = orderDao.retrieveOrderByShopId(shopId);
        if (null != list && list.size() > 0) {
            ret = list.stream().map(orderPo -> {
                SimpleOrderDto simpleOrderDto = new SimpleOrderDto();
                simpleOrderDto.setId(orderPo.getId());
                simpleOrderDto.setStatus(orderPo.getStatus());
                simpleOrderDto.setDiscountPrice(orderPo.getDiscountPrice());
                simpleOrderDto.setExpressFee(orderPo.getExpressFee());
                simpleOrderDto.setOriginPrice(orderPo.getOriginPrice());
                simpleOrderDto.setGmtCreat(orderPo.getGmtCreate());
                return simpleOrderDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return new PageDto<>(ret,page,pageSize);
    }

    public List<OrderStateDto> findAllOrderState() {
        List<OrderStateDto> ret=null;
        List<OrderPo> list = orderDao.findAllOrderState();
        if (null != list && list.size() > 0) {
            ret = list.stream().map(orderPo -> {
                OrderStateDto orderStateDto = new OrderStateDto();
                orderStateDto.setCode(orderPo.getStatus());
                orderStateDto.setName("状态");
                return orderStateDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return ret;
    }

    public ReturnObject updateOrderMessageById(Long shopId, Long id,String  message,UserDto user) {
        if (null != shopId&null != id) {
            orderDao.updateOrderMessageById(shopId,id,message,user);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    public PageDto<SimpleOrderDto> getCustomerOrders(Long customerId,Integer page,Integer pageSize){
        List<OrderPo> orders=this.orderDao.retrieveOrderByCustomerId(customerId);
        //查找订单
        List<SimpleOrderDto> ret = new ArrayList<>();
        if (orders != null && orders.size()>0) {
            orders.forEach(o -> {
                SimpleOrderDto simpleOrderDto=SimpleOrderDto.builder().id(o.getId()).status(o.getStatus()).gmtCreat(o.getGmtCreate()).originPrice(o.getOriginPrice()).discountPrice(o.getDiscountPrice()).expressFee(o.getExpressFee()).build();
                ret.add(simpleOrderDto);
            });
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public OrderInfoDto getCustomerOrderById(Long id){
        Order order=this.orderDao.retrieveOrderByOrderId(id);
        ConsigneeDto exm= ConsigneeDto.builder().consignee(order.getConsignee()).regionId(order.getRegionId()).address(order.getAddress()).mobile(order.getMobile()).build();
        List<OrderItemDto> orderItemDtos=new ArrayList<>();
        order.getOrderItems().forEach(o->{
            OrderItemDto orderItemDto=OrderItemDto.builder().onsaleId(o.getOnsaleId()).quantity(o.getQuantity()).actId(o.getActId()).couponId(o.getCouponId()).build();
            orderItemDtos.add(orderItemDto);
        });
        SimpleCustomerDto customerDto= SimpleCustomerDto.builder().id(order.getCustomerId()).build();
        SimpleShopDto shopDto=new SimpleShopDto();
        shopDto.setId(order.getShopId());
        SimplePackageDto packageDto=SimplePackageDto.builder().id(order.getPackageId()).build();
        OrderInfoDto ret= OrderInfoDto.builder().id(order.getId()).orderSn(order.getOrderSn()).customer(customerDto).shop(shopDto).pid(order.getPid()).status(order.getStatus()).gmtCreate(order.getGmtCreate()).gmtModified(order.getGmtModified())
                .originPrice(order.getOriginPrice()).discountPrice(order.getDiscountPrice()).expressFee(order.getExpressFee()).message(order.getMessage()).consignee(exm).pack(packageDto).orderItems(orderItemDtos).build();
        return ret;
    }


    public ReturnObject updateOrderStatusById(Long shopId, Long id,UserDto user) {
        if (null != shopId&null != id) {
            orderDao.updateOrderStatusById(shopId,id,user);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    public ReturnObject updateCustomerOrderById(Long id,String consignee,Long regionId,String address,String mobile,UserDto user){
        if (null != id && null != user.getDepartId()){
            Order bo=Order.builder().id(id).consignee(consignee).regionId(regionId).address(address).mobile(mobile).build();
            orderDao.updateCustomerOrderById(id,consignee,regionId,address,mobile,user);
        }
        else {
            // throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "订单", id, order.getStatusName()));
            //订单bo中写的状态
        }
        return new ReturnObject(ReturnNo.OK);
    }

    public ReturnObject deleteOrderById(Long shopId, Long id,UserDto user) {
        if (null != shopId&null != id) {
           orderDao.deleteOrderById(shopId,id,user);
        }
        return new ReturnObject(ReturnNo.OK);
    }
    public ReturnObject deleteCustomerOrderById(Long id,UserDto user){
        if (null != id){
            // this.updateCustomerOrderStatusById();    删除订单需要修改订单状态
            orderDao.deleteOrderById(0,id,user);
        }
        return new ReturnObject(ReturnNo.OK);
    }



    public ReturnObject payOrderById(Long id, PayInfoVo payInfoVo,UserDto costomer) {
        if (null != id) {
            orderDao.payOrderById(id,payInfoVo,costomer);
        }
        return new ReturnObject(ReturnNo.OK);
    }

}
