package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class RegionDaoTest {
    @Autowired
    private RegionDao regionDao;
    @MockBean
    private RedisUtil redisUtil;
    @Test
    public void  findById1() {
        Region region = new Region();
        region.setId(1l);
        region.setPid(0L);
        region.setName("北京市");
        region.setStatus(Byte.valueOf("0"));
        region.setLevel(Byte.valueOf("0"));
        region.setShortName("北京");
        region.setAreaCode(110000000000l);
        region.setZipCode(000000);
        region.setCityCode(null);
        region.setMergerName("北京");
        region.setPinyin("BeiJing");
        region.setLng(new BigDecimal(116.407526));
        region.setLat(new BigDecimal(39.904030));

        Mockito.when(redisUtil.hasKey(String.format(RegionDao.KEY, 1))).thenReturn(true);
        Mockito.when(redisUtil.get(String.format(RegionDao.KEY, 1))).thenReturn(region);

        Region ret = regionDao.findById(1L);

        assertThat(ret.getId()).isEqualTo(region.getId());
        assertThat(ret.getPid()).isEqualTo(region.getPid());
        assertThat(ret.getName()).isEqualTo(region.getName());
        assertThat(ret.getStatus()).isEqualTo(region.getStatus());
        assertThat(ret.getLevel()).isEqualTo(region.getLevel());
        assertThat(ret.getShortName()).isEqualTo(region.getShortName());
        assertThat(ret.getAreaCode()).isEqualTo(region.getAreaCode());
        assertThat(ret.getZipCode()).isEqualTo(region.getZipCode());
        assertThat(ret.getCityCode()).isEqualTo(region.getCityCode());
        assertThat(ret.getMergerName()).isEqualTo(region.getMergerName());
        assertThat(ret.getPinyin()).isEqualTo(region.getPinyin());
        assertThat(ret.getLng()).isEqualTo(region.getLng());
        assertThat(ret.getLat()).isEqualTo(region.getLat());
    }

    @Test
    public void findById2(){
        Mockito.when(redisUtil.hasKey(String.format(RegionDao.KEY, 1))).thenReturn(false);
        Region ret = regionDao.findById(1L);

        assertThat(ret.getId()).isEqualTo(1l);
        assertThat(ret.getPid()).isEqualTo(0l);
        assertThat(ret.getName()).isEqualTo("北京市");
        assertThat(ret.getStatus()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getLevel()).isEqualTo(Byte.valueOf("0"));
        assertThat(ret.getShortName()).isEqualTo("北京");
        assertThat(ret.getAreaCode()).isEqualTo(110000000000l);
        assertThat(ret.getZipCode()).isEqualTo(000000);
        assertThat(ret.getCityCode()).isEqualTo("");
        assertThat(ret.getMergerName()).isEqualTo("北京");
        assertThat(ret.getPinyin()).isEqualTo("BeiJing");

    }
}
