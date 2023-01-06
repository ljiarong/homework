package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDto {
    private Long id;
    private String orderSn;
    private SimpleCustomerDto customer;
    private SimpleShopDto shop;
    private Long pid;
    private Integer status;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long originPrice;
    private Long discountPrice;
    private Long expressFee;
    private String message;
    private ConsigneeDto consignee;
    private SimplePackageDto pack;
    private List<OrderItemDto> orderItems;
}
