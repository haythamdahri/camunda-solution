package com.microservices.fr.tech.controller;

import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.services.PaymentQualifierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/payments")
public class PaymentController {

    private final PaymentQualifierService paymentQualifierService;

    public PaymentController(
            PaymentQualifierService paymentQualifierService
    ) {
        this.paymentQualifierService = paymentQualifierService;
    }

    @PostMapping(path = "/")
    public ResponseEntity<Void> executePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        // Execute Pre Qualification
        this.paymentQualifierService.qualifyPayment(paymentRequest);
        // Return no content response
        return ResponseEntity.noContent().build();
    }

}
