package cn.edu.xmu.oomall.freight.service.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(WarehouseRegion.class)
@ToString
public class FullWarehouseDto extends OOMallObject implements Comparable<FullWarehouseDto> {
    private Warehouse warehouse;



    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private UserDto creator;
    private UserDto modifier;

    @Override
    public int compareTo(FullWarehouseDto o) {
        {
            FullWarehouseDto fullWarehouseDto = (FullWarehouseDto) o;
            return Double.compare(this.warehouse.getPriority(), fullWarehouseDto.warehouse.getPriority());

        }
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public UserDto getModifier() {
        return modifier;
    }

    public void setModifier(UserDto modifier) {
        this.modifier = modifier;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
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
