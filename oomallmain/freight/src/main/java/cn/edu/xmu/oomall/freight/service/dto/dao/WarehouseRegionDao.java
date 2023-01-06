package cn.edu.xmu.oomall.freight.service.dto.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseRegionPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseRegionPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseRegionPoExample;
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
public class WarehouseRegionDao {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseLogisticsDao.class);

    private static final String KEY = "WR%d";

    @Value("${oomall.freight.timeout}")
    private long timeout;

    private WarehouseRegionPoMapper warehouseRegionPoMapper;

    private RedisUtil redisUtil;
    private WarehouseDao warehouseDao;
    private RegionDao regionDao;

    @Autowired
    public WarehouseRegionDao(WarehouseRegionPoMapper warehouseRegionPoMapper, RedisUtil redisUtil, WarehouseDao warehouseDao, RegionDao regionDao) {
        this.warehouseRegionPoMapper = warehouseRegionPoMapper;
        this.redisUtil = redisUtil;
        this.warehouseDao = warehouseDao;
        this.regionDao = regionDao;
    }

    private WarehouseRegion getBo(WarehouseRegionPo po, String redisKey) {
//

        WarehouseRegion ret = CloneFactory.copy(new WarehouseRegion(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setWarehouseDao(this.warehouseDao);
        ret.setRegionDao(this.regionDao);
        return ret;

    }


    public WarehouseRegion findByIdAndRegionId(Long id, Long wid) {
        WarehouseRegion ret = null;
        WarehouseRegionPoExample warehouseRegionPoExample = new WarehouseRegionPoExample();
        warehouseRegionPoExample.createCriteria().andRegionIdEqualTo(id).andWarehouseIdEqualTo(wid);
        List<WarehouseRegionPo> warehouseRegionPos = warehouseRegionPoMapper.selectByExample(warehouseRegionPoExample);
        if (warehouseRegionPos.size() > 0 && warehouseRegionPos != null) {
            ret = this.getBo(warehouseRegionPos.get(0), String.format(WarehouseRegionDao.KEY, warehouseRegionPos.get(0).getId()));
        }
        return ret;
    }

    public void save(Long shopId, Long wid, Long id, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        WarehouseRegionPo warehouseRegionPo = new WarehouseRegionPo();

        warehouseRegionPo.setBeginTime(beginAndEndTimeVo.getBeginTime());
        warehouseRegionPo.setEndTime(beginAndEndTimeVo.getEndTime());
        warehouseRegionPo.setRegionId(id);
        warehouseRegionPo.setWarehouseId(wid);
        putUserFields(warehouseRegionPo, "creator", user);
        putGmtFields(warehouseRegionPo, "create");
        warehouseRegionPoMapper.insertSelective(warehouseRegionPo);
    }

    public List<WarehouseRegion> retrieveByRegionId(Long id) {
        WarehouseRegionPoExample warehouseRegionPoExample = new WarehouseRegionPoExample();
        warehouseRegionPoExample.createCriteria().andRegionIdEqualTo(id);

        List<WarehouseRegionPo> warehouseRegionPos = warehouseRegionPoMapper.selectByExample(warehouseRegionPoExample);
        if (warehouseRegionPos.size() > 0) {
            List<WarehouseRegion> list = warehouseRegionPos.stream().map(po ->
                            this.getBo(po, String.format(WarehouseRegionDao.KEY, po.getId())))
                    .collect(Collectors.toList());
            return list;

        } else {
            return new ArrayList<>();
        }
    }

    public void deleByWarehouseIdAndRegionId(Long wid, Long id) {
        if (null != wid && null != id) {
            WarehouseRegionPoExample warehouseRegionPoExample = new WarehouseRegionPoExample();
            warehouseRegionPoExample.createCriteria().andWarehouseIdEqualTo(wid).andRegionIdEqualTo(id);
            List<WarehouseRegionPo> warehouseRegionPos = warehouseRegionPoMapper.selectByExample(warehouseRegionPoExample);
            if (null != warehouseRegionPos && warehouseRegionPos.size() > 0) {
                warehouseRegionPos.stream().forEach(po -> {
                    warehouseRegionPoMapper.deleteByPrimaryKey(po.getId());
                    redisUtil.del(String.format(WarehouseRegionDao.KEY, po.getId()));
                });
            }
        }
    }


    public List<WarehouseRegion> retrieveByWarehouseId(Long id) {
        WarehouseRegionPoExample warehouseRegionPoExample = new WarehouseRegionPoExample();
        warehouseRegionPoExample.createCriteria().andWarehouseIdEqualTo(id);
        List<WarehouseRegionPo> warehouseRegionPos = warehouseRegionPoMapper.selectByExample(warehouseRegionPoExample);
        if (warehouseRegionPos.size() > 0) {
            List<WarehouseRegion> list = warehouseRegionPos.stream().map(po -> this.getBo(po, String.format(WarehouseRegionDao.KEY, po.getId()))).collect(Collectors.toList());
            return list;
        } else {
            return new ArrayList<>();
        }
    }


    public void deleByWarehouseId(Long id) {
        if (null != id) {
            WarehouseRegionPoExample warehouseRegionPoExample = new WarehouseRegionPoExample();
            warehouseRegionPoExample.createCriteria().andWarehouseIdEqualTo(id);
            List<WarehouseRegionPo> warehouseRegionPos = warehouseRegionPoMapper.selectByExample(warehouseRegionPoExample);
            if (null != warehouseRegionPos && warehouseRegionPos.size() > 0) {
                warehouseRegionPos.stream().forEach(po ->
                {
                    warehouseRegionPoMapper.deleteByPrimaryKey(po.getId());
                    redisUtil.del(String.format(WarehouseRegionDao.KEY, po.getId()));
                });
            }
        }
    }

    public String update(WarehouseRegion warehouseRegion, UserDto user) {

        WarehouseRegionPo warehouseRegionPo = CloneFactory.copy(new WarehouseRegionPo(), warehouseRegion);
        putUserFields(warehouseRegionPo, "modifier", user);
        putGmtFields(warehouseRegionPo, "modified");
        warehouseRegionPoMapper.updateByPrimaryKey(warehouseRegionPo);
        return String.format(WarehouseRegionDao.KEY, warehouseRegionPo.getId());
    }
}
