package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ShopLogisticsDaoTest {
    @Autowired
    private ShopLogisticsDao shopLogisticsDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void findById1() {
        ShopLogistics shopLogistics = new ShopLogistics();
        shopLogistics.setId(2L);
        shopLogistics.setInvalid(ShopLogistics.VALID);
        shopLogistics.setSecret("secret2");
        shopLogistics.setPriority(113);

        Mockito.when(redisUtil.hasKey(String.format(LogisticsDao.KEY, 1))).thenReturn(true);
        Mockito.when(redisUtil.get(String.format(LogisticsDao.KEY, 1))).thenReturn(shopLogistics);
        ShopLogistics ret = shopLogisticsDao.findById(2l);
        assertThat(ret.getId()).isEqualTo(shopLogistics.getId());
        assertThat(ret.getInvalid()).isEqualTo(shopLogistics.getInvalid());
        assertThat(ret.getSecret()).isEqualTo(shopLogistics.getSecret());
        assertThat(ret.getPriority()).isEqualTo(shopLogistics.getPriority());

    }

    @Test
    public void findById2() {
        Mockito.when(redisUtil.hasKey(String.format(LogisticsDao.KEY, 1))).thenReturn(false);
        ShopLogistics ret = shopLogisticsDao.findById(2l);
        assertThat(ret.getId()).isEqualTo(2L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getPriority()).isEqualTo(113);
    }

    @Test
    public void RetrieveByShopIdAndId() {
        List<Long> longs = new ArrayList<>();
        longs.add(2L);
        ShopLogistics ret = shopLogisticsDao.RetrieveByShopIdAndId(1L, longs, 1, 10).get(0);
        assertThat(ret.getId()).isEqualTo(2L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getPriority()).isEqualTo(113);

    }

    @Test
    public void findByLogisticsAndShopId() {
        ShopLogistics ret = shopLogisticsDao.findByLogisticsAndShopId(1L, 2L);
        assertThat(ret.getId()).isEqualTo(2L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getPriority()).isEqualTo(113);
    }

    @Test
    public void save() {
        ShopLogisticsVo shopLogisticsVo = new ShopLogisticsVo();
        shopLogisticsVo.setLogisticsId(1L);
        shopLogisticsVo.setSecret("secret3");
        shopLogisticsVo.setPriority(12);
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);

        ShopLogistics ret = shopLogisticsDao.save(shopLogisticsVo, 11l, user);

        assertThat(ret.getLogistics().getId()).isEqualTo(1);
        assertThat(ret.getLogistics().getName()).isEqualTo("顺丰快递");
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret3");
        assertThat(ret.getPriority()).isEqualTo(12);
    }

    @Test
    public void retrieveByShopId() {
        ShopLogistics ret = shopLogisticsDao.retrieveByShopId(1l,1,10).get(1);
        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret1");
        assertThat(ret.getPriority()).isEqualTo(3);
    }

    @Test
    public void update() {
        ShopLogistics shopLogistics = shopLogisticsDao.findByShopLogisticsAndShopId(10l, 30l);
        shopLogistics.setSecret("secret2");
        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        String key = shopLogisticsDao.update(10l, shopLogistics, user);
        assertThat(key).isEqualTo("SL30");
    }
    @Test
    public void suspendShopLogistics(){

        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        shopLogisticsDao.suspendShopLogistics(1l,1l,user);
        ShopLogistics ret = shopLogisticsDao.findById(1l);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.INVALID);
    }
    @Test
    public void resumeShopLogistics(){

        UserDto user = new UserDto();
        user.setId(Long.valueOf(2));
        user.setName("test1");
        user.setUserLevel(1);
        shopLogisticsDao.resumeShopLogistics(1l,1l,user);
        ShopLogistics ret = shopLogisticsDao.findById(1l);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
    }
    @Test
    public void findByShopLogisticsAndShopId(){
        ShopLogistics ret = shopLogisticsDao.findByShopLogisticsAndShopId(1L, 2L);
        assertThat(ret.getLogisticsId()).isEqualTo(2L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getPriority()).isEqualTo(113);
    }
}
