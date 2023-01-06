package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class LogisticsDaoTest {
    @Autowired
    private LogisticsDao logisticsDao;
    @MockBean
    private RedisUtil redisUtil;
    @Test
    public void findById1(){
        Logistics logistics = new Logistics();
        logistics.setId(1l);
        logistics.setName("顺丰快递");
        logistics.setAppId("SF1001");
        logistics.setSnPattern("^SF[A-Za-z0-9-]{4,35}$");

        Mockito.when(redisUtil.hasKey(String.format(LogisticsDao.KEY, 1))).thenReturn(true);
        Mockito.when(redisUtil.get(String.format(LogisticsDao.KEY, 1))).thenReturn(logistics);

        Logistics ret = logisticsDao.findById(1l);

        assertThat(ret.getId()).isEqualTo(logistics.getId());
        assertThat(ret.getName()).isEqualTo(logistics.getName());
        assertThat(ret.getAppId()).isEqualTo(logistics.getAppId());
        assertThat(ret.getSnPattern()).isEqualTo(logistics.getSnPattern());

    }
    @Test
    public void  findById2(){
        Mockito.when(redisUtil.hasKey(String.format(LogisticsDao.KEY, 1))).thenReturn(false);

        Logistics ret = logisticsDao.findById(1l);

        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getName()).isEqualTo("顺丰快递");
        assertThat(ret.getAppId()).isEqualTo("SF1001");
        assertThat(ret.getSnPattern()).isEqualTo("^SF[A-Za-z0-9-]{4,35}$");
    }
    @Test
    public void  retrieve(){
        Logistics ret = logisticsDao.retrieve().get(0);
        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getName()).isEqualTo("顺丰快递");
        assertThat(ret.getAppId()).isEqualTo("SF1001");
        assertThat(ret.getSnPattern()).isEqualTo("^SF[A-Za-z0-9-]{4,35}$");
    }
}
