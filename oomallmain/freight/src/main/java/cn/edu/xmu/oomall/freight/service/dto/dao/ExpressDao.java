package cn.edu.xmu.oomall.freight.service.dto.dao;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.*;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.Express;
import cn.edu.xmu.oomall.freight.mapper.generator.ExpressPoMapper;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.ShopLogistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;

@Repository
public class ExpressDao {
    private static final Logger logger = LoggerFactory.getLogger(ExpressDao.class);

    public static final String KEY = "E%d";

    @Value("${oomall.freight.timeout}")
    private long timeout;

    private ExpressPoMapper expressPoMapper;

    private RedisUtil redisUtil;

    private RegionDao regionDao;
    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    public ExpressDao(ExpressPoMapper expressPoMapper, RedisUtil redisUtil, RegionDao regionDao, ShopLogisticsDao shopLogisticsDao) {
        this.expressPoMapper = expressPoMapper;
        this.redisUtil = redisUtil;
        this.regionDao = regionDao;
        this.shopLogisticsDao = shopLogisticsDao;
    }


    private Express getBo(ExpressPo po, String redisKey) {
        Express ret = CloneFactory.copy(new Express(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setRegionDao(this.regionDao);
        ret.setShopLogisticsDao(this.shopLogisticsDao);
        return ret;
    }

    public Express findByBillCode(String billCode) {
        Express express = null;
        ExpressPoExample expressPoExample = new ExpressPoExample();
        expressPoExample.createCriteria().andBillCodeEqualTo(billCode);
        List<ExpressPo> expressPos = expressPoMapper.selectByExample(expressPoExample);
        if (null != expressPos && expressPos.size() > 0) {
            express = this.getBo(expressPos.get(0), String.format(ExpressDao.KEY, expressPos.get(0).getId()));
        }
        return express;
    }


    public void saveExpress(Long shopId, String billCode,ExpressVo expressVo, UserDto user) {
        ExpressPo expressPo = new ExpressPo();
        expressPo.setShopLogisticsId(expressVo.getShopLogisticId());
        expressPo.setBillCode(billCode);
        expressPo.setSenderName(expressVo.getSender().getName());
        expressPo.setSenderMobile(expressVo.getSender().getMobile());
        expressPo.setSenderRegionId(expressVo.getSender().getRegionId());
        expressPo.setSenderAddress(expressVo.getSender().getAddress());
        expressPo.setDeliveryName(expressVo.getDelivery().getName());
        expressPo.setDeliveryMobile(expressVo.getDelivery().getMobile());
        expressPo.setDeliveryRegionId(expressVo.getDelivery().getRegionId());
        expressPo.setDeliveryAddress(expressVo.getDelivery().getAddress());
        expressPo.setShopId(shopId);
        expressPo.setStatus((byte) 0);
        putUserFields(expressPo, "creator", user);
        putGmtFields(expressPo, "create");
        expressPoMapper.insertSelective(expressPo);
    }

    public Express getExpressById( Long id) {
        ExpressPo expressPo = expressPoMapper.selectByPrimaryKey(id);
        Express ret = CloneFactory.copy(new Express(), expressPo);
        return ret;
    }
    public void updateExpress(Long shopId, Long id, Byte status, UserDto user) {
        ExpressPo expressPo = expressPoMapper.selectByPrimaryKey(id);
        expressPo.setStatus(status);
    }

    public void cancelExpress(Long shopId, Long id, UserDto user) {
        expressPoMapper.deleteByPrimaryKey(id);
    }
}
