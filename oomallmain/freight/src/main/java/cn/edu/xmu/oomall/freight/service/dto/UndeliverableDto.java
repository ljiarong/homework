package cn.edu.xmu.oomall.freight.service.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(Undeliverable.class)
public class UndeliverableDto {
    private Long id;
    private Region region;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private UserDto creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private UserDto modifier;
}
