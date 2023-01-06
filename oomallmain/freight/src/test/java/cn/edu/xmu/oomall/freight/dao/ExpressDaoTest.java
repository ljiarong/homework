package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.service.FreightService;
import cn.edu.xmu.oomall.freight.service.dto.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.Express;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FreightApplication.class)
public class ExpressDaoTest {
    @Autowired
    private   ExpressDao expressDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void saveExpress(){
      ExpressVo expressVo=new ExpressVo();
      expressVo.setShopLogisticId(0L);
      ExpressVo.Sender sender=new ExpressVo.Sender();
      sender.setName("小红");
      sender.setMobile("0898-11111");
      sender.setRegionId(0L);
      sender.setAddress("东城区");
      expressVo.setSender(sender);
      ExpressVo.Delivery delivery=new ExpressVo.Delivery();
      delivery.setName("小明");
      delivery.setMobile("0898-22222");
      delivery.setRegionId(0L);
      delivery.setAddress("西城区");
      expressVo.setDelivery(delivery);

        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(1));
        userDto.setName("test1");
        userDto.setUserLevel(1);
       expressDao.saveExpress(1L,"^SF5-kWT-l$",expressVo,userDto);
    }

    @Test
    public void getExpressById(){
        Express express=new Express();
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test2");
        expressDao.getExpressById(0L);
    }
    @Test
    public void updateExpress(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(3));
        userDto.setName("test3");
        userDto.setUserLevel(1);
        expressDao.updateExpress(2L, 3L, (byte) 1,userDto);
    }
    @Test
    public void cancleExpress(){
        UserDto userDto=new UserDto();
        userDto.setId(Long.valueOf(2));
        userDto.setName("test4");
        userDto.setUserLevel(1);
        expressDao.cancelExpress(3L, 3L,userDto);

    }
}
