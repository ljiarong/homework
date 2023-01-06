package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.controller.vo.PayInfoVo;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;


    @Test
    public void findAllOrderState(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.findAllOrderState();
    }

    @Test
    public void retrieveOrderByShopId(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.retrieveOrderByShopId(1L,0,10);
    }

    @Test
    public void updateOrderMessageById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.updateOrderMessageById(1L, 1L,"商品待收货",user);
    }

    @Test
    public void updateOrderStatusById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.updateOrderStatusById(1L, 1L,user);
    }


    @Test
    public void deleteOrderById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
       orderService.deleteOrderById(0L, 1L,user);
    }

    @Test
    public void createOrder(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        OrderItemDto orderItemDto1= OrderItemDto.builder().quantity(4).actId(null).onsaleId(33L).couponId(null).build(),
                     orderItemDto2= OrderItemDto.builder().quantity(5).actId(null).onsaleId(23L).couponId(null).build();
        List<OrderItemDto> orderItemDtos=new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        ConsigneeDto consigneeDto= ConsigneeDto.builder().consignee("赵永波").address("人民北路").regionId(2417L).mobile("13959235540").build();
        orderService.createOrder(orderItemDtos,consigneeDto,
                "等待所有商品备齐后再发", user);
    }

    @Test
    public void packOrder(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        OrderItemDto orderItemDto1= OrderItemDto.builder().quantity(4).actId(null).onsaleId(33L).couponId(null).build(),
                orderItemDto2= OrderItemDto.builder().quantity(5).actId(null).onsaleId(23L).couponId(null).build();
        List<OrderItemDto> orderItemDtos=new ArrayList<>();
        orderItemDtos.add(orderItemDto1);
        orderItemDtos.add(orderItemDto2);
        orderService.packOrder(orderItemDtos,user);
    }

    @Test
    public void getCustomerOrders(){
        orderService.getCustomerOrders(1L,0,10);
    }

    @Test
    public void getCustomerOrderById(){
        orderService.getCustomerOrderById(1L);
    }

    @Test
    public void updateCustomerOrderById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.updateCustomerOrderById(1L,"赵永波",2418L,"人民南路","13959235540",user);
    }

    @Test
    public void deleteCustomerOrderById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        orderService.deleteCustomerOrderById(129L,user);
    }

    @Test
    public void payOrderById(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        PayInfoVo payInfoVo=new PayInfoVo();
        payInfoVo.setCoupons(null);
        payInfoVo.setPoint(10L);
        payInfoVo.setShopChannel(20L);
        orderService.payOrderById(128L,payInfoVo,user);
    }


}
