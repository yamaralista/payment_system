package com.example.payment_system_v3.controller;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.PaymentSystemNotFoundException;
import com.example.payment_system_v3.service.impl.PaymentProcessingCenterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/card")
public class CardController {
    private final PaymentProcessingCenterStrategy paymentProcessingCenterStrategy;

    @Autowired
    public CardController(PaymentProcessingCenterStrategy paymentProcessingCenterStrategy) {
        this.paymentProcessingCenterStrategy = paymentProcessingCenterStrategy;
    }

    public ResponseEntity<String> issueCard(
            @RequestBody CardRequestDto cardRequestDto
            ) throws PaymentSystemNotFoundException,
            ClientNotFoundException,
            InvalidCredentialsException {
        try{
        paymentProcessingCenterStrategy.choiceOfProcessingStrategies(cardRequestDto.getPaymentSystemId()).issueCard(cardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Card issued successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Card issuance failed: " + e.getMessage());
        }
    }
}
