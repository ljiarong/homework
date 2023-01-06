package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.oomall.freight.service.openfeign.JtService;
import org.springframework.stereotype.Service;

@Service
public class JtAdaptor extends JtService implements BillCodeAdaptor{

    public String createBillCode() {
        String ret = super.createJtBillCode();
        return ret;
    }
}
