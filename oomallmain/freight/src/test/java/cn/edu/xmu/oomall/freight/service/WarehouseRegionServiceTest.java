package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.dao.WarehouseRegionDao;
import cn.edu.xmu.oomall.freight.service.dto.FullWarehouseDto;
import cn.edu.xmu.oomall.freight.service.dto.WarehouseRegionDto;
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
public class WarehouseRegionServiceTest {
    @Autowired
    private WarehouseRegionDao warehouseRegionDao;
    @Autowired
    private FreightService freightService;

    @Test
    public void retrieveWarehouseByRegionId() {
        FullWarehouseDto ret = freightService.retrieveWarehouseByRegionId(1l, 1l, 1, 10).getList().get(0);

        assertThat(ret.getWarehouse().getId()).isEqualTo(1l);
        assertThat(ret.getWarehouse().getName()).isEqualTo("朝阳新城第二仓库");
        assertThat(ret.getWarehouse().getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getWarehouse().getPriority()).isEqualTo(1000);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


    }

    @Test
    public void addWarehouseRegionById1() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
//        测试仓库不存在
        assertThrows(BusinessException.class, () -> freightService.addWarehouseRegionById(1l, 30l, 1l, beginAndEndTimeVo, user));

    }

    @Test
    public void addWarehouseRegionById2() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
//        测试地区不存在
        assertThrows(BusinessException.class, () -> freightService.addWarehouseRegionById(1l, 1l, 100132131232131232l, beginAndEndTimeVo, user));

    }

    @Test
    public void addWarehouseRegionById3() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
//        测试已存在该配送地区
        assertThrows(BusinessException.class, () -> freightService.addWarehouseRegionById(1l, 1l, 1l, beginAndEndTimeVo, user));

    }

    @Test
    public void addWarehouseRegionById4() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);

        ReturnObject ret = freightService.addWarehouseRegionById(1l, 1l, 2l, beginAndEndTimeVo, user);

        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);

    }

    //正常情况下
    @Test
    public void updateWarehouseRegionById1() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);

        ReturnObject ret = freightService.updateWarehouseRegionById(1l, 1l, 1l, beginAndEndTimeVo, user);

        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    //如果仓库没有此地区设置
    @Test
    public void updateWarehouseRegionById2() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);

        ReturnObject ret = freightService.updateWarehouseRegionById(1l, 1l, 2l, beginAndEndTimeVo, user);

        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void deleteWarehouseRegionById() {
        ReturnObject ret = freightService.deleteWarehouseRegionById(1l, 1l, 1l);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void retrieveRegionByWarehouseId() {
        WarehouseRegionDto ret = freightService.retrieveRegionByWarehouseId(1l, 1l, 1, 10).getList().get(0);
        assertThat(ret.getRegion().getId()).isEqualTo(1l);
        assertThat(ret.getRegion().getName()).isEqualTo("北京市");
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
