package cn.edu.xmu.oomall.freight.service.dto.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.service.dto.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.service.dto.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseLogisticsPo;
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
@CopyFrom(WarehouseLogisticsPo.class)
public class WarehouseLogistics extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseLogistics.class);

    public static Byte VALID = 0;

    public static Byte INVALID = 1;
    private Long id;


    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Byte invalid;

    private Long shopLogisticsId;

    private ShopLogistics shopLogistics;


    private ShopLogisticsDao shopLogisticsDao;
    public ShopLogistics getShopLogistics() {
        if (null == this.shopLogistics && null != this.shopLogisticsDao) {
            logger.debug("getShopLogistics: this.shopLogisticsId = {}", this.shopLogisticsId);
            this.shopLogistics = this.shopLogisticsDao.findById(this.shopLogisticsId);
        }
        return this.shopLogistics;
    }

    private Long warehouseId;

    private Warehouse warehouse;

    private WarehouseDao warehouseDao;

    public Warehouse getWarehouse() {
        if (null == this.warehouse && null != this.warehouseDao) {
            logger.debug("getWareouse: this.warehouseId = {}", this.warehouseId);
            this.warehouse = this.warehouseDao.findById(this.warehouseId);
        }
        return this.warehouse;
    }

    public void setShopLogisticsDao(ShopLogisticsDao shopLogisticsDao) {
        this.shopLogisticsDao = shopLogisticsDao;
    }

    public void setWarehouseDao(WarehouseDao warehouseDao) {
        this.warehouseDao = warehouseDao;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
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
