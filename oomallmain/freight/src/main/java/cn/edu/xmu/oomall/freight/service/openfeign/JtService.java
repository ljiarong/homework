package cn.edu.xmu.oomall.freight.service.openfeign;

import com.mifmif.common.regex.Generex;
import org.springframework.stereotype.Service;

@Service
public class JtService {
    Generex generex = new Generex("JT[0-9]{13}$");
    public String createJtBillCode() {
        String random = generex.random();
        return random;
    }
}
