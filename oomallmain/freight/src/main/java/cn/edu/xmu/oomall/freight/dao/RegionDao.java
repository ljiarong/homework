package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.mapper.generator.RegionPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.RegionPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.RegionPoExample;
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

@Repository
public class RegionDao {
    private static final Logger logger = LoggerFactory.getLogger(RegionDao.class);

    public static final String KEY = "R%d";
    @Value("${oomall.freight.timeout}")
    private long timeout;
    private RedisUtil redisUtil;
    private RegionPoMapper regionPoMapper;

    @Autowired
    public RegionDao(RedisUtil redisUtil, RegionPoMapper regionPoMapper) {
        this.redisUtil = redisUtil;
        this.regionPoMapper = regionPoMapper;
    }

    private Region getBo(RegionPo po, String redisKey) {
        Region ret = CloneFactory.copy(new Region(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }

        return ret;
    }


    public Region findById(Long id) throws RuntimeException {
        Region region = null;
        if (null != id) {
            logger.debug("findObjById: id = {}", id);
            String key = String.format(RegionDao.KEY, id);
            if (redisUtil.hasKey(key)) {
                region = (Region) redisUtil.get(key);
            } else {
                  RegionPo po = this.regionPoMapper.selectByPrimaryKey(id);

                if (null == po) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "地区 ", id));
                }
                region = this.getBo(po, String.format(RegionDao.KEY, po.getId()));

            }
        }
        return region;
    }


}
