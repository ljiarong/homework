package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.dao.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({WarehousePo.class, WarehouseVo.class})
public class Warehouse extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Warehouse.class);

    private Long id;

    private String name;

    private String address;

    private String senderMobile;

    private String senderName;

    private Integer priority;

    private Byte invalid;


    public static Byte VALID = 0;

    public static Byte INVALID = 1;



    private Region region;

    private Long RegionId;

    private RegionDao regionDao;

    /**
     * 新建无状态
     */
    @Builder
    public Warehouse(Long id, String name, String address, String senderMobile, String senderName, Integer priority, Byte invalid) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.senderMobile = senderMobile;
        this.senderName = senderName;
        this.priority = priority;
        this.invalid = invalid;
    }

    public Region getRegion() {
        if (null == this.region && null != this.regionDao) {
            logger.debug("getregion: this.RegionId = {}", this.RegionId);
            this.region = this.regionDao.findById(this.RegionId);
        }
        return this.region;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public Long getRegionId() {
        return RegionId;
    }

    public void setRegionId(Long regionId) {
        RegionId = regionId;
    }

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
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
