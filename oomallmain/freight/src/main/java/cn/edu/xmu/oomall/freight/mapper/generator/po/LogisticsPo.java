package cn.edu.xmu.oomall.freight.mapper.generator.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
@CopyFrom(Logistics.class)
@ToString
public class LogisticsPo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.app_id
     *
     * @mbggenerated
     */
    private String appId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.creator_id
     *
     * @mbggenerated
     */
    private Long creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.creator_name
     *
     * @mbggenerated
     */
    private String creatorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.modifier_id
     *
     * @mbggenerated
     */
    private Long modifierId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.modifier_name
     *
     * @mbggenerated
     */
    private String modifierName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.gmt_create
     *
     * @mbggenerated
     */
    private LocalDateTime gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.gmt_modified
     *
     * @mbggenerated
     */
    private LocalDateTime gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column freight_logistics.sn_pattern
     *
     * @mbggenerated
     */
    private String snPattern;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.id
     *
     * @return the value of freight_logistics.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.id
     *
     * @param id the value for freight_logistics.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.name
     *
     * @return the value of freight_logistics.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.name
     *
     * @param name the value for freight_logistics.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.app_id
     *
     * @return the value of freight_logistics.app_id
     *
     * @mbggenerated
     */
    public String getAppId() {
        return appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.app_id
     *
     * @param appId the value for freight_logistics.app_id
     *
     * @mbggenerated
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.creator_id
     *
     * @return the value of freight_logistics.creator_id
     *
     * @mbggenerated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.creator_id
     *
     * @param creatorId the value for freight_logistics.creator_id
     *
     * @mbggenerated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.creator_name
     *
     * @return the value of freight_logistics.creator_name
     *
     * @mbggenerated
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.creator_name
     *
     * @param creatorName the value for freight_logistics.creator_name
     *
     * @mbggenerated
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.modifier_id
     *
     * @return the value of freight_logistics.modifier_id
     *
     * @mbggenerated
     */
    public Long getModifierId() {
        return modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.modifier_id
     *
     * @param modifierId the value for freight_logistics.modifier_id
     *
     * @mbggenerated
     */
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.modifier_name
     *
     * @return the value of freight_logistics.modifier_name
     *
     * @mbggenerated
     */
    public String getModifierName() {
        return modifierName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.modifier_name
     *
     * @param modifierName the value for freight_logistics.modifier_name
     *
     * @mbggenerated
     */
    public void setModifierName(String modifierName) {
        this.modifierName = modifierName == null ? null : modifierName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.gmt_create
     *
     * @return the value of freight_logistics.gmt_create
     *
     * @mbggenerated
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.gmt_create
     *
     * @param gmtCreate the value for freight_logistics.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.gmt_modified
     *
     * @return the value of freight_logistics.gmt_modified
     *
     * @mbggenerated
     */
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.gmt_modified
     *
     * @param gmtModified the value for freight_logistics.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column freight_logistics.sn_pattern
     *
     * @return the value of freight_logistics.sn_pattern
     *
     * @mbggenerated
     */
    public String getSnPattern() {
        return snPattern;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column freight_logistics.sn_pattern
     *
     * @param snPattern the value for freight_logistics.sn_pattern
     *
     * @mbggenerated
     */
    public void setSnPattern(String snPattern) {
        this.snPattern = snPattern == null ? null : snPattern.trim();
    }
}