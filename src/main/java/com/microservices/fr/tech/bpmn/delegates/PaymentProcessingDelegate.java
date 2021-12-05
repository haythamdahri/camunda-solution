package com.microservices.fr.tech.bpmn.delegates;

import com.microservices.fr.tech.constants.BPMNConstants;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.mq.producers.PaymentSender;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PaymentProcessingDelegate implements JavaDelegate {

    private final PaymentSender paymentSender;

    public PaymentProcessingDelegate(PaymentSender paymentSender) {
        this.paymentSender = paymentSender;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Start PaymentProcessingDelegate.execute(DelegateExecution delegateExecution)");
        log.info("PaymentProcessingDelegate.execute(DelegateExecution delegateExecution) | ProcessBusinessKey: {}",
                delegateExecution.getProcessBusinessKey());
        log.info("PaymentProcessingDelegate.execute(DelegateExecution delegateExecution) | BusinessKey: {}",
                delegateExecution.getBusinessKey());
        PaymentRequest paymentRequest = (PaymentRequest)delegateExecution.getVariable(BPMNConstants.paymentExecutionContextID);
        log.info("PaymentRequest: {}", paymentRequest);
        // Set componentName
        paymentRequest.setComponentName(this.getClass().getSimpleName());
        // Send data through MQ
        this.paymentSender.sendPaymentPayload(paymentRequest, delegateExecution.getProcessBusinessKey());
        log.info("End PaymentProcessingDelegate.execute(DelegateExecution delegateExecution)");
    }
}
