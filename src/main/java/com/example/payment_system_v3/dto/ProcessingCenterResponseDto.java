package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProcessingCenterResponseDto {
    private Long id;
    private String name;
    private String address;
    private String contactInfo;
    private String country;
    private List<CardResponseDto> cards;
}
