package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CardResponseDto {
    private Long id;
    private String cardNumber;
    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
    private Long paymentSystemId;
    private Long clientId;
}
