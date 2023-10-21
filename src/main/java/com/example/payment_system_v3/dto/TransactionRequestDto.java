package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionRequestDto {
    private LocalDateTime createdAt;
    private Integer paymentAmount;
    private Long senderCardId;
    private Long receiverCardId;
}
