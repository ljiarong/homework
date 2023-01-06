package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehousePoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import static cn.edu.xmu.oomall.freight.dao.bo.Warehouse.VALID;

@Repository
public class WarehouseDao {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseDao.class);

    @Value("${oomall.freight.timeout}")
    private long timeout;

    public static final String KEY = "W%d";

    private RedisUtil redisUtil;
    private WarehousePoMapper warehousePoMapper;
    private RegionDao regionDao;

    private Warehouse getBo(WarehousePo po, String redisKey) {
//
        Warehouse ret = CloneFactory.copy(new Warehouse(), po);

        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setRegionDao(this.regionDao);

        return ret;
    }

    @Autowired
    public WarehouseDao(RedisUtil redisUtil, WarehousePoMapper warehousePoMapper, RegionDao regionDao) {
        this.redisUtil = redisUtil;
        this.warehousePoMapper = warehousePoMapper;
        this.regionDao = regionDao;
    }

    public Warehouse findById(Long warehouseId) {
        Warehouse warehouse = null;
        if (null != warehouseId) {
            logger.debug("findObjById: warehouseId = {}", warehouseId);
            String key = String.format(KEY, warehouseId);
            if (redisUtil.hasKey(key)) {
                warehouse = (Warehouse) redisUtil.get(key);
            } else {
                WarehousePo po = this.warehousePoMapper.selectByPrimaryKey(warehouseId);
                if (null == po) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "对应仓库", warehouseId));
                }
                warehouse = this.getBo(po, String.format(WarehouseDao.KEY, po.getId()));

            }
        }
        return warehouse;
    }

    /**
     * 商户或管理员查询某个地区可以配送的所有仓库
     *
     * @param ids 地区Id
     * @return 仓库对象
     */
    public PageInfo<Warehouse> retrieveWarehouseByRegionId(List<Long> ids, Integer page, Integer pageSize) {
        List<Warehouse> ret = null;
        if (null != ids && ids.size() > 0) {
            WarehousePoExample warehousePoExample = new WarehousePoExample();
            warehousePoExample.createCriteria().andIdIn(ids).andInvalidEqualTo(VALID);
            warehousePoExample.setOrderByClause("priority");
            PageHelper.startPage(page, pageSize);
            List<WarehousePo> warehousePoList = warehousePoMapper.selectByExample(warehousePoExample);
            if (warehousePoList.size() > 0) {
                ret = warehousePoList.stream()
                        .map(po -> this.getBo(po, String.format(WarehouseDao.KEY, po.getId())))
                        .collect(Collectors.toList());
            } else {
                ret = new ArrayList<>();
            }
        }
        return new PageInfo<>(ret);
    }


    public Warehouse saveWarehouse(Long shopId, Warehouse warehouse, UserDto user) {
        WarehousePo warehousePo = CloneFactory.copy(new WarehousePo(), warehouse);
        warehousePo.setShopId(shopId);
        warehousePo.setInvalid(VALID);
        putUserFields(warehousePo, "creator", user);
        putGmtFields(warehousePo, "create");
        warehousePoMapper.insertSelective(warehousePo);
        return this.getBo(warehousePo, String.format(WarehouseDao.KEY, warehousePo.getId()));
    }

    public PageInfo<Warehouse> retrieveWarehouseByShopId(Long shopId, Integer page, Integer pageSize) {
        List<Warehouse> ret = null;
        if (null != shopId) {
            WarehousePoExample warehousePoExample = new WarehousePoExample();
            warehousePoExample.createCriteria().andShopIdEqualTo(shopId);
            warehousePoExample.setOrderByClause("priority ASC");
            PageHelper.startPage(page,pageSize);
            List<WarehousePo> warehousePoList = warehousePoMapper.selectByExample(warehousePoExample);
            if (warehousePoList.size() > 0) {
                ret = warehousePoList.stream()
                        .map(po -> this.getBo(po, String.format(WarehouseDao.KEY, po.getId())))
                        .collect(Collectors.toList());
            } else {
                ret = new ArrayList<>();
            }
        }
        return new PageInfo<>(ret);
    }

    public void saveById(Warehouse warehouse, Long shopId, Long id, UserDto user) {
        if (null != warehouse && null != warehouse.getId()) {
            WarehousePo warehousePo = CloneFactory.copy(new WarehousePo(), warehouse);
            warehousePo.setId(id);
            warehousePo.setInvalid(Warehouse.VALID);
            putUserFields(warehousePo, "modifier", user);
            putGmtFields(warehousePo, "Modified");
            warehousePo.setShopId(shopId);
            warehousePoMapper.updateByPrimaryKeySelective(warehousePo);
        }
    }

    public void delById( Long id) {
        if (null != id) {
            warehousePoMapper.deleteByPrimaryKey(id);
            redisUtil.del(String.format(WarehouseDao.KEY,id));
        }
    }

    public void suspendWarehouse(Long shopId, Long id, UserDto user) {
        if (null != shopId && null != id && null != user) {
            WarehousePoExample warehousePoExample = new WarehousePoExample();
            warehousePoExample.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
            List<WarehousePo> warehousePoList = warehousePoMapper.selectByExample(warehousePoExample);
            if (warehousePoList != null && warehousePoList.size() > 0) {
                warehousePoList.stream().forEach(po -> {
                    redisUtil.del(String.format(WarehouseDao.KEY,po.getId()));
                    putUserFields(po, "modifier", user);
                    putGmtFields(po, "Modified");
                    po.setInvalid(Warehouse.INVALID);
                    warehousePoMapper.updateByPrimaryKeySelective(po);
                });
            }
        }
    }

    public void resumeWarehouse(Long shopId, Long id, UserDto user) {
        if (null != shopId && null != id && null != user) {
            WarehousePoExample warehousePoExample = new WarehousePoExample();
            warehousePoExample.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
            List<WarehousePo> warehousePoList = warehousePoMapper.selectByExample(warehousePoExample);
            if (warehousePoList != null && warehousePoList.size() > 0) {
                warehousePoList.stream().forEach(po -> {
                    redisUtil.del(String.format(WarehouseDao.KEY,po.getId()));
                    putUserFields(po, "modifier", user);
                    putGmtFields(po, "Modified");
                    po.setInvalid(VALID);
                    warehousePoMapper.updateByPrimaryKeySelective(po);
                });
            }
        }
    }

    public void update(Long shopId, Long wid, Integer priority) {
        if (null != shopId && null != wid && null != priority) {
            WarehousePo warehousePo = warehousePoMapper.selectByPrimaryKey(wid);
            if (null != warehousePo) {
                warehousePo.setPriority(priority);
                warehousePoMapper.updateByPrimaryKeySelective(warehousePo);
            }

        }
    }
}
