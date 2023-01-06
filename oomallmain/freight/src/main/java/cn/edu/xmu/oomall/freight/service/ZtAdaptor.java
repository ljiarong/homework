package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.oomall.freight.service.openfeign.ZtService;
import org.springframework.stereotype.Service;

@Service
public class ZtAdaptor extends ZtService implements BillCodeAdaptor {
    public String createBillCode() {
        String ret = super.createZtBillCode();
        return ret;
    }
}
