package com.example.payment_system_v3.dto;


import com.example.payment_system_v3.entity.Client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardRequestDto {
    private String cardNumber;
    private Client client;
    private Long paymentSystem;
}
