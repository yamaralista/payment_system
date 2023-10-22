package com.example.payment_system_v3.service;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;

import java.math.BigDecimal;

public interface ProcessingCenterStrategy {

    void issueCard(CardRequestDto cardRequestDto) throws InvalidCredentialsException, ClientNotFoundException;

    void topUpCard(CardRequestDto cardRequestDto, BigDecimal amount) throws InvalidPinException, CardNotFoundException;

    void debitCard(CardRequestDto cardRequestDto, BigDecimal amount) throws InvalidPinException, CardNotFoundException;
}
