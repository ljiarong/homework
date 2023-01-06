package cn.edu.xmu.oomall.order.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderModifyVo {
    private String consignee;
    private Long regionId;
    private String adress;
    private String mobile;
}
