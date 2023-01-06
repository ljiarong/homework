package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.controller.vo.PayInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void updateOrderMessageById(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        orderDao.updateOrderMessageById(1L, 1L,"商品待发货",userDto);

    }

    @Test
    public void findAllOrderState(){

        orderDao.findAllOrderState();

    }

    @Test
    public void retrieveOrderByShopId(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        orderDao.retrieveOrderByShopId(1L);

    }
    @Test
    public void updateOrderStatusById(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        orderDao.updateOrderStatusById(1L, 1L,userDto);

    }

    @Test
    public void deleteOrderById(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        orderDao.deleteOrderById(3L, 3L,userDto);

    }
    @Test
    public void  payOrderById() {
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        PayInfoVo payInfoVo=new PayInfoVo();
        payInfoVo.setCoupons(1L);
        payInfoVo.setPoint(1L);
        payInfoVo.setShopChannel(1L);
       orderDao.payOrderById(1L,payInfoVo,userDto);
    }

    @Test
    public void retrieveOrderByCustomerId(){
        orderDao.retrieveOrderByCustomerId(1L);
    }

    @Test
    public void retrieveOrderByCustomerId2(){
        orderDao.retrieveOrderByCustomerId(2L);
    }

    @Test
    public void retrieveOrderByOrderId(){
        orderDao.retrieveOrderByOrderId(1L);
    }

    @Test
    public void retrieveOrderByOrderId2(){
        orderDao.retrieveOrderByOrderId(2L);
    }

    @Test
    public void retrieveOrderItemByOrderId(){
        orderDao.retrieveOrderItemByOrderId(1L);
    }

    @Test
    public void updateCustomerOrderById(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        orderDao.updateCustomerOrderById(15L,"赵俊",67922L,"大同街","13959298011",userDto);
    }
}
