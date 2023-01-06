package cn.edu.xmu.oomall.freight.service.dto.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.mapper.generator.LogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LogisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(LogisticsDao.class);

    public static final String KEY = "L%d";

    private RedisUtil redisUtil;
    @Value("${oomall.freight.timeout}")
    private long timeout;
    private LogisticsPoMapper logisticsPoMapper;

    @Autowired
    public LogisticsDao(RedisUtil redisUtil, LogisticsPoMapper logisticsPoMapper) {
        this.redisUtil = redisUtil;
        this.logisticsPoMapper = logisticsPoMapper;
    }

    private Logistics getBo(LogisticsPo po, String redisKey) {
        Logistics ret = CloneFactory.copy(new Logistics(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        return ret;
    }

    public Logistics findById(Long logisticsId) throws RuntimeException {
        Logistics logistics = null;
        if (null != logisticsId) {
            logger.debug("findObjById: id = {}", logisticsId);
            String key = String.format(KEY, logisticsId);
            if (redisUtil.hasKey(key)) {
                logistics = (Logistics) redisUtil.get(key);
            } else {
                LogisticsPo po = this.logisticsPoMapper.selectByPrimaryKey(logisticsId);

                if (null == po) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流渠道", logisticsId));
                }
                logistics = this.getBo(po, String.format(LogisticsDao.KEY, po.getId()));

            }
        }
        return logistics;
    }

    public List<Logistics> retrieve() {
        LogisticsPoExample logisticsPoExample = new LogisticsPoExample();
        List<LogisticsPo> logisticsPos = logisticsPoMapper.selectByExample(logisticsPoExample);
        List<Logistics> ret = null;
        if (null != logisticsPos && logisticsPos.size() > 0) {
            ret = logisticsPos.stream().map(po ->
                    this.getBo(po, String.format(LogisticsDao.KEY, po.getId()))).collect(Collectors.toList());

        } else {
            ret = new ArrayList<>();
        }
        return ret;
    }
}
