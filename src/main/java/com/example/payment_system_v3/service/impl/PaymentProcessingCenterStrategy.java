package com.example.payment_system_v3.service.impl;

import com.example.payment_system_v3.entity.PaymentSystem;
import com.example.payment_system_v3.entity.attribute.PaymentSystemAtrbt;
import com.example.payment_system_v3.exception.PaymentSystemNotFoundException;
import com.example.payment_system_v3.repository.PaymentSystemRepository;
import com.example.payment_system_v3.service.ProcessingCenterStrategy;
import com.example.payment_system_v3.service.impl.MasterCardProcessingCenterStrategyImpl;
import com.example.payment_system_v3.service.impl.VisaProcessingCenterStrategyImpl;
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

    public ProcessingCenterStrategy choiceOfProcessingStrategies(Long paymentSystemId) throws PaymentSystemNotFoundException {
        Optional<PaymentSystem> paymentSystemOptional = paymentSystemRepository.getPaymentSystemById(paymentSystemId);
        if (paymentSystemOptional.isEmpty()){
            throw new PaymentSystemNotFoundException("Payment system not found!");
        }
        PaymentSystem paymentSystem = paymentSystemOptional.get();

        if (paymentSystem.getName().equals(PaymentSystemAtrbt.VISA)) {
            return this.visaProcessingCenterStrategy;
        } else if (paymentSystem.getName().equals(PaymentSystemAtrbt.MASTER_CARD)) {
            return this.masterCardProcessingCenterStrategy;
        } else {
            throw new IllegalArgumentException("Unsupported payment system");
        }
    }
}
