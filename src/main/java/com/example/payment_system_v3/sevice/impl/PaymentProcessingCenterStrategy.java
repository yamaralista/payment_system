package com.example.payment_system_v3.sevice.impl;

import com.example.payment_system_v3.entity.PaymentSystem;
import com.example.payment_system_v3.entity.attribute.PaymentSystemAtrbt;
import com.example.payment_system_v3.exception.PaymentSystemNotFoundException;
import com.example.payment_system_v3.repository.PaymentSystemRepository;
import com.example.payment_system_v3.sevice.ProcessingCenterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Этот сервис является стратегическим компонентом системы обработки платежей. Он отвечает за выбор соответствующей стратегии
 * обработки для конкретной платежной системы (например, Visa, MasterCard и и тюд).
 */
@Service
public class PaymentProcessingCenterStrategy {
    private final VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy;
    private final MasterCardProcessingCenterStrategyImpl masterCardProcessingCenterStrategy;
    private final PaymentSystemRepository paymentSystemRepository;

    /**
     * Конструктор класса для внедрения зависимостей через Spring.
     *
     * @param visaProcessingCenterStrategy      Стратегия обработки для платежной системы Visa
     * @param masterCardProcessingCenterStrategy Стратегия обработки для платежной системы MasterCard
     * @param paymentSystemRepository            Репозиторий для работы с данными платежных систем
     */
    @Autowired
    public PaymentProcessingCenterStrategy(VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy, MasterCardProcessingCenterStrategyImpl masterCardProcessingCenterStrategy, PaymentSystemRepository paymentSystemRepository) {
        this.visaProcessingCenterStrategy = visaProcessingCenterStrategy;
        this.masterCardProcessingCenterStrategy = masterCardProcessingCenterStrategy;
        this.paymentSystemRepository = paymentSystemRepository;
    }

    /**
     * Метод выбора стратегии обработки платежа на основе платежной системы.
     *
     * @param paymentSystem Платежная система, для которой нужно выбрать стратегию обработки
     * @return Соответствующая стратегия обработки для данной платежной системы
     * @throws PaymentSystemNotFoundException Исключение, если платежная система не найдена
     * @throws IllegalArgumentException         Исключение, если платежная система не поддерживается
     */
    public ProcessingCenterStrategy choiceOfProcessingStrategies(PaymentSystem paymentSystem) throws PaymentSystemNotFoundException, IllegalArgumentException {
        Optional<PaymentSystem> paymentSystemOptional = paymentSystemRepository.getPaymentSystemById(paymentSystem.getId());
        if (paymentSystemOptional.isEmpty()){
            throw new PaymentSystemNotFoundException("Payment system not found!");
        }
        PaymentSystem paymentSystemCurrent = paymentSystemOptional.get();

        if (paymentSystemCurrent.getName().equals(PaymentSystemAtrbt.VISA)) {
            return visaProcessingCenterStrategy;
        } else if (paymentSystemCurrent.getName().equals(PaymentSystemAtrbt.MASTER_CARD)) {
            return masterCardProcessingCenterStrategy;
        } else {
            throw new IllegalArgumentException("Unsupported payment system");
        }
    }
}
