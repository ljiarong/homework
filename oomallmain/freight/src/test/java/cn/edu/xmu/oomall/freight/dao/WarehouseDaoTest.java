package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class WarehouseDaoTest {
    @Autowired
    private WarehouseDao warehouseDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void findById1() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1l);
        warehouse.setAddress("北京,朝阳,东坝,朝阳新城第二曙光路14号");
        warehouse.setName("朝阳新城第二仓库");
        warehouse.setSenderMobile("139542562579");
        warehouse.setSenderName("阮杰");
        warehouse.setInvalid(Byte.valueOf("0"));
        warehouse.setPriority(10001);

        Mockito.when(redisUtil.hasKey(String.format(WarehouseDao.KEY, 1))).thenReturn(true);
        Mockito.when(redisUtil.get(String.format(WarehouseDao.KEY, 1))).thenReturn(warehouse);

        Warehouse ret = warehouseDao.findById(1l);

        assertThat(ret.getId()).isEqualTo(warehouse.getId());
        assertThat(ret.getAddress()).isEqualTo(warehouse.getAddress());
        assertThat(ret.getName()).isEqualTo(warehouse.getName());
        assertThat(ret.getSenderMobile()).isEqualTo(warehouse.getSenderMobile());
        assertThat(ret.getSenderName()).isEqualTo(warehouse.getSenderName());
        assertThat(ret.getInvalid()).isEqualTo(warehouse.getInvalid());
        assertThat(ret.getPriority()).isEqualTo(warehouse.getPriority());
    }

    @Test
    public void findById2() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1l);
        warehouse.setAddress("北京,朝阳,东坝,朝阳新城第二曙光路14号");
        warehouse.setName("朝阳新城第二仓库");
        warehouse.setSenderMobile("139542562579");
        warehouse.setSenderName("阮杰");
        warehouse.setInvalid(Byte.valueOf("0"));
        warehouse.setPriority(10001);

        Mockito.when(redisUtil.hasKey(String.format(WarehouseDao.KEY, 1))).thenReturn(false);


        Warehouse ret = warehouseDao.findById(1l);

        assertThat(ret.getId()).isEqualTo(warehouse.getId());
        assertThat(ret.getAddress()).isEqualTo(warehouse.getAddress());
        assertThat(ret.getName()).isEqualTo(warehouse.getName());
        assertThat(ret.getSenderMobile()).isEqualTo(warehouse.getSenderMobile());
        assertThat(ret.getSenderName()).isEqualTo(warehouse.getSenderName());
        assertThat(ret.getInvalid()).isEqualTo(warehouse.getInvalid());
        assertThat(ret.getPriority()).isEqualTo(warehouse.getPriority());
    }

    @Test
    public void saveWarehouse() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        Warehouse warehouse = new Warehouse();
        warehouse.setName("测试");
        warehouse.setAddress("测试");
        warehouse.setRegionId(7l);
        warehouse.setSenderName("张三");
        warehouse.setSenderMobile("1323232323");
        warehouse.setPriority(1232);

        Warehouse ret = warehouseDao.saveWarehouse(1l, warehouse, user);


        assertThat(ret.getAddress()).isEqualTo("测试");
        assertThat(ret.getName()).isEqualTo("测试");
        assertThat(ret.getSenderMobile()).isEqualTo("1323232323");
        assertThat(ret.getSenderName()).isEqualTo("张三");
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getPriority()).isEqualTo(1232);
    }

    @Test
    public void retrieveWarehouseByShopId() {
        Warehouse ret = warehouseDao.retrieveWarehouseByShopId(1l, 1, 10).getList().get(1);

        assertThat(ret.getAddress()).isEqualTo("湖北,潜江,渔洋,苏湖林场曙光路14号");
        assertThat(ret.getName()).isEqualTo("苏湖林场仓库");
        assertThat(ret.getSenderMobile()).isEqualTo("13948733198");
        assertThat(ret.getSenderName()).isEqualTo("钱峰");
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getPriority()).isEqualTo(1000);
    }

    @Test
    public void saveById() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        Warehouse warehouse = new Warehouse();
        warehouse.setName("朝阳新城第二仓库");
        warehouse.setAddress("北京,朝阳,东坝,朝阳新城第二曙光路14号");
        warehouse.setRegionId(7l);
        warehouse.setSenderName("张三");
        warehouse.setSenderMobile("1323232323");
        warehouse.setPriority(1232);
        warehouseDao.saveById(warehouse, 1l, 1l, user);
    }

    @Test
    public void delById() {
        warehouseDao.delById(1l);
    }

    @Test
    public void suspendWarehouse() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        warehouseDao.suspendWarehouse(1l, 1l, user);
    }

    @Test
    public void resumeWarehouse() {
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        warehouseDao.suspendWarehouse(1l, 1l, user);
    }
}
