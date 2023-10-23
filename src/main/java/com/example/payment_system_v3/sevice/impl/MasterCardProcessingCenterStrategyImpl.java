package com.example.payment_system_v3.sevice.impl;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.entity.Client;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;
import com.example.payment_system_v3.mapper.CardMapper;
import com.example.payment_system_v3.repository.CardRepository;
import com.example.payment_system_v3.repository.ClientRepository;
import com.example.payment_system_v3.sevice.ProcessingCenterStrategy;
import com.example.payment_system_v3.validator.CardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

/**
 * Этот класс реализует стратегию обработки для платежных карт MasterCard.
 * Стратегия включает в себя выпуск карт, пополнение и списание средств.
 */

@Service
public class MasterCardProcessingCenterStrategyImpl implements ProcessingCenterStrategy {
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardValidator cardValidator;
    private final ClientRepository clientRepository;

    /**
     * Конструктор класса для внедрения зависимостей через Spring.
     *
     * @param cardRepository     Репозиторий для работы с данными карт
     * @param passwordEncoder    Кодировщик паролей
     * @param cardValidator      Валидатор карт
     * @param clientRepository   Репозиторий для работы с данными клиентов
     */
    @Autowired
    public MasterCardProcessingCenterStrategyImpl(CardRepository cardRepository, PasswordEncoder passwordEncoder,
                                                  CardValidator cardValidator, ClientRepository clientRepository) {
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardValidator = cardValidator;
        this.clientRepository = clientRepository;
    }


    /**
     * Метод выпускает новую карту MasterCard на основе данных из DTO.
     *
     * @param cardRequestDto Информация о новой карте, переданная через DTO
     * @throws InvalidCredentialsException Исключение, если данные карты недействительны
     * @throws ClientNotFoundException    Исключение, если клиент не найден
     */
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
        return "5" + generateNumber(15);
    }

    /**
     * Генерирует уникальный номер карты MasterCard.
     *
     * @return Уникальный номер карты
     */
    private String generateNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Метод для пополнения баланса карты MasterCard.
     *
     * @param card   Карта, на которую производится пополнение
     * @param amount Сумма для пополнения
     * @throws InvalidPinException    Исключение, если введен неверный пин-код
     * @throws CardNotFoundException   Исключение, если карта не найдена
     * @throws IllegalArgumentException Исключение, если сумма недействительна
     */
    @Override
    public void topUpCard(Card card, BigDecimal  amount) throws InvalidPinException, CardNotFoundException {
        Optional<Card> cardOptional = cardRepository.getCardByCardNumber(card.getCardNumber());
        if (cardOptional.isEmpty()){
            throw new CardNotFoundException("There is no such card");
        }
        Card cardCurrent = cardOptional.get();
        BigDecimal currentBalance = cardCurrent.getBalance();

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Invalid amount");
        }

        BigDecimal newBalance = currentBalance.add(amount);
        card.setBalance(newBalance);

        cardRepository.save(cardCurrent);
    }

    /**
     * Метод для списания средств с карты MasterCard.
     *
     * @param card   Карта, с которой производится списание
     * @param amount Сумма для списания
     * @throws InvalidPinException    Исключение, если введен неверный пин-код
     * @throws CardNotFoundException   Исключение, если карта не найдена
     */
    @Override
    public void debitCard(Card card, BigDecimal amount) throws InvalidPinException, CardNotFoundException {

        Optional<Card> cardOptional = cardRepository.getCardByCardNumber(card.getCardNumber());
        if (cardOptional.isEmpty()){
            throw new CardNotFoundException("There is no such card");
        }
        Card cardCurrent = cardOptional.get();

        BigDecimal currentBalance = cardCurrent.getBalance();

        BigDecimal newBalance = currentBalance.subtract(amount);
        card.setBalance(newBalance);

        cardRepository.save(cardCurrent);
    }


}
