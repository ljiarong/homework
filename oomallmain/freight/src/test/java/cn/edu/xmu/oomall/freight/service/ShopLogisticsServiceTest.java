package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.LogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.service.dto.SimpleShopLogisticsDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ShopLogisticsServiceTest {
    @Autowired
    private FreightService freightService;

    @Test
    public void addShoplogistics1() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ShopLogisticsVo shopLogisticsVo = new ShopLogisticsVo();
        shopLogisticsVo.setLogisticsId(2l);
        shopLogisticsVo.setSecret("secret2");
        shopLogisticsVo.setPriority(5);

        SimpleShopLogisticsDto ret = freightService.addShoplogistics(11l, shopLogisticsVo, user);


        assertThat(ret.getLogistics().getId()).isEqualTo(2l);
        assertThat(ret.getLogistics().getName()).isEqualTo("中通快递");
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getPriority()).isEqualTo(5);

    }

    @Test
    public void addShoplogistics2() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ShopLogisticsVo shopLogisticsVo = new ShopLogisticsVo();
        shopLogisticsVo.setLogisticsId(2l);
        shopLogisticsVo.setSecret("secret2");
        shopLogisticsVo.setPriority(5);


        assertThrows(BusinessException.class, () -> freightService.addShoplogistics(1l, shopLogisticsVo, user));

    }


    @Test
    public void getShopLoistics() {
        SimpleShopLogisticsDto ret = freightService.getShopLoistics(1l, 1, 10).getList().get(0);

        assertThat(ret.getLogistics().getId()).isEqualTo(3l);
        assertThat(ret.getLogistics().getName()).isEqualTo("极兔速递");
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("1"));
        assertThat(ret.getSecret()).isEqualTo("secret3");
        assertThat(ret.getPriority()).isEqualTo(2);

    }

    @Test
    public void updateShopLogistics() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        LogisticsVo logisticsVo = new LogisticsVo();
        logisticsVo.setPriority(5);
        logisticsVo.setSecret("secret3");
        ReturnObject ret = freightService.updateShopLogistics(1l, 1l, logisticsVo, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void suspendShopLogistics() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.suspendShopLogistics(1l, 1l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void resumeShopLogistics() {

        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.resumeShopLogistics(1l, 1l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
}
