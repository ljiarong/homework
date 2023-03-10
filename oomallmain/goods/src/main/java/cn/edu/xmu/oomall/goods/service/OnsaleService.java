//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.controller.vo.OnSaleVo;
import cn.edu.xmu.oomall.goods.dao.OnsaleDao;
import cn.edu.xmu.oomall.goods.dao.ProductDao;
import cn.edu.xmu.oomall.goods.dao.bo.Onsale;
import cn.edu.xmu.oomall.goods.dao.bo.Product;
import cn.edu.xmu.oomall.goods.dao.bo.Shop;
import cn.edu.xmu.oomall.goods.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.goods.service.dto.*;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.model.ReturnNo.GOODS_PRICE_CONFLICT;
import static cn.edu.xmu.javaee.core.model.ReturnNo.STATENOTALLOW;
import static cn.edu.xmu.javaee.core.util.Common.*;

@Service
@Transactional
public class OnsaleService {

    private static final Logger logger = LoggerFactory.getLogger(OnsaleService.class);

    private OnsaleDao onsaleDao;

    private ProductDao productDao;

    private RedisUtil redisUtil;


    @Autowired
    public OnsaleService(OnsaleDao onsaleDao,ProductDao productDao,RedisUtil redisUtil) {
        this.onsaleDao = onsaleDao;
        this.productDao=productDao;
        this.redisUtil=redisUtil;
    }

    public OnsaleDto findById(Long id){
        Onsale bo = this.onsaleDao.findById(id);
        Product product = bo.getProduct();
        Shop shop = bo.getShop();
        List<IdNameDto> actDto = null;
        if (null != bo.getActList()){
            actDto = bo.getActList().stream().map(act -> IdNameDto.builder().id(act.getId()).name(act.getName()).build()).collect(Collectors.toList());
        }
        logger.debug("findOnsaleById: actDto ={}", actDto);
        OnsaleDto onsaleDto = OnsaleDto.builder().id(bo.getId()).price(bo.getPrice()).endTime(bo.getEndTime()).beginTime(bo.getBeginTime()).maxQuantity(bo.getMaxQuantity()).quantity(bo.getQuantity()).type(bo.getType())
                .product(IdNameDto.builder().id(product.getId()).name(product.getName()).build())
                .shop(IdNameTypeDto.builder().id(shop.getId()).name(shop.getName()).type(shop.getType()).build())
                .actList(actDto).build();

        return onsaleDto;
    }

