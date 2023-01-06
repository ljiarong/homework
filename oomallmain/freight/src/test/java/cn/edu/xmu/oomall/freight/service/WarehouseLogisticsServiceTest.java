package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.service.dto.ShopLogisticsDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class WarehouseLogisticsServiceTest {
    @Autowired
    private FreightService freightService;

    @Test
    public void addWarehousepLogisticsById1() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);

        assertThrows(BusinessException.class, () -> freightService.addWarehousepLogisticsById(1l, 25l, 1l, beginAndEndTimeVo, user));
    }

    @Test
    public void addWarehousepLogisticsById2() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.addWarehousepLogisticsById(1l, 25l, 4l, beginAndEndTimeVo, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void updateWarehousepLogisticsById() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.updateWarehousepLogisticsById(1l, 25l, 1l, beginAndEndTimeVo, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void delWarehousepLogisticsById() {
        ReturnObject ret = freightService.delWarehousepLogisticsById(1l, 25l, 1l);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void retrieveShoplogistics() {
        ShopLogisticsDto ret = freightService.retrieveShoplogistics(1l, 25l, 1, 10).getList().get(0);
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getShopLogistics().getId()).isEqualTo(3);
        assertThat(ret.getShopLogistics().getLogistics().getId()).isEqualTo(3);
        assertThat(ret.getShopLogistics().getLogistics().getName()).isEqualTo("极兔速递");
        assertThat(ret.getShopLogistics().getInvalid()).isEqualTo(Byte.valueOf("1"));
        assertThat(ret.getShopLogistics().getSecret()).isEqualTo("secret3");
        assertThat(ret.getShopLogistics().getPriority()).isEqualTo(2);
    }
}
