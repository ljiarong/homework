package cn.edu.xmu.oomall.freight.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopLogisticsVo {
    @NotNull(message="物流Id不能为空")
    private Long logisticsId;
    @NotNull(message="密钥不能为空")
    private String secret;
    @NotNull(message="优先级不能为空")
    private Integer priority;
}
