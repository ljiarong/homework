package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;

import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressService {
    @Autowired
    private ExpressDao expressDao;
    @Autowired
    ShopLogisticsDao shopLogisticsDao;

    @Autowired
    BillCodeAdaptorFactory billCodeAdaptorFactory;
    /**
     * 商户获得运单信息。
     */
    public Express getExpressById(Long id){
        return expressDao.getExpressById(id);

    }
    /**
     * 商户新建运单。
     */
    public ReturnObject addExpressById(Long shopId, ExpressVo expressVo, UserDto user) {
        ShopLogistics shopLogistics=new ShopLogistics();
        List<ShopLogistics> shopLogistics1=shopLogisticsDao.retrieveByShopId(shopId);
        shopLogistics=shopLogistics1.get(0);
        Long shopLogisticsId =shopLogistics.getLogisticsId();
        expressDao.saveExpress(shopId, billCodeAdaptorFactory.createBillCodeAdaptor(shopLogisticsId),expressVo, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户更新运单信息。
     */
    public ReturnObject updateExpress(Long shopId, Long id, Byte status, UserDto user) {
        expressDao.updateExpress(shopId, id, status, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户取消运单。
     */
    public ReturnObject cancelExpress(Long shopId, Long id, UserDto user) {
        expressDao.cancelExpress(shopId, id,user);
        return new ReturnObject(ReturnNo.OK);
    }

}
