//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.goods.dao.bo.Onsale;
import cn.edu.xmu.oomall.goods.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.goods.mapper.OnsalePoMapper;
import cn.edu.xmu.oomall.goods.mapper.po.OnsalePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
@RefreshScope
public class OnsaleDao {

    private static final String KEY = "O%d";
    private static final String VALID_KEY = "OV%d";

    private final static Logger logger = LoggerFactory.getLogger(OnsaleDao.class);

    public  final static Onsale NOTEXIST = new Onsale(){{
        setId(-1L);
    }};

    @Value("${oomall.onsale.timeout}")
    private int timeout;

    private OnsalePoMapper onsalePoMapper;

    private ActivityDao activityDao;

    private ShopDao shopDao;

    private ProductDao productDao;

    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    public OnsaleDao(OnsalePoMapper onsalePoMapper, ActivityDao activityDao, ShopDao shopDao, ProductDao productDao, RedisUtil redisUtil) {
        this.onsalePoMapper = onsalePoMapper;
        this.activityDao = activityDao;
        this.shopDao = shopDao;
        this.productDao = productDao;
        this.redisUtil = redisUtil;
    }

    private Onsale getBo(OnsalePo po, Optional<String> redisKey){
        Onsale bo = Onsale.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
        .quantity(po.getQuantity()).maxQuantity(po.getMaxQuantity()).price(po.getPrice()).endTime(po.getEndTime()).beginTime(po.getBeginTime()).invalid(po.getInvalid()).type(po.getType()).productId(po.getProductId()).shopId(po.getShopId()).build();
        Long newTimeout = this.getNewTimeout(po.getEndTime());
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, newTimeout));
        return bo;
    }

    /**
     * ????????????????????????????????????onsale???endtime
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 19:20
     * @param endTime
     * @return
     */
    private Long getNewTimeout(LocalDateTime endTime) {
        Long diff = Duration.between(LocalDateTime.now(), endTime).toSeconds();
        Long newTimeout = Math.min(this.timeout,  diff);
        return newTimeout;
    }

    private void setBo(Onsale bo){
        bo.setActivityDao(this.activityDao);
        bo.setShopDao(this.shopDao);
        bo.setProductDao(this.productDao);
    }
    /**
     * ???????????????????????????????????????
     *
     * @param productId ????????????
     * @return ????????????
     */
    public Onsale findLatestValidOnsaleByProductId(Long productId) throws RuntimeException{
        logger.debug("findLatestValidOnsale: id ={}",productId);
        String key = String.format(VALID_KEY, productId);
        if (redisUtil.hasKey(key)){
            Integer onsaleId = (Integer) redisUtil.get(key);
            if (!onsaleId.equals(NOTEXIST.getId().intValue())) {
                try {
                    Onsale bo = this.findById(Long.valueOf(onsaleId));
                    setBo(bo);
                    return bo;
                } catch (BusinessException e) {
                    if (ReturnNo.RESOURCE_ID_NOTEXIST != e.getErrno()) {
                        throw e;
                    }
                }
            }
            return NOTEXIST;
        }

        Pageable pageable = PageRequest.of(0, MAX_RETURN, Sort.by("beginTime").ascending());
        LocalDateTime now = LocalDateTime.now();
        Page<OnsalePo> retObj = this.onsalePoMapper.findByProductIdEqualsAndEndTimeAfter(productId, now, pageable);
        if (retObj.isEmpty() ){
            redisUtil.set(key, NOTEXIST.getId(), timeout);
            return NOTEXIST;
        }else{
            OnsalePo po = retObj.stream().limit(1).collect(Collectors.toList()).get(0);
            Onsale bo =  this.getBo(po, Optional.ofNullable(null));
            redisUtil.set(key, bo.getId(), getNewTimeout(bo.getEndTime()) );
            return bo;
        }
    }

    /**
     * ???id?????????
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 8:34
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Onsale findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            Onsale bo = (Onsale) redisUtil.get(key);
            setBo(bo);
            return bo;
        }
        Optional<OnsalePo> retObj = this.onsalePoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "??????", id));
        }else{
            OnsalePo po = retObj.get();
            return this.getBo(po, Optional.of(key));
        }

    }
   /*
     * ??????Onsale??????????????????
     * @param onsale Onsale??????
     * @param user ?????????????????????
     */
    public Onsale insert(Onsale onsale, UserDto user) {
        logger.debug("insertOnsale onsale={}",onsale);
        OnsalePo onsalePo = cloneObj(onsale, OnsalePo.class);

        if(null!=user){
            putGmtFields(onsalePo,"create");
            putUserFields(onsalePo,"creator",user);
        }
        onsalePo.setId(null);
        OnsalePo ret = this.onsalePoMapper.save(onsalePo);
        return cloneObj(ret,Onsale.class);
    }

    /**
     * ??????Onsale,??????????????????
     * @param onsale
     * @param user
     * @return redisKey
     */
    public String save(Onsale onsale, UserDto user){
        logger.debug("saveOnsale onsale={}",onsale);
        OnsalePo onsalePo = cloneObj(onsale, OnsalePo.class);
        if(null!=user){
            putGmtFields(onsalePo,"create");
            putUserFields(onsalePo,"creator",user);
        }
        assert(null != onsalePo.getId());

        OnsalePo newPo = this.onsalePoMapper.save(onsalePo);
        if (NOTEXIST.equals(newPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "??????", onsalePo.getId()));
        }
        return String.format(KEY,onsale.getId());
    }

    /**
     * ??????productId ??????Onsale????????????
     * @param productId
     * @param page
     * @param pageSize
     * @return
     */
    public List<Onsale> retrieveByProductId( Long productId, Integer page, Integer pageSize) {
        Pageable pageable=PageRequest.of(page,pageSize);
        Page<OnsalePo> retObj = this.onsalePoMapper.findByProductIdIs(productId, pageable);
        if(null==retObj){
            return new ArrayList<>();
        }
        List<Onsale> onsales = retObj.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        logger.debug("findOnsale : productId={}",productId);
        return onsales;
    }



    /**
     * ?????????????????????Onsale??????
     * @author Ming Qiu
     * <p>
     * date: 2022-12-10 19:04
     * @param productId
     * @param beginTime
     * @param endTime
     * @return
     */
    public Onsale findOverlapOnsale(Long productId, LocalDateTime beginTime, LocalDateTime endTime){
        if (null == productId){
            return null;
        }

        PageRequest pageable = PageRequest.of(0,1, Sort.by(Sort.Direction.ASC, "beginTime"));
        Page<OnsalePo> ret =  this.onsalePoMapper.findOverlap(productId, beginTime, endTime, pageable);
        if (!ret.isEmpty()){
            OnsalePo po = ret.getContent().get(0);
            return this.getBo(po, Optional.ofNullable(null));
        }else{
            return NOTEXIST;
        }
    }


}
