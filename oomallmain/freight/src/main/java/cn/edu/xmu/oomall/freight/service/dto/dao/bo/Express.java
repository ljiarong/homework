package cn.edu.xmu.oomall.freight.service.dto.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.service.dto.dao.RegionDao;
import cn.edu.xmu.oomall.freight.service.dto.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ExpressPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(ExpressPo.class)
public class Express extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Express.class);

    private Long id;


    private String billCode;


    private Long shopLogisticsId;

    private Long senderRegionId;

    private String senderAddress;
    private Long deliveryRegionId;

    private String deliverAddress;

    private String senderName;

    private String senderMobile;

    private String deliveryName;

    private Byte status;

    private Region region;

    private RegionDao regionDao;

    public Region getRegion() {
        if (null == this.region && null != this.regionDao) {
            logger.debug("getRegion: this.deliveryRegionId = {}", this.deliveryRegionId);
            this.region = this.regionDao.findById(this.deliveryRegionId);
        }
        return this.region;
    }

    private ShopLogistics shopLogistics;

    private ShopLogisticsDao shopLogisticsDao;

    public ShopLogistics getShopLogistics() {
        if (null == this.shopLogistics && null != this.shopLogisticsDao) {
            logger.debug("getshopLogistics: this.shopLogisticsId = {}", this.shopLogisticsId);
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

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
    }

    public Long getSenderRegionId() {
        return senderRegionId;
    }

    public void setSenderRegionId(Long senderRegionId) {
        this.senderRegionId = senderRegionId;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Long getDeliveryRegionId() {
        return deliveryRegionId;
    }

    public void setDeliveryRegionId(Long deliveryRegionId) {
        this.deliveryRegionId = deliveryRegionId;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

