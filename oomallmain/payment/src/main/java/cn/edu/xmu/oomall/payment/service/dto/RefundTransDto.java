package cn.edu.xmu.oomall.payment.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundTransDto {
    private Long id;
    private String outNo;
    private String transNo;
    private Long amount;
    private LocalDateTime successTime;
    private SimpleChannelDto channel;
    private Byte status;
    private String userReceiveAccount;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long modifierId;
    private String modifierName;
    private Long adjustorId;
    private String adjustorName;
    private String adjustTime;
}
