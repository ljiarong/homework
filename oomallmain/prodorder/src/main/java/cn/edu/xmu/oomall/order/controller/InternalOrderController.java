package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.OrderStateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class InternalOrderController {
    private OrderService orderService;

    @Autowired
    public InternalOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/states")
    public ReturnObject getOrderState() {
        List<OrderStateDto> ret=orderService.findAllOrderState();
        return  new ReturnObject(ret);
    }

}
