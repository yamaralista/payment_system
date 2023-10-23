package com.example.payment_system_v3.sevice;

import com.example.payment_system_v3.dto.CardRequestDto;
import com.example.payment_system_v3.entity.Card;
import com.example.payment_system_v3.exception.CardNotFoundException;
import com.example.payment_system_v3.exception.ClientNotFoundException;
import com.example.payment_system_v3.exception.InvalidCredentialsException;
import com.example.payment_system_v3.exception.InvalidPinException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Этот интерфейс определяет контракт для стратегий обработки платежных операций, таких как выпуск карты, пополнение счета
 * и списание средств с карты.
 */
public interface ProcessingCenterStrategy {
    /**
     * Метод выпуска карты на основе данных из CardRequestDto.
     *
     * @param cardRequestDto Данные для выпуска карты
     * @throws InvalidCredentialsException Исключение, если данные карты недопустимы
     * @throws ClientNotFoundException     Исключение, если клиент не найден
     */
    void issueCard(CardRequestDto cardRequestDto) throws InvalidCredentialsException, ClientNotFoundException;

    /**
     * Метод для пополнения счета карты на указанную сумму.
     *
     * @param card   Карта, на которую производится пополнение
     * @param amount Сумма пополнения
     * @throws InvalidPinException   Исключение, если введен неверный PIN-код
     * @throws CardNotFoundException Исключение, если карта не найдена
     */
    @Transactional
    void topUpCard(Card card, BigDecimal amount) throws InvalidPinException, CardNotFoundException;

    /**
     * Метод для списания средств с карты на указанную сумму.
     *
     * @param card   Карта, с которой списываются средства
     * @param amount Сумма списания
     * @throws InvalidPinException   Исключение, если введен неверный PIN-код
     * @throws CardNotFoundException Исключение, если карта не найдена
     */
    @Transactional
    void debitCard(Card card, BigDecimal amount) throws InvalidPinException, CardNotFoundException;
}
