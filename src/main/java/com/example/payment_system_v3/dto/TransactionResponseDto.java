package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDto {
    private Long id;
    private Integer paymentAmount;
    private CardResponseDto senderCard;
    private CardResponseDto receiverCard;
}
