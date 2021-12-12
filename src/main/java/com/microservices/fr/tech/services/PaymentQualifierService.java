package com.microservices.fr.tech.services;

import com.microservices.fr.tech.dto.CancelPaymentRequestDTO;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import org.springframework.lang.NonNull;

public interface PaymentQualifierService {

    /**
     * Qualify payment request
     *
     * @param paymentRequest: Payment Payload
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void qualifyPayment(PaymentRequest paymentRequest) throws BusinessException, TechnicalException;

    /**
     * Cancel an existing payment
     *
     * @param cancelPaymentRequest: Payment To Cancel
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void cancelPayment(@NonNull CancelPaymentRequestDTO cancelPaymentRequest) throws BusinessException, TechnicalException;

}
