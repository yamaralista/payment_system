package com.example.payment_system_v3.sevice.impl;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.entity.Client;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;
import com.example.payment_system_v3.repository.CardRepository;
import com.example.payment_system_v3.repository.ClientRepository;
import com.example.payment_system_v3.validator.CardValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class VisaProcessingCenterStrategyImplTest {
    @InjectMocks
    private VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CardValidator cardValidator;

    @Mock
    private ClientRepository clientRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIssueCard() throws InvalidCredentialsException, ClientNotFoundException {
        CardRequestDto cardRequestDto = new CardRequestDto();
        cardRequestDto.setClientId(1L);

        when(clientRepository.getClientById(anyLong())).thenReturn(Optional.of(new Client()));
        when(cardRepository.save(any()))
                .thenAnswer((Answer<Card>) invocation -> {
                    Card card = invocation.getArgument(0);
                    return card;
                });


        visaProcessingCenterStrategy.issueCard(cardRequestDto);

        verify(cardValidator).validateCardRequestDto(cardRequestDto);
        verify(clientRepository).getClientById(anyLong());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    public void testTopUpCard() throws InvalidPinException, CardNotFoundException {

        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setBalance(new BigDecimal("100.00"));

        when(cardRepository.getCardByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal amount = new BigDecimal("50.00");


        visaProcessingCenterStrategy.topUpCard(card, amount);


        verify(cardRepository).getCardByCardNumber(card.getCardNumber());
        verify(cardRepository).save(card);
    }

    @Test
    public void testDebitCard() throws InvalidPinException, CardNotFoundException {

        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setBalance(new BigDecimal("100.00"));

        when(cardRepository.getCardByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal amount = new BigDecimal("50.00");


        visaProcessingCenterStrategy.debitCard(card, amount);


        verify(cardRepository).getCardByCardNumber(card.getCardNumber());
        verify(cardRepository).save(card);
    }

    @Test
    public void testDebitCardWithInsufficientBalance() {

        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setBalance(new BigDecimal("50.00"));

        when(cardRepository.getCardByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

        BigDecimal amount = new BigDecimal("100.00");


        assertThrows(IllegalArgumentException.class, () -> visaProcessingCenterStrategy.debitCard(card, amount));
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    public void testDebitCardWithNonExistentCard() {

        Card card = new Card();
        card.setCardNumber("1234567890123456");

        when(cardRepository.getCardByCardNumber(card.getCardNumber())).thenReturn(Optional.empty());

        BigDecimal amount = new BigDecimal("50.00");


        assertThrows(CardNotFoundException.class, () -> visaProcessingCenterStrategy.debitCard(card, amount));
        verify(cardRepository, never()).save(any(Card.class));
    }
}