package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<CardResponseDto> cards;
}
