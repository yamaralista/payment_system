package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSystemResponseDto {
    private Long id;
    private String name;
}
