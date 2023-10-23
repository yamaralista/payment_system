package com.example.payment_system_v3.controller;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.entity.PaymentSystem;
import com.example.payment_system_v3.exception.*;
import com.example.payment_system_v3.repository.CardRepository;
import com.example.payment_system_v3.repository.PaymentSystemRepository;
import com.example.payment_system_v3.sevice.impl.PaymentProcessingCenterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/card")
public class CardController {
    private final PaymentProcessingCenterStrategy paymentProcessingCenterStrategy;
    private final CardRepository cardRepository;
    private final PaymentSystemRepository paymentSystemRepository;

    @Autowired
    public CardController(PaymentProcessingCenterStrategy paymentProcessingCenterStrategy, CardRepository cardRepository, PaymentSystemRepository paymentSystemRepository) {
        this.paymentProcessingCenterStrategy = paymentProcessingCenterStrategy;
        this.cardRepository = cardRepository;
        this.paymentSystemRepository = paymentSystemRepository;
    }

    @PostMapping("/issue/{payment_system_id}")
    public ResponseEntity<String> issueCard(
            @PathVariable Long paymentSystemId,
            @RequestBody CardRequestDto cardRequestDto
            ) throws PaymentSystemNotFoundException,
            ClientNotFoundException,
            InvalidCredentialsException {
        Optional<PaymentSystem> paymentSystemOptional = paymentSystemRepository.getPaymentSystemById(paymentSystemId);
        if (paymentSystemOptional.isPresent()){
        PaymentSystem paymentSystem = paymentSystemOptional.get();
        paymentProcessingCenterStrategy.choiceOfProcessingStrategies(paymentSystem).issueCard(cardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Card issued successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card issue failed");
    }

    @PostMapping("/top_up_card/{cardNumber}")
    public ResponseEntity<String> topUpCard(@PathVariable String cardNumber,
                                            @RequestBody BigDecimal amount) throws InvalidPinException, CardNotFoundException, PaymentSystemNotFoundException {
        Optional<Card> cardOptional = this.cardRepository.getCardByCardNumber(cardNumber);
        if (cardOptional.isPresent()) {
        Card card = cardOptional.get();

        paymentProcessingCenterStrategy.choiceOfProcessingStrategies(card.getPaymentSystem()).topUpCard(card, amount);
        return ResponseEntity.ok("Card topped up successfully");
    }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
    }
    @PostMapping ("/debit/{cardId}")
    public ResponseEntity<String> debitCard(@PathVariable Long cardId, @RequestBody BigDecimal amount) throws PaymentSystemNotFoundException, InvalidPinException, CardNotFoundException {
        Optional<Card> cardOptional = cardRepository.getCardById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            paymentProcessingCenterStrategy.choiceOfProcessingStrategies(card.getPaymentSystem()).debitCard(card, amount);
            cardRepository.save(card);
            return ResponseEntity.ok("Payment processed successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
    }

}



