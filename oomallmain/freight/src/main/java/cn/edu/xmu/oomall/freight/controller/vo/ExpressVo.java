package cn.edu.xmu.oomall.freight.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpressVo {
    @NotNull(message = "商铺物流id不能为空")
    private Long shopLogisticId;
    Sender sender;
    Delivery delivery;

    @Data

    public static class Sender {
        private String name;
        private String mobile;
        private Long regionId;
        private String address;
    }

    @Data
    public static class Delivery{
        private String name;
        private String mobile;
        private Long regionId;
        private String address;
    }

}
