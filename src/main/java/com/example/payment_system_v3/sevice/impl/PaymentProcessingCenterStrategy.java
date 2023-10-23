package com.example.payment_system_v3.sevice.impl;

import com.example.payment_system_v3.entity.PaymentSystem;
import com.example.payment_system_v3.entity.attribute.PaymentSystemAtrbt;
import com.example.payment_system_v3.exception.PaymentSystemNotFoundException;
import com.example.payment_system_v3.repository.PaymentSystemRepository;
import com.example.payment_system_v3.sevice.ProcessingCenterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentProcessingCenterStrategy {
    private final VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy;
    private final MasterCardProcessingCenterStrategyImpl masterCardProcessingCenterStrategy;
    private final PaymentSystemRepository paymentSystemRepository;

    @Autowired
    public PaymentProcessingCenterStrategy(VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy, MasterCardProcessingCenterStrategyImpl masterCardProcessingCenterStrategy, PaymentSystemRepository paymentSystemRepository) {
        this.visaProcessingCenterStrategy = visaProcessingCenterStrategy;
        this.masterCardProcessingCenterStrategy = masterCardProcessingCenterStrategy;
        this.paymentSystemRepository = paymentSystemRepository;
    }

    public ProcessingCenterStrategy choiceOfProcessingStrategies(PaymentSystem paymentSystem) throws PaymentSystemNotFoundException {
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
