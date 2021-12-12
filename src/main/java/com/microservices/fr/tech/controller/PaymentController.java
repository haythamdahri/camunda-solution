package com.microservices.fr.tech.controller;

import com.microservices.fr.tech.dto.CancelPaymentRequestDTO;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.services.PaymentQualifierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/cancellations")
    public ResponseEntity<Void> cancelPayment(@Valid @RequestBody CancelPaymentRequestDTO cancelPaymentRequest) {
        // Execute Payment cancellation workflow
        this.paymentQualifierService.cancelPayment(cancelPaymentRequest);
        // Return no content response
        return ResponseEntity.noContent().build();
    }

}
