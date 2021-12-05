package com.microservices.fr.tech.bpmn.delegates;

import com.microservices.fr.tech.constants.BPMNConstants;
import com.microservices.fr.tech.dto.PaymentRequest;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PostPaymentProcessor implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Start PostPaymentProcessor.execute(DelegateExecution delegateExecution)");
        log.info("PostPaymentProcessor.execute(DelegateExecution delegateExecution) | ProcessBusinessKey: {}",
                delegateExecution.getProcessBusinessKey());
        log.info("PostPaymentProcessor.execute(DelegateExecution delegateExecution) | BusinessKey: {}",
                delegateExecution.getBusinessKey());
        PaymentRequest paymentRequest = (PaymentRequest)delegateExecution.getVariable(BPMNConstants.paymentExecutionContextID);
        log.info("PaymentRequest: {}", paymentRequest);
        // Set componentName
        paymentRequest.setComponentName(this.getClass().getSimpleName());
        log.info("End PostPaymentProcessor.execute(DelegateExecution delegateExecution)");
    }
}
