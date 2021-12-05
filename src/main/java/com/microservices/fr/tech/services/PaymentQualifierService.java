package com.microservices.fr.tech.services;

import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;

public interface PaymentQualifierService {

    /**
     *
     * Qualify payment request
     * @param paymentRequest: Payment Payload
     * @throws BusinessException: Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void qualifyPayment(PaymentRequest paymentRequest) throws BusinessException, TechnicalException;

}
