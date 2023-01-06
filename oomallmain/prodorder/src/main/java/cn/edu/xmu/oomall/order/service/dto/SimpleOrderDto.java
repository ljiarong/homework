package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SimpleOrderDto {
    private Long id;
    private Integer status;
    private LocalDateTime gmtCreat;
    private Long originPrice;
    private Long discountPrice;
    private Long expressFee;

    public SimpleOrderDto() {

    }
}
