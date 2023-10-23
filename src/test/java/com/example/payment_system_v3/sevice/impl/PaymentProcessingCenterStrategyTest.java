package com.example.payment_system_v3.sevice.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.payment_system_v3.entity.PaymentSystem;
import com.example.payment_system_v3.entity.attribute.PaymentSystemAtrbt;
import com.example.payment_system_v3.exception.PaymentSystemNotFoundException;
import com.example.payment_system_v3.repository.PaymentSystemRepository;
import com.example.payment_system_v3.sevice.ProcessingCenterStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
class PaymentProcessingCenterStrategyTest {
    @InjectMocks
    private PaymentProcessingCenterStrategy paymentProcessingCenterStrategy;

    @Mock
    private VisaProcessingCenterStrategyImpl visaProcessingCenterStrategy;

    @Mock
    private MasterCardProcessingCenterStrategyImpl masterCardProcessingCenterStrategy;

    @Mock
    private PaymentSystemRepository paymentSystemRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChoiceOfProcessingStrategiesForVisa() throws PaymentSystemNotFoundException {
        PaymentSystem visaPaymentSystem = new PaymentSystem();
        visaPaymentSystem.setId(1L);
        visaPaymentSystem.setName(PaymentSystemAtrbt.VISA);

        when(paymentSystemRepository.getPaymentSystemById(1L)).thenReturn(Optional.of(visaPaymentSystem));

        ProcessingCenterStrategy strategy = paymentProcessingCenterStrategy.choiceOfProcessingStrategies(visaPaymentSystem);

        assertNotNull(strategy);
        assertTrue(strategy instanceof VisaProcessingCenterStrategyImpl);

        verify(paymentSystemRepository).getPaymentSystemById(1L);
    }

    @Test
    public void testChoiceOfProcessingStrategiesForMasterCard() throws PaymentSystemNotFoundException {
        PaymentSystem masterCardPaymentSystem = new PaymentSystem();
        masterCardPaymentSystem.setId(2L);
        masterCardPaymentSystem.setName(PaymentSystemAtrbt.MASTER_CARD);

        when(paymentSystemRepository.getPaymentSystemById(2L)).thenReturn(Optional.of(masterCardPaymentSystem));

        ProcessingCenterStrategy strategy = paymentProcessingCenterStrategy.choiceOfProcessingStrategies(masterCardPaymentSystem);

        assertNotNull(strategy);
        assertTrue(strategy instanceof MasterCardProcessingCenterStrategyImpl);

        verify(paymentSystemRepository).getPaymentSystemById(2L);
    }

    @Test
    public void testChoiceOfProcessingStrategiesForUnsupportedPaymentSystem() {
        PaymentSystem unsupportedPaymentSystem = new PaymentSystem();
        unsupportedPaymentSystem.setId(3L);
        unsupportedPaymentSystem.setName(PaymentSystemAtrbt.VISA);

        when(paymentSystemRepository.getPaymentSystemById(3L)).thenReturn(Optional.of(unsupportedPaymentSystem));

        assertThrows(IllegalArgumentException.class, () -> {
            paymentProcessingCenterStrategy.choiceOfProcessingStrategies(unsupportedPaymentSystem);
        });

        verify(paymentSystemRepository).getPaymentSystemById(3L);
    }

    @Test
    public void testChoiceOfProcessingStrategiesForMissingPaymentSystem() {
        when(paymentSystemRepository.getPaymentSystemById(4L)).thenReturn(Optional.empty());

        assertThrows(PaymentSystemNotFoundException.class, () -> {
            paymentProcessingCenterStrategy.choiceOfProcessingStrategies(new PaymentSystem(4L, PaymentSystemAtrbt.VISA));
        });

        verify(paymentSystemRepository).getPaymentSystemById(4L);
    }
}
