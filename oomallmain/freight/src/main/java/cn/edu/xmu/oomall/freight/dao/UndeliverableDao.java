package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.mapper.generator.UndeliverablePoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.UndeliverablePo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.UndeliverablePoExample;
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
public class UndeliverableDao {
    private static final Logger logger = LoggerFactory.getLogger(UndeliverableDao.class);

    private static final String KEY = "U%d";

    @Value("${oomall.freight.timeout}")
    private long timeout;

    private UndeliverablePoMapper undeliverablePoMapper;

    private RedisUtil redisUtil;
    private RegionDao regionDao;
    private ShopLogisticsDao shopLogisticsDao;

    private Undeliverable getBo(UndeliverablePo po, String redisKey) {
        Undeliverable ret = CloneFactory.copy(new Undeliverable(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        ret.setRegionDao(this.regionDao);
        ret.setShopLogisticsDao(this.shopLogisticsDao);
        return ret;
    }

    @Autowired
    public UndeliverableDao(UndeliverablePoMapper undeliverablePoMapper, RedisUtil redisUtil, RegionDao regionDao, ShopLogisticsDao shopLogisticsDao) {
        this.undeliverablePoMapper = undeliverablePoMapper;
        this.redisUtil = redisUtil;
        this.regionDao = regionDao;
        this.shopLogisticsDao = shopLogisticsDao;
    }

    public void addUndeliverable(Long shopId, Long id, Long rid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        UndeliverablePo undeliverablePo = new UndeliverablePo();
        undeliverablePo.setShopLogisticsId(id);
        undeliverablePo.setRegionId(rid);
        undeliverablePo.setBeginTime(beginAndEndTimeVo.getBeginTime());
        undeliverablePo.setEndTime(beginAndEndTimeVo.getEndTime());
        putUserFields(undeliverablePo, "creator", user);
        putGmtFields(undeliverablePo, "create");
        undeliverablePoMapper.insert(undeliverablePo);
    }

    public void updateUndeliverable(Long shopId, Long id, Long rid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        UndeliverablePoExample undeliverablePoExample = new UndeliverablePoExample();
        undeliverablePoExample.createCriteria().andRegionIdEqualTo(rid).andShopLogisticsIdEqualTo(id);
        List<UndeliverablePo> undeliverablePos = undeliverablePoMapper.selectByExample(undeliverablePoExample);
        if (null != undeliverablePos && undeliverablePos.size() > 0) {
            undeliverablePos.stream().forEach(po -> {
                po.setBeginTime(beginAndEndTimeVo.getBeginTime());
                po.setEndTime(beginAndEndTimeVo.getEndTime());
                putUserFields(po, "modifier", user);
                putGmtFields(po, "modified");
                redisUtil.del(String.format(UndeliverableDao.KEY,po.getId()));
                undeliverablePoMapper.updateByPrimaryKey(po);
            });
        }
    }

    public void delUndeliverable(Long shopId, Long id, Long rid, UserDto user) {
        UndeliverablePoExample undeliverablePoExample = new UndeliverablePoExample();
        undeliverablePoExample.createCriteria().andRegionIdEqualTo(rid).andShopLogisticsIdEqualTo(id);
        List<UndeliverablePo> undeliverablePos = undeliverablePoMapper.selectByExample(undeliverablePoExample);
        if (null != undeliverablePos && undeliverablePos.size() > 0) {
            undeliverablePos.stream().forEach(po -> {
                undeliverablePoMapper.deleteByPrimaryKey(po.getId());
                redisUtil.del(String.format(UndeliverableDao.KEY,po.getId()));
            });
        }
    }

    public List<Undeliverable> retrieveByShopLogisticsId(Long id, Integer page, Integer pageSize) {
        UndeliverablePoExample undeliverablePoExample = new UndeliverablePoExample();
        undeliverablePoExample.createCriteria().andShopLogisticsIdEqualTo(id);
        PageHelper.startPage(page, pageSize);
        List<UndeliverablePo> undeliverablePos = undeliverablePoMapper.selectByExample(undeliverablePoExample);
        List<Undeliverable> ret = null;
        if (null != undeliverablePos && undeliverablePos.size() > 0) {
            ret = undeliverablePos.stream().map(po ->
                    this.getBo(po, String.format(UndeliverableDao.KEY, po.getId()))).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return ret;
    }
}
