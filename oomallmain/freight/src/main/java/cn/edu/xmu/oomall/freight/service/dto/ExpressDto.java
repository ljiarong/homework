package cn.edu.xmu.oomall.freight.service.dto;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.Express;
import cn.edu.xmu.oomall.freight.service.dto.dao.bo.WarehouseRegion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(Express.class)
public class ExpressDto {
    private Long id;

    private String billCode;

    private Long shopLogisticsId;

    private Long senderRegionId;

    private String senderAddress;
    private Long deliveryRegionId;

    private String deliverAddress;

    private String senderName;

    private String senderMobile;

    private String deliveryName;

    private Byte status;

}
