package com.example.payment_system_v3.dto;


import com.example.payment_system_v3.entity.Client;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
public class CardRequestDto {

    private String cardNumber;
    private Long clientId;
    private Long paymentSystemId;
    @NotBlank(message = "The pin-code can't be empty")
    @Size(min = 4, message = "The pin-code must have at least 4 characters")
    private String pinCode;
    private BigDecimal balance;
}
