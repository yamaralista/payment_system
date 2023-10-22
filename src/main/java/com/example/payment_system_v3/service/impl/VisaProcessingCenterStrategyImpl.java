package com.example.payment_system_v3.service.impl;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Client;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;
import com.example.payment_system_v3.mapper.CardMapper;
import com.example.payment_system_v3.repository.CardRepository;
import com.example.payment_system_v3.repository.ClientRepository;
import com.example.payment_system_v3.service.ProcessingCenterStrategy;
import com.example.payment_system_v3.validator.CardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class VisaProcessingCenterStrategyImpl implements ProcessingCenterStrategy {
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardValidator cardValidator;
    private final ClientRepository clientRepository;

    @Autowired
    public VisaProcessingCenterStrategyImpl(CardRepository cardRepository, PasswordEncoder passwordEncoder, CardValidator cardValidator, ClientRepository clientRepository) {
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardValidator = cardValidator;
        this.clientRepository = clientRepository;
    }
    @Override
    public void issueCard(CardRequestDto cardRequestDto) throws InvalidCredentialsException, ClientNotFoundException {
        this.cardValidator.validateCardRequestDto(cardRequestDto);
        Optional<Client> clientOptional =
                this.clientRepository.getClientById(cardRequestDto.getClientId());
                if (clientOptional.isEmpty()){
                    throw new ClientNotFoundException("Such a user does not exist");
                }
                Client client = clientOptional.get();

        Card card =
                CardMapper.mapCardRequestDtoToEntity(cardRequestDto);
        card.setCardNumber(generateUniqueCardNumber());

        card.setPinCode(passwordEncoder.encode(cardRequestDto.getPinCode()));
        card.setClient(client);

        cardRepository.save(card);
    }
    private String generateUniqueCardNumber() {
        return "4" + generateNumber(15);
    }

    private String generateNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public void topUpCard(CardRequestDto cardRequestDto, BigDecimal  amount) throws InvalidPinException, CardNotFoundException {
        Optional<Card> cardOptional = cardRepository.getCardByCardNumber(cardRequestDto.getCardNumber());
        if (cardOptional.isEmpty()){
            throw new CardNotFoundException("There is no such card");
        }
        Card card = cardOptional.get();
        BigDecimal currentBalance = card.getBalance();

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Invalid amount");
        }

        BigDecimal newBalance = currentBalance.add(amount);
        card.setBalance(newBalance);

        cardRepository.save(card);
    }

    @Override
    public void debitCard(CardRequestDto cardRequestDto, BigDecimal amount) throws InvalidPinException, CardNotFoundException {
        String pinCode = cardRequestDto.getPinCode();
        Optional<Card> cardOptional = cardRepository.getCardByCardNumber(cardRequestDto.getCardNumber());
        if (cardOptional.isEmpty()){
            throw new CardNotFoundException("There is no such card");
        }
        Card card = cardOptional.get();
        if (!this.passwordEncoder.matches(pinCode,card.getPinCode())) {
            throw new InvalidPinException("Invalid PIN code");
        }
        BigDecimal currentBalance = card.getBalance();

        BigDecimal newBalance = currentBalance.subtract(amount);
        card.setBalance(newBalance);

        cardRepository.save(card);
    }


}
