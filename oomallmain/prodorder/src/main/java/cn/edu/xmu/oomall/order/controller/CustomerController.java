//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.controller.vo.PayInfoVo;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.service.dto.SimpleOrderDto;
import cn.edu.xmu.oomall.order.service.dto.SimpleOrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "orders",produces = "application/json;charset=UTF-8")
public class CustomerController {

    private OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ReturnObject createOrder(@RequestBody @Validated OrderVo orderVo, @LoginUser UserDto user) {
        orderService.createOrder(orderVo.getItems().stream().map(item -> OrderItemDto.builder().onsaleId(item.getOnsaleId()).quantity(item.getQuantity()).actId(item.getActId()).couponId(item.getCouponId()).build()).collect(Collectors.toList()),
                ConsigneeDto.builder().consignee(orderVo.getConsignee()).address(orderVo.getAddress()).regionId(orderVo.getRegionId()).mobile(orderVo.getMobile()).build(),
                orderVo.getMessage(), user);
        return new ReturnObject(ReturnNo.CREATED);
    }


    @GetMapping
    public ReturnObject getOrders(@RequestParam(required = false,defaultValue = "1") Integer page,
                                  @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                  @LoginUser UserDto user){
        PageDto<SimpleOrderDto> res=orderService.getCustomerOrders(user.getDepartId(),page,pageSize);
        return new ReturnObject(res);
    }

    @GetMapping(value = "/{id}")
    public ReturnObject getOrderById(@PathVariable(value = "id",required = true) Long id){
        return new ReturnObject(orderService.getCustomerOrderById(id));
    }

    @PutMapping(value = "/{id}")
    public ReturnObject updateCustomerOrderMessageById(@PathVariable(value = "id", required = true) Long id,
                                                       @Validated @RequestBody OrderVo vo,
                                                       @LoginUser UserDto user){
        ReturnObject ret =orderService.updateCustomerOrderById(id,vo.getConsignee(),vo.getRegionId(),vo.getAddress(),vo.getMobile(),user);
        return new ReturnObject(ret);
    }

    @DeleteMapping(value = "/{id}")
    public ReturnObject deleteCustomerOrderById(@PathVariable("id") Long id,
                                                @LoginUser UserDto userDto){
        ReturnObject ret = orderService.deleteCustomerOrderById(id, userDto);
        return new ReturnObject(ret);
    }


    /**
     * 顾客支付订单
     */

    @PostMapping("/{id}/pay")
    public ReturnObject payOrderById(@PathVariable(value = "id", required = true) Long id,
                                                   @Validated @RequestBody PayInfoVo payInfoVo,
                                                   @LoginUser UserDto customer) {
        return orderService.payOrderById(id,payInfoVo,customer);
    }
}
