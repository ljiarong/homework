package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.oomall.freight.service.dto.SimpleLogisticsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class LogisticsServiceTest {
    @Autowired
    private FreightService freightService;
    @Test
    public void getLogistics(){
        SimpleLogisticsDto ret =(SimpleLogisticsDto) freightService.getLogistics("").getData();
        assertThat(ret.getId()).isEqualTo(1);
        assertThat(ret.getName()).isEqualTo("顺丰快递");
    }

}
