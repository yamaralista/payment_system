package com.example.payment_system_v3.dto;

import com.example.payment_system_v3.entity.attribute.PaymentState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentHistoryResponseDto {
    private Long id;
    private LocalDateTime paymentDate;
    private Integer paymentAmount;
    private CardResponseDto senderCard;
    private CardResponseDto receiverCard;
    private PaymentState paymentState;
}