    /**
     * ????????????????????????????????????,???????????????????????????
     * @param shopId
     * @param id
     * @param onsale
     * @param user
     * @return
     */
    public SimpleOnsaleDto insert(Long shopId, Long id,Onsale onsale, UserDto user) {
        Product product = this.productDao.findProductByBeginEnd(id, onsale.getBeginTime(), onsale.getEndTime());
        if(PLATFORM!=shopId && shopId!=product.getShop().getId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        Onsale ret = product.getValidOnsale();
        if(null!=ret){
            throw new BusinessException(ReturnNo.GOODS_PRICE_CONFLICT,String.format(GOODS_PRICE_CONFLICT.getMessage(),ret.getId()));
        }
        onsale.setProductId(id);
        onsale.setInvalid(Byte.valueOf("1"));
        logger.debug("insert: productId = {}", id);
        Onsale saveOnsale = this.onsaleDao.insert(onsale, user);
        SimpleOnsaleDto simpleOnsaleDto = cloneObj(saveOnsale, SimpleOnsaleDto.class);
        simpleOnsaleDto.setProduct(new IdNameDto(product.getId(), product.getName()));
        return simpleOnsaleDto;
    }

    /**
     * ????????????id??????Onsale??????
     * @param shopId
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public PageDto<SimpleOnsaleDto> retrieveByProductId( Long shopId,Long id, Integer page, Integer pageSize) {
        logger.debug("retrieveOnSale: productId = {}", id);
        Product product = this.productDao.findProductById(id);
        if(PLATFORM!=shopId && shopId!=product.getShop().getId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        List<Onsale> onsales = this.onsaleDao.retrieveByProductId(id,page,pageSize);
        List<SimpleOnsaleDto> ret = onsales.stream().map(obj -> {
            SimpleOnsaleDto dto = cloneObj(obj, SimpleOnsaleDto.class);
            dto.setProduct(new IdNameDto(product.getId(),product.getName()));
            return dto;
        }).collect(Collectors.toList());
        logger.debug("getOnsale: onsales={}", ret);
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * ?????????????????????????????????
     * @param id OnsaleId
     * @param user
     * @return
     */
    public void validateOnsale( Long shopId,Long id, UserDto user) {
        Onsale onsale = this.onsaleDao.findById(id);

        if(PLATFORM!=shopId && shopId!=onsale.getShop().getId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        Long productId = onsale.getProductId();
        Onsale validOnsale = this.productDao.findProductByBeginEnd(productId, LocalDateTime.now(), onsale.getEndTime()).getValidOnsale();
        if(null!=validOnsale&&validOnsale.getId()!=id){
            throw new BusinessException(ReturnNo.GOODS_PRICE_CONFLICT,String.format(GOODS_PRICE_CONFLICT.getMessage(),id));
        }
        onsale.setBeginTime(LocalDateTime.now());//????????????
        String key = this.onsaleDao.save(onsale, user);
        if (redisUtil.hasKey(key)){
            redisUtil.del(key);
        }

    }

    /**
     * ?????????????????????????????????
     * @param shopId ??????id
     * @param id ????????????id
     * @param user
     * @return
     */
    public void invalidateOnsale(Long shopId, Long id, UserDto user) {
        Onsale onsale = this.onsaleDao.findById(id);
        if(PLATFORM!=shopId && shopId!=onsale.getShop().getId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        if(onsale.getBeginTime().isAfter(LocalDateTime.now())){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        //???????????????????????????????????????BeginTime????????????
        onsale.setEndTime(LocalDateTime.now());
        String key = this.onsaleDao.save(onsale, user);
        if (redisUtil.hasKey(key)){
            redisUtil.del(key);
        }
    }


    /**
     * ???????????????????????????
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    public void delete(Long shopId, Long id, UserDto user) {
        Onsale onsale = this.onsaleDao.findById(id);
        if(PLATFORM!=shopId && shopId!=onsale.getShop().getId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        if(onsale.getBeginTime().isAfter(LocalDateTime.now())){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        //????????????????????????????????????Onsale??????
        onsale.setEndTime(LocalDateTime.now());
        String key = this.onsaleDao.save(onsale, user);
        if (redisUtil.hasKey(key)){
            redisUtil.del(key);
        }
    }

    /**
     * ????????????????????????????????????
     * @param shopId ??????id
     * @param id OnsaleId
     * @param user
     */
    public void save(Long shopId, Long id, Onsale onsale, UserDto user) {
        Onsale oldOnsale = onsaleDao.findById(id);
        if(PLATFORM != shopId && shopId != oldOnsale.getShopId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        }
        Product product = this.productDao.findProductByBeginEnd(oldOnsale.getProductId(), onsale.getBeginTime(), onsale.getEndTime());
        Onsale validOnsale = product.getValidOnsale();
        if(null!=validOnsale&&validOnsale.getId()!=id){
            throw new BusinessException(GOODS_PRICE_CONFLICT,String.format(ReturnNo.GOODS_PRICE_CONFLICT.getMessage(),id));
        }
        onsale.setId(id);
        String key = this.onsaleDao.save(onsale, user);
        if (redisUtil.hasKey(key)){
            redisUtil.del(key);
        }
    }

    /**
     * ??????quantity
     * @author Ming Qiu
     * <p>
     * date: 2022-12-15 10:24
     * @param quantity
     */
    public void incrQuantity(Long onsaleId ,int quantity){
        Onsale onsale = this.onsaleDao.findById(onsaleId);
        Integer newQuantity = onsale.getQuantity() + quantity;
        if (newQuantity < 0){
            logger.error("incrQuantity: onsale id = {} is oversold", onsaleId);
        }
        Onsale newOnsale = Onsale.builder().id(onsaleId).quantity(newQuantity).build();
        String key = this.onsaleDao.save(newOnsale, null);
        this.redisUtil.del(key);
    }


}
