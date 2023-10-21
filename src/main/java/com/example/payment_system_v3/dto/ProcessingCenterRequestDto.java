package com.example.payment_system_v3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessingCenterRequestDto {
    private String name;
    private String address;
    private String contactInfo;
    private String country;

}
