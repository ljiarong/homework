package cn.edu.xmu.oomall.freight.service.dto.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.service.dto.dao.RegionDao;
import cn.edu.xmu.oomall.freight.service.dto.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.UndeliverablePo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@CopyFrom(UndeliverablePo.class)
public class Undeliverable extends OOMallObject implements Serializable {
    private static  final Logger logger = LoggerFactory.getLogger(Undeliverable.class);


    private Long id;

    private Long regionId;

    private Long shopLogisticsId;


    private LocalDateTime beginTime;

    private LocalDateTime endTime;
    private Region region;

    private RegionDao regionDao;
    public Region getRegion(){
        if (null==this.region && null!=this.regionDao){
            logger.debug("getregion: this.RegionId = {}", this.regionId);
            this.region=this.regionDao.findById(this.regionId);
        }
        return this.region;
    }
    private ShopLogistics shopLogistics;

    private ShopLogisticsDao shopLogisticsDao;

    public ShopLogistics getShopLogistics() {
        if (null == this.shopLogistics && null != this.shopLogisticsDao) {
            logger.debug("getshopLogistics this.shopLogisticsId = {}", this.shopLogisticsId);
            this.shopLogistics = this.shopLogisticsDao.findById(this.shopLogisticsId);
        }
        return this.shopLogistics;
    }

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public void setShopLogisticsDao(ShopLogisticsDao shopLogisticsDao) {
        this.shopLogisticsDao = shopLogisticsDao;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
