package cn.edu.xmu.oomall.freight.service.dto.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.service.dto.dao.LogisticsDao;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.service.dto.CreatedByDto;
import cn.edu.xmu.oomall.freight.service.dto.ModifiedByDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@CopyFrom(ShopLogisticsPo.class)
public class ShopLogistics extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);

    private Long id;

    private Byte invalid;
    private String secret;

    private Integer priority;

    public static Byte VALID = 0;
    public static Byte INVALID = 1;

    private Long logisticsId;
    private CreatedByDto createdBy;
    private ModifiedByDto modifiedBy;

    public CreatedByDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByDto createdBy) {
        this.createdBy = createdBy;
    }

    public ModifiedByDto getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(ModifiedByDto modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Builder
    public ShopLogistics(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long id1, Byte invalid, Integer priority, Long logisticsId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.id = id1;
        this.invalid = invalid;
        this.priority = priority;
        this.logisticsId = logisticsId;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    private Logistics logistics;

    private LogisticsDao logisticsDao;

    public Logistics getLogistics() throws BusinessException {
        if (null == this.logistics && null != this.logisticsDao) {
            logger.debug("getLogistics: this.logisticsId = {}", this.logisticsId);
            this.logistics = this.logisticsDao.findById(this.logisticsId);
        }
        return this.logistics;
    }

    public void setLogisticsDao(LogisticsDao logisticsDao) {
        this.logisticsDao = logisticsDao;
    }



    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId) {
        this.logisticsId = logisticsId;
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
