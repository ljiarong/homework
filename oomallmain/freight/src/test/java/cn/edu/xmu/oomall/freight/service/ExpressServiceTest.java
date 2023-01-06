package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.FreightApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = FreightApplication.class)
public class ExpressServiceTest {
    @Autowired
    public ExpressService expressService;
    @Test
    public void getExpressById(){
        expressService.getExpressById(1L);
    }
    @Test
    public void updateExpress(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
     expressService.updateExpress(0L, 0L, (byte) 1,user);
    }
    @Test
    public void cancleExpress(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        expressService.cancelExpress(0L, 1L,user);
    }
}
