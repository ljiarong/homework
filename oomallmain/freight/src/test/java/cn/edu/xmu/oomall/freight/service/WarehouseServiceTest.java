package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseRegionDao;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.service.dto.CreateWarehouseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class WarehouseServiceTest {

    @Autowired
    private FreightService freightService;
    @Test
    public void createWarehouse(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseVo warehouse = new WarehouseVo();
        warehouse.setName("测试");
        warehouse.setAddress("测试");
        warehouse.setRegionId(7l);
        warehouse.setSenderName("张三");
        warehouse.setSenderMobile("1323232323");
        warehouse.setPriority(1232);

        CreateWarehouseDto ret = freightService.createWarehouse(1l, warehouse, user);

        assertThat(ret.getName()).isEqualTo("测试");
        assertThat(ret.getAddress()).isEqualTo("测试");
        assertThat(ret.getRegion().getId()).isEqualTo(7l);
        assertThat(ret.getRegion().getName()).isEqualTo("东厂社区居委会");
        assertThat(ret.getSenderName()).isEqualTo("张三");
        assertThat(ret.getSenderMobile()).isEqualTo("1323232323");
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getPriority()).isEqualTo(1232);



    }
    @Test
    public void retrieveWarehouse(){
        CreateWarehouseDto ret = freightService.retrieveWarehouse(1l, 1, 10).getList().get(1);
        assertThat(ret.getId()).isEqualTo(10l);
        assertThat(ret.getAddress()).isEqualTo("湖北,潜江,渔洋,苏湖林场曙光路14号");
        assertThat(ret.getName()).isEqualTo("苏湖林场仓库");
        assertThat(ret.getSenderMobile()).isEqualTo("13948733198");
        assertThat(ret.getSenderName()).isEqualTo("钱峰");
        assertThat(ret.getInvalid()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getPriority()).isEqualTo(1000);
        assertThat(ret.getRegion().getId()).isEqualTo(450826l);
        assertThat(ret.getRegion().getName()).isEqualTo("苏湖林场生活区");
    }
    @Test
    public void updateWarehouse(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseVo warehouse = new WarehouseVo();
        warehouse.setName("朝阳新城第二仓库");
        warehouse.setAddress("北京,朝阳,东坝,朝阳新城第二曙光路14号");
        warehouse.setRegionId(7l);
        warehouse.setSenderName("张三");
        warehouse.setSenderMobile("1323232323");
        warehouse.setPriority(1232);
        ReturnObject ret = freightService.updateWarehouse(1l, 1l, warehouse, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void delWarehouse(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.delWarehouse(1l, 1l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void suspendWarehouse(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.suspendWarehouse(1l, 1l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
    @Test
    public void resumeWarehouse(){
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        ReturnObject ret = freightService.resumeWarehouse(1l, 1l, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }
}
