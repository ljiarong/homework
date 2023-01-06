package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.oomall.freight.service.openfeign.SfService;
import org.springframework.stereotype.Service;

@Service
public class SfAdaptor extends SfService implements BillCodeAdaptor {
    public String createBillCode() {
        String ret = super.createSfBillCode();
        return ret;
    }
}
