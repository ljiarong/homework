package cn.edu.xmu.oomall.freight.service.openfeign;


import com.mifmif.common.regex.Generex;
import org.springframework.stereotype.Service;

@Service
public class SfService {
    Generex generex = new Generex("SF[A-Za-z0-9-]{4,35}$");
    public String createSfBillCode() {
        String random = generex.random();
        return random;
    }


}
