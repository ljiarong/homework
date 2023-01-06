package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.service.dto.UndeliverableDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class UndeliverableServiceTest {
    @Autowired
    private FreightService freightService;
    @Test
    public void addUndeliverable(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ReturnObject ret = freightService.addUndeliverable(1l, 1l, 1l, beginAndEndTimeVo, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void updateUndeliverable(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ReturnObject ret = freightService.addUndeliverable(1l, 1l, 483250l, beginAndEndTimeVo, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void delUndeliverable(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.delUndeliverable(1l, 1l, 483250l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void getUndeliverable(){
        UndeliverableDto ret = freightService.getUndeliverable(1l, 1l, 1, 10).getList().get(0);
        assertThat(ret.getId()).isEqualTo(1);
        assertThat(ret.getRegion().getId()).isEqualTo(483250);
        assertThat(ret.getRegion().getName()).isEqualTo("广东省");
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 22:28:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-12-02 22:28:49", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
