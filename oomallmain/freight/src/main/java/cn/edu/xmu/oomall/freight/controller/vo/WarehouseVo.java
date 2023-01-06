package cn.edu.xmu.oomall.freight.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class WarehouseVo {
    @NotNull(message="仓库名字不能为空")
    private String name;
    @NotNull(message="仓库地址不能为空")
    private String address;
    @NotNull(message="地区Id不能为空")
    private Long regionId;
    @NotNull(message="联系电话不能为空")
    private String senderMobile;
    @NotNull(message="联系人不能为空")
    private String senderName;
    @NotNull(message="优先级不能为空")
    private Integer priority;

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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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
}
