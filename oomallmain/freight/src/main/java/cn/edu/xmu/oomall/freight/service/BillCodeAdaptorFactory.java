package cn.edu.xmu.oomall.freight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillCodeAdaptorFactory {
    private static final Logger logger = LoggerFactory.getLogger(BillCodeAdaptorFactory.class);
    @Autowired
    public SfAdaptor sfAdaptor;
    @Autowired
    public ZtAdaptor ztAdaptor;
    @Autowired
    public JtAdaptor jtAdaptor;

    public String createBillCodeAdaptor(Long shopLogisticsId) {
        if(shopLogisticsId==1){
            return sfAdaptor.createBillCode();
        }
        else if(shopLogisticsId==2){
            return ztAdaptor.createBillCode();
        }
         else{
             return jtAdaptor.createBillCode();
         }


    }

}
