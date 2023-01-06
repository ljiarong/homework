package cn.edu.xmu.oomall.freight.service;

import com.mifmif.common.regex.Generex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class SfServiceTest {
    @Test
    public void test()
    {
        Generex generex = new Generex("^SF[A-Za-z0-9-]{4,35}$");
        String random = generex.random();
        System.out.println(random);
    }


}
