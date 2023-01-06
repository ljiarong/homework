package cn.edu.xmu.oomall.freight.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.edu.xmu.javaee.core.model.Constants.BEGIN_TIME;
import static cn.edu.xmu.javaee.core.model.Constants.END_TIME;
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeginAndEndTimeVo {
    @NotNull(message="开始时间不能为空")
    private LocalDateTime beginTime = BEGIN_TIME;

    @NotNull(message="结束时间不能为空")
    private LocalDateTime endTime = END_TIME;
}
