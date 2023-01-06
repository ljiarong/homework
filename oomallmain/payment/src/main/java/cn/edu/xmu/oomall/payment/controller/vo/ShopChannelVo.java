package cn.edu.xmu.oomall.payment.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 黄坤鹏
 * @date 2022/11/9 8:28
 */
@Data
@NoArgsConstructor
public class ShopChannelVo {

    @NotNull(message = "子商户号必填")
    private String subMchid;
}
