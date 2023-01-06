package cn.edu.xmu.oomall.freight.service.openfeign;

import com.mifmif.common.regex.Generex;
import org.springframework.stereotype.Service;

@Service
public class ZtService {
    Generex generex = new Generex("ZTO[0-9]{12}$");
    public String createZtBillCode() {
        String random = generex.random();
        return random;
    }
}
