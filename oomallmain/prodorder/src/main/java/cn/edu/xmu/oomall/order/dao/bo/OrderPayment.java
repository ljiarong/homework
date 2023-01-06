package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@NoArgsConstructor
public class OrderPayment extends OOMallObject {
    @Builder
    public OrderPayment(Long id, Long orderId, Long paymentId, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified){
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
    }
}
