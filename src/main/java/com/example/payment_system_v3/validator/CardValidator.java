package com.example.payment_system_v3.validator;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CardValidator {
    public void validateCardRequestDto(CardRequestDto cardRequestDto) throws IllegalArgumentException, InvalidCredentialsException {
        if(Objects.isNull(cardRequestDto)) {
            throw new IllegalArgumentException("The created card cannot be null!");
        }
        if (Objects.isNull(cardRequestDto.getPinCode()) || cardRequestDto.getPinCode().isEmpty()){
            throw new InvalidCredentialsException("The PIN code of the card can be null or empty!");
        }
    }
}
