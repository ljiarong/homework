package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseLogisticsPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class WarehouseLogisticsDaoTest {
    @Autowired
    private WarehouseLogisticsDao warehouseLogisticsDao;
    @MockBean
    private RedisUtil redisUtil;
    @Test
    public void deleByWarehouseId(){
        warehouseLogisticsDao.deleByWarehouseId(1l);
    }
    @Test
    public void findByIdAndLogisticsId(){
        WarehouseLogisticsPo ret = warehouseLogisticsDao.findByIdAndLogisticsId(25l, 1l).get(0);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    @Test
    public void save(){
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        warehouseLogisticsDao.save(12l,23l,beginAndEndTimeVo,user);
    }
    @Test
    public void update(){
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        warehouseLogisticsDao.update(25l,1l,beginAndEndTimeVo,user);
    }
    @Test
    public void deleByWarehouseIdAndLogisticsId(){
        List<WarehouseLogisticsPo> byIdAndLogisticsId = warehouseLogisticsDao.findByIdAndLogisticsId(25l, 1l);
        warehouseLogisticsDao.deleByWarehouseIdAndLogisticsId(25l,1l,byIdAndLogisticsId);

    }
    @Test
    public void retrieveByWarehouseId(){
        WarehouseLogistics ret = warehouseLogisticsDao.retrieveByWarehouseId(25l,1,10).get(0);
        assertThat(ret.getShopLogisticsId()).isEqualTo(1l);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 13:57:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
