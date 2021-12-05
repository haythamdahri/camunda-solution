package com.microservices.fr.tech.mocks.producers;

import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.services.PaymentQualifierService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class PaymentProcessMock {

    private final PaymentQualifierService paymentQualifierService;
    private static final PaymentRequest PAYMENT_REQUEST;

    static {
        PAYMENT_REQUEST = PaymentRequest.builder()
                .amount(BigDecimal.valueOf(5896.99D))
                .userIdentifier("850")
                .currency("EUR")
                .executionDateTime(LocalDateTime.now(ZoneId.systemDefault()))
                .build();
    }

    public PaymentProcessMock(PaymentQualifierService paymentQualifierService) {
        this.paymentQualifierService = paymentQualifierService;
    }

    //@Scheduled(fixedDelayString = "${com.cron.create-payments.fixed-delay}")
    public void executePaymentPeriodically() {
        this.paymentQualifierService.qualifyPayment(PAYMENT_REQUEST);
    }

}
