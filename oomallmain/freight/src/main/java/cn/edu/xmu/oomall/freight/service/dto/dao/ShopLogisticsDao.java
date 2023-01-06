package cn.edu.xmu.oomall.freight.service.dto.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.mapper.generator.ShopLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;

@Repository
public class ShopLogisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(ShopLogisticsDao.class);

    private static final String KEY = "SL%d";

    @Value("${oomall.freight.timeout}")
    private long timeout;

    private ShopLogisticsPoMapper shopLogisticsPoMapper;

    private RedisUtil redisUtil;
    private LogisticsDao logisticsDao;

    @Autowired
    public ShopLogisticsDao(ShopLogisticsPoMapper shopLogisticsPoMapper, RedisUtil redisUtil, LogisticsDao logisticsDao) {
        this.shopLogisticsPoMapper = shopLogisticsPoMapper;
        this.redisUtil = redisUtil;
        this.logisticsDao = logisticsDao;
    }

    private ShopLogistics getBo(ShopLogisticsPo po, String redisKey) {
        ShopLogistics ret = CloneFactory.copy(new ShopLogistics(), po);

        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setLogisticsDao(this.logisticsDao);
        return ret;

    }

    /**
     * 延迟加载
     */
    public ShopLogistics findById(Long shopLogisticsId) {
        ShopLogistics shopLogistics = null;
        if (null != shopLogisticsId) {
            logger.debug("findObjById: shopLogisticsId = {}", shopLogisticsId);
            String key = String.format(KEY, shopLogisticsId);
            if (redisUtil.hasKey(key)) {
                shopLogistics = (ShopLogistics) redisUtil.get(key);
            } else {
                ShopLogisticsPo po = this.shopLogisticsPoMapper.selectByPrimaryKey(shopLogisticsId);
                if (null == po) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺物流渠道", shopLogisticsId));
                }
                shopLogistics = this.getBo(po, String.format(ShopLogisticsDao.KEY, po.getId()));
            }
        }
        return shopLogistics;
    }


    public List<ShopLogistics> RetrieveByShopIdAndId(Long shopId, List<Long> ids, Integer page, Integer pageSize) {
        List<ShopLogistics> ret = null;
        if (null != shopId && null != ids) {
            ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
            shopLogisticsPoExample.createCriteria().andIdIn(ids).andShopIdEqualTo(shopId);
            shopLogisticsPoExample.setOrderByClause("priority");
            PageHelper.startPage(page, pageSize);
            List<ShopLogisticsPo> shopLogisticsPos = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
            if (null != shopLogisticsPos && shopLogisticsPos.size() > 0) {
                ret = shopLogisticsPos.stream().map(po -> this.getBo(po, String.format(ShopLogisticsDao.KEY, po.getId()
                ))).collect(Collectors.toList());
            }
        } else {
            ret = new ArrayList<>();
        }
        return ret;
    }

    public ShopLogistics findByLogisticsAndShopId(Long shopId, Long logisticsId) {
        ShopLogistics ret = null;
        if (null != shopId && null != logisticsId) {
            ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
            shopLogisticsPoExample.createCriteria().andShopIdEqualTo(shopId).andLogisticsIdEqualTo(logisticsId);
            PageHelper.startPage(1, 1, false);
            List<ShopLogisticsPo> poList = this.shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
            if (poList.size() > 0) {
                ret = this.getBo(poList.get(0), String.format(ShopLogisticsDao.KEY, poList.get(0).getId()));
            }


        }
        return ret;
    }

    public ShopLogistics save(ShopLogisticsVo shopLogisticsVo, Long shopId, UserDto user) {
        ShopLogisticsPo po = new ShopLogisticsPo();
        po.setInvalid(ShopLogistics.VALID);
        po.setLogisticsId(shopLogisticsVo.getLogisticsId());
        po.setSecret(shopLogisticsVo.getSecret());
        po.setPriority(shopLogisticsVo.getPriority());
        po.setShopId(shopId);
        putUserFields(po, "creator", user);
        putGmtFields(po, "create");
        shopLogisticsPoMapper.insertSelective(po);
        return this.getBo(po, String.format(ShopLogisticsDao.KEY, po.getId()));
    }

    public List<ShopLogistics> retrieveByShopId(Long shopId) {
        List<ShopLogistics> ret = null;
        ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
        shopLogisticsPoExample.createCriteria().andShopIdEqualTo(shopId);
        shopLogisticsPoExample.setOrderByClause("priority ASC");
        List<ShopLogisticsPo> shopLogisticsPos = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
        if (shopLogisticsPos.size() > 0 && shopLogisticsPos != null) {
            ret = shopLogisticsPos.stream().map(po ->
                    this.getBo(po, String.format(ShopLogisticsDao.KEY, po.getId()))).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return ret;
    }

    /*public String update(Long shopId, ShopLogistics shopLogistics, UserDto user) {
        ShopLogisticsPo shopLogisticsPo = CloneFactory.copy(shopLogistics, new ShopLogisticsPo());
        String key = null;
        if (shopLogisticsPo != null && shopId != null) {
            shopLogisticsPo.setShopId(shopId);
            putUserFields(shopLogisticsPo, "modifier", user);
            putGmtFields(shopLogisticsPo, "modified");
            shopLogisticsPoMapper.updateByPrimaryKey(shopLogisticsPo);
            key = String.format(ShopLogisticsDao.KEY, shopLogisticsPo.getId());
        }
        return key;
    }*/

    public void suspendShopLogistics(Long shopId, Long id, UserDto user) {
        if (null != shopId && null != id && null != user) {
            ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
            shopLogisticsPoExample.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
            List<ShopLogisticsPo> shopLogisticsPos = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
            if (shopLogisticsPos != null && shopLogisticsPos.size() > 0) {
                shopLogisticsPos.stream().forEach(po -> {
                    putUserFields(po, "modifier", user);
                    putGmtFields(po, "Modified");
                    po.setInvalid(ShopLogistics.INVALID);
                    redisUtil.del(String.format(ShopLogisticsDao.KEY,po.getId()));
                    shopLogisticsPoMapper.updateByPrimaryKeySelective(po);
                });
            }
        }
    }

    public void resumeShopLogistics(Long shopId, Long id, UserDto user) {
        if (null != shopId && null != id && null != user) {
            ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
            shopLogisticsPoExample.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
            List<ShopLogisticsPo> shopLogisticsPos = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
            if (shopLogisticsPos != null && shopLogisticsPos.size() > 0) {
                shopLogisticsPos.stream().forEach(po -> {
                    putUserFields(po, "modifier", user);
                    putGmtFields(po, "Modified");
                    po.setInvalid(ShopLogistics.VALID);
                    redisUtil.del(String.format(ShopLogisticsDao.KEY,po.getId()));
                    shopLogisticsPoMapper.updateByPrimaryKeySelective(po);
                });
            }
        }
    }

    public ShopLogistics findByShopLogisticsAndShopId(Long shopId, Long id) {
        ShopLogistics ret = null;
        if (null != shopId && null != id) {
            ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
            shopLogisticsPoExample.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
            PageHelper.startPage(1, 1, false);
            List<ShopLogisticsPo> poList = this.shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
            if (poList.size() > 0) {
                ret = this.getBo(poList.get(0), String.format(ShopLogisticsDao.KEY, poList.get(0).getId()));
            }


        }
        return ret;
    }
}
