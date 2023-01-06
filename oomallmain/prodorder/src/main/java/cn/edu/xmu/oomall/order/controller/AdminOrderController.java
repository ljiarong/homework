package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.service.dto.SimpleOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController /*Restful的Controller对象*/
@ResponseBody
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminOrderController {
    private OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * 店家查询商户所有订单（概要）
     */

    @GetMapping("/orders")
    public ReturnObject getSimpleOrder(
            @PathVariable(value = "shopId",required = true) Long shopId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        PageDto<SimpleOrderDto> pageDto = orderService.retrieveOrderByShopId(shopId, page, pageSize);
        return new ReturnObject(pageDto);
    }


    /**
     * 店家修改订单（留言）
     */

    @PutMapping("/orders/{id}")
    public ReturnObject updateOrderMessageById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                  @PathVariable(value = "id", required = true) Long id,
                                               @Validated @RequestBody String message,
                                                  @LoginUser UserDto user) {
        return orderService.updateOrderMessageById(shopId, id, message,user);
    }

    /**
     * 店家确认订单
     */

    @DeleteMapping("/orders/{id}/confirm")
    public ReturnObject updateOrderStatusById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                  @PathVariable(value = "id", required = true) Long id,
                                                  @LoginUser UserDto user) {
        return orderService.updateOrderStatusById(shopId, id, user);
    }

    /**
     * 商户或管理员取消订单
     */

    @DeleteMapping("/orders/{id}")
    public ReturnObject deleteWarehouseRegionById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                  @PathVariable(value = "id", required = true) Long id,
                                                  @LoginUser UserDto user) {
        return orderService.deleteOrderById(shopId, id, user);
    }

}
