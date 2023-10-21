package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSystemRequestDto {
    private String name;
}
