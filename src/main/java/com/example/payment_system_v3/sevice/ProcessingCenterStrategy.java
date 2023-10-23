package com.example.payment_system_v3.sevice;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ProcessingCenterStrategy {

    void issueCard(CardRequestDto cardRequestDto) throws InvalidCredentialsException, ClientNotFoundException;

    @Transactional
    void topUpCard(Card card, BigDecimal amount) throws InvalidPinException, CardNotFoundException;

    @Transactional
    void debitCard(Card card, BigDecimal amount) throws InvalidPinException, CardNotFoundException;
}
