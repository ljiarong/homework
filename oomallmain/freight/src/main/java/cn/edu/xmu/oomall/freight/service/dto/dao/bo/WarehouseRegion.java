package cn.edu.xmu.oomall.freight.service.dto.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.service.dto.dao.RegionDao;
import cn.edu.xmu.oomall.freight.service.dto.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseRegionPo;
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
@CopyFrom({WarehouseRegionPo.class})
public class WarehouseRegion extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseRegion.class);

    private Long id;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long regionId;

    private Long warehouseId;
    private Region region;
    public Region getRegion() {
        if (null == this.region && null != this.regionDao) {
            logger.debug("getRegion: this.regionId = {}", this.regionId);
            this.region = this.regionDao.findById(this.regionId);
        }
        return this.region;
    }
    private RegionDao regionDao;
    private Warehouse warehouse;
    public Warehouse getWarehouse() {
        if (null == this.warehouse && null != this.warehouseDao) {
            logger.debug("getWareouse: this.warehouseId = {}", this.warehouseId);
            this.warehouse = this.warehouseDao.findById(this.warehouseId);
        }
        return this.warehouse;
    }

    private WarehouseDao warehouseDao;

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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
