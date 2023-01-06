package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseLogisticsPoExample;
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
public class WarehouseLogisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseLogisticsDao.class);

    private static final String KEY = "WL%d";

    @Value("${oomall.freight.timeout}")
    private long timeout;

    private WarehouseLogisticsPoMapper warehouseLogisticsPoMapper;

    private RedisUtil redisUtil;
    private ShopLogisticsDao shopLogisticsDao;
    private WarehouseDao warehouseDao;

    @Autowired
    public WarehouseLogisticsDao(WarehouseLogisticsPoMapper warehouseLogisticsPoMapper, RedisUtil redisUtil, ShopLogisticsDao shopLogisticsDao, WarehouseDao warehouseDao) {
        this.warehouseLogisticsPoMapper = warehouseLogisticsPoMapper;
        this.redisUtil = redisUtil;
        this.shopLogisticsDao = shopLogisticsDao;
        this.warehouseDao = warehouseDao;
    }

    private WarehouseLogistics getBo(WarehouseLogisticsPo po, String redisKey) {
        WarehouseLogistics ret = CloneFactory.copy(new WarehouseLogistics(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setWarehouseDao(this.warehouseDao);
        ret.setShopLogisticsDao(this.shopLogisticsDao);
        return ret;

    }


    public void deleByWarehouseId(Long id) {
        if (null != id) {
            WarehouseLogisticsPoExample warehouseLogisticsPoExample = new WarehouseLogisticsPoExample();
            warehouseLogisticsPoExample.createCriteria().andWarehouseIdEqualTo(id);
            List<WarehouseLogisticsPo> warehouseLogisticsPos = warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
            if (warehouseLogisticsPos.size() > 0 && null != warehouseLogisticsPos) {
                warehouseLogisticsPos.stream().forEach(po -> {
                            warehouseLogisticsPoMapper.deleteByPrimaryKey(po.getId());
                            redisUtil.del(String.format(WarehouseLogisticsDao.KEY, po.getId()));
                        }
                );
            }

        }
    }

    public List<WarehouseLogisticsPo> findByIdAndLogisticsId(Long id, Long lid) {
        WarehouseLogisticsPoExample warehouseLogisticsPoExample = new WarehouseLogisticsPoExample();
        warehouseLogisticsPoExample.createCriteria().andWarehouseIdEqualTo(id).andShopLogisticsIdEqualTo(lid);
        return warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
    }

    public void save(Long id, Long lid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        WarehouseLogisticsPo warehouseLogisticsPo = new WarehouseLogisticsPo();
        warehouseLogisticsPo.setWarehouseId(id);
        warehouseLogisticsPo.setShopLogisticsId(lid);
        warehouseLogisticsPo.setBeginTime(beginAndEndTimeVo.getBeginTime());
        warehouseLogisticsPo.setEndTime(beginAndEndTimeVo.getEndTime());
        warehouseLogisticsPo.setInvalid(WarehouseLogistics.VALID);
        putUserFields(warehouseLogisticsPo, "creator", user);
        putGmtFields(warehouseLogisticsPo, "create");
        warehouseLogisticsPoMapper.insertSelective(warehouseLogisticsPo);
    }

    public void update(Long id, Long lid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {

        WarehouseLogisticsPoExample warehouseLogisticsPoExample = new WarehouseLogisticsPoExample();
        warehouseLogisticsPoExample.createCriteria().andWarehouseIdEqualTo(lid).andShopLogisticsIdEqualTo(id);
        List<WarehouseLogisticsPo> warehouseLogisticsPos = warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
        if (warehouseLogisticsPos.size() > 0 && warehouseLogisticsPos != null) {
            warehouseLogisticsPos.stream().forEach(po ->
            {
                putUserFields(po, "modifier", user);
                putGmtFields(po, "modified");
                po.setBeginTime(beginAndEndTimeVo.getBeginTime());
                po.setEndTime(beginAndEndTimeVo.getEndTime());
                warehouseLogisticsPoMapper.updateByPrimaryKeySelective(po);
                redisUtil.del(String.format(WarehouseLogisticsDao.KEY, po.getId()));
            });
        }

    }

    public void deleByWarehouseIdAndLogisticsId(Long id, Long lid, List<WarehouseLogisticsPo> byIdAndLogisticsId) {
//
      if(null!=byIdAndLogisticsId&&byIdAndLogisticsId.size()>0){
          byIdAndLogisticsId.stream().forEach(po -> {
              redisUtil.del(String.format(WarehouseLogisticsDao.KEY, po.getId()));
              warehouseLogisticsPoMapper.deleteByPrimaryKey(po.getId());
          });
      }
    }

    public List<WarehouseLogistics> retrieveByWarehouseId(Long id,Integer page,Integer pageSize) {
        List<WarehouseLogistics> ret = null;
        if (null != id) {
            WarehouseLogisticsPoExample warehouseLogisticsPoExample = new WarehouseLogisticsPoExample();
            warehouseLogisticsPoExample.createCriteria().andWarehouseIdEqualTo(id);
            PageHelper.startPage(page,pageSize);
            List<WarehouseLogisticsPo> warehouseLogisticsPos = warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
            if (null != warehouseLogisticsPos && warehouseLogisticsPos.size() > 0) {
                ret = warehouseLogisticsPos.stream().map(po ->
                        this.getBo(po, String.format(WarehouseLogisticsDao.KEY, po.getId()))).collect(Collectors.toList());

            } else {
                ret = new ArrayList<>();
            }
        }
        return ret;
    }
}
