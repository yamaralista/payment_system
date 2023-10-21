package com.example.payment_system_v3.dto;


import com.example.payment_system_v3.entity.attribute.PaymentState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentHistoryRequestDto {
    private LocalDateTime paymentDate;
    private Integer paymentAmount;
    private Long senderCardId;
    private Long receiverCardId;
    private PaymentState paymentState;
}
