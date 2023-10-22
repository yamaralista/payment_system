package com.example.payment_system_v3.mapper;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.dto.CardResponseDto;
import com.example.payment_system_v3.entity.Card;

public class CardMapper {
    public static Card mapCardRequestDtoToEntity(CardRequestDto cardRequestDto){
        return Card.builder()
                .cardNumber(cardRequestDto.getCardNumber())
                .pinCode(cardRequestDto.getPinCode())
                .balance(cardRequestDto.getBalance())
                .build();
    }

    public static CardResponseDto mapToCardResponseDto(Card card){
        return CardResponseDto.builder()
                .id(card.getId())
                .createdAt(card.getCreatedAt())
                .cardNumber(card.getCardNumber())
                .clientId(card.getClient().getId())
                .expirationDate(card.getExpirationDate())
                .build();
    }
}
