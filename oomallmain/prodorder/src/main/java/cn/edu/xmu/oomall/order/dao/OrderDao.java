//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.order.controller.vo.PayInfoVo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.mapper.generator.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPaymentPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;


@Repository
public class OrderDao {

    private OrderPoMapper orderPoMapper;

    private OrderItemPoMapper orderItemPoMapper;

    private OrderPaymentPoMapper orderPaymentPoMapper;

    private RedisUtil redisUtil;
    @Autowired
    public OrderDao(OrderPoMapper orderPoMapper, OrderItemPoMapper orderItemPoMapper,OrderPaymentPoMapper orderPaymentPoMapper) {
        this.orderPoMapper = orderPoMapper;
        this.orderItemPoMapper = orderItemPoMapper;
        this.orderPaymentPoMapper=orderPaymentPoMapper;
    }



    public void createOrder(Order order){
      //  OrderPo orderPo = OrderPo.builder().creatorId(order.getCreatorId()).creatorName(order.getCreatorName()).orderSn(order.getOrderSn()).build();
       OrderPo orderPo=new OrderPo();
       orderPo.setCreatorId(order.getCreatorId());
       orderPo.setCreatorName(order.getCreatorName());
       orderPo.setOrderSn(order.getOrderSn());
       orderPo.setExpressFee(order.getExpressFee());//运费
        orderPoMapper.save(orderPo);

        order.getOrderItems().stream().forEach(orderItem -> {


            OrderItemPo orderItemPo = OrderItemPo.builder().creatorId(orderItem.getCreatorId()).onsaleId(orderItem.getOnsaleId()).quantity(orderItem.getQuantity()).build();
            orderItemPoMapper.save(orderItemPo);

        });


    }

    public List<OrderPo> findAllOrderState() {
        OrderPoExample orderPoExample = new OrderPoExample();
        orderPoExample.createCriteria().andConsigneeIsNotNull();
        List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);

        return orderPos ;

    }

    public Order getOrderBo(OrderPo po){
        Order ret=Order.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .customerId(po.getCustomerId()).shopId(po.getShopId()).orderSn(po.getOrderSn()).pid(po.getPid()).consignee(po.getConsignee()).regionId(po.getRegionId()).address(po.getAddress()).mobile(po.getMobile())
                .message(po.getMessage()).packageId(po.getPackageId()).expressFee(po.getExpressFee()).status(po.getStatus()).originPrice(po.getOriginPrice()).discountPrice(po.getDiscountPrice()).build();
        //还有orderItem和activityId

        return ret;
    }

    public OrderItem getOrderItemBo(OrderItemPo po){
        OrderItem ret=OrderItem.builder().orderId(po.getOrderId()).onsaleId(po.getOnsaleId()).quantity(po.getQuantity()).price(po.getPrice()).discountPrice(po.getDiscountPrice())
                .point(po.getPoint()).name(po.getName()).actId(po.getActivityId()).couponId(po.getCouponId()).commented(po.getCommented()).build();
        return ret;
    }

    public List<OrderItem> retrieveOrderItemByOrderId(Long orderId){
        List<OrderItem> ret = new ArrayList<>();
        if (null != orderId) {
            OrderItemPoExample orderItemPoExample=new OrderItemPoExample();
            orderItemPoExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OrderItemPo> pos = this.orderItemPoMapper.selectByExample(orderItemPoExample);
            ret = pos.stream().map(po -> this.getOrderItemBo(po))
                    .collect(Collectors.toList());
        }
        return ret;
    }

    public void findOrderByOrderSn(String orderSn){
        this.orderPoMapper.selectByOrderSn(orderSn);
    }

    public List<OrderPo> retrieveOrderByShopId(Long shopId) {
        OrderPoExample orderPoExample = new OrderPoExample();
       orderPoExample.createCriteria().andShopIdEqualTo(shopId);
        List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);

            return orderPos ;
    }
    public void updateOrderMessageById(long shopId, Long id,String message, UserDto user) throws RuntimeException {

        OrderPo orderPo = this.orderPoMapper.selectByPrimaryKey(id);
        orderPo.setMessage(message);
        this.orderPoMapper.updateByPrimaryKey(orderPo);


    }
    public Order retrieveOrderByOrderId(Long id){
        Optional<OrderPo> ret=this.orderPoMapper.findById(id);
        if (ret.isEmpty()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "订单", id));
        }
        else {
            OrderPo po=ret.get();
            Order bo=this.getOrderBo(po);
            bo.setOrderItems(this.retrieveOrderItemByOrderId(bo.getId()));
            return bo;
        }
    }
    public List<OrderPo> retrieveOrderByCustomerId(Long customerId){
        OrderPoExample orderPoExample=new OrderPoExample();
        orderPoExample.createCriteria().andCustomerIdEqualTo(customerId);
        List<OrderPo> orderPos=orderPoMapper.selectByExample(orderPoExample);
        return orderPos;
    }
    public void updateCustomerOrderById(Long id,String consignee,Long regionId,String address,String mobile,UserDto user){
        OrderPo orderPo=this.orderPoMapper.selectByPrimaryKey(id);
        orderPo.setConsignee(consignee);
        orderPo.setRegionId(regionId);
        orderPo.setAddress(address);
        orderPo.setMobile(mobile);
        this.orderPoMapper.updateByPrimaryKey(orderPo);
    }

    public void updateOrderStatusById(long shopId, Long id, UserDto user) throws RuntimeException {
        if (null == id)
            return;
        OrderPo orderPo= this.orderPoMapper.selectByPrimaryKey(id);
        orderPo.setStatus(1);
        this.orderPoMapper.updateByPrimaryKey(orderPo);
    }




    public void deleteOrderById(long shopId, Long id, UserDto user) throws RuntimeException {
        if (null == id)
            return;
        this.orderPoMapper.deleteByPrimaryKey(id);
       // redisUtil.del(String.format(KEY, id));
    }

    public void deleteCustOrderById( Long id, UserDto customer) throws RuntimeException {
        if (null == id)
            return;
        this.orderPoMapper.deleteByPrimaryKey(id);
    }

    public void payOrderById(Long id, PayInfoVo payInfoVo, UserDto customer) throws RuntimeException {
        OrderPaymentPo orderPaymentPo=new OrderPaymentPo();
        orderPaymentPo.setOrderId(id);
        putUserFields(orderPaymentPo, "creator", customer);
        putGmtFields(orderPaymentPo, "create");
        this.orderPaymentPoMapper.insert(orderPaymentPo);
    }
}
