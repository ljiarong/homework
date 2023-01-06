package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class WarehouseRegionDaoTest {
    @Autowired
    private WarehouseRegionDao warehouseRegionDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void retrieveByRegionId() {
        WarehouseRegion ret = warehouseRegionDao.retrieveByRegionId(1L, 1, 10).get(0);
        assertThat(ret.getId()).isEqualTo(1l);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getWarehouseId()).isEqualTo(1l);

    }

    @Test
    public void findByIdAndRegionId() {
        WarehouseRegion ret = warehouseRegionDao.findByIdAndRegionId(1l, 1l);
        assertThat(ret.getId()).isEqualTo(1l);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void save() {
        BeginAndEndTimeVo beginAndEndTimeVo = new BeginAndEndTimeVo();
        beginAndEndTimeVo.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        beginAndEndTimeVo.setEndTime(LocalDateTime.parse("2024-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        warehouseRegionDao.save(1l, 25l, 1l, beginAndEndTimeVo, user);
    }

    @Test
    public void update() {

        WarehouseRegion ret = warehouseRegionDao.findByIdAndRegionId(1l, 1l);
        ret.setBeginTime(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ret.setEndTime(LocalDateTime.parse("2027-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        String key = warehouseRegionDao.update(ret, user);
        assertThat(key).isEqualTo(1l);
    }

    @Test
    public void deleByWarehouseIdAndRegionId() {

        warehouseRegionDao.deleByWarehouseIdAndRegionId(1l, 1l);

    }

    @Test
    public void retrieveByWarehouseId() {
        WarehouseRegion ret = warehouseRegionDao.retrieveByWarehouseId(1l, 1, 10).get(0);
        assertThat(ret.getId()).isEqualTo(1l);
        assertThat(ret.getRegionId()).isEqualTo(1l);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02 14:20:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void deleByWarehouseId() {
        warehouseRegionDao.deleByWarehouseId(1l);
    }
}
