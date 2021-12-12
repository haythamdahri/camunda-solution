package com.microservices.fr.tech.mq.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.fr.tech.bpmn.definitions.ProcessDefinitionId;
import com.microservices.fr.tech.constants.BPMNConstants;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.dto.QueueMessage;
import com.microservices.fr.tech.services.CamundaRunningOrchestratorService;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 */
@RabbitListener(queues = {"${com.tech.queues.create_payment}"})
@Component
@Log4j2
public class PaymentListener {

    private final CamundaRunningOrchestratorService camundaRunningOrchestratorService;
    private final ObjectMapper objectMapper;

    public PaymentListener(CamundaRunningOrchestratorService camundaRunningOrchestratorService, ObjectMapper objectMapper) {
        this.camundaRunningOrchestratorService = camundaRunningOrchestratorService;
        this.objectMapper = objectMapper;
    }

    @RabbitHandler
    public void handlePaymentQueueMessage(QueueMessage<Object> paymentQueueMessage) throws InterruptedException {
        Thread.sleep(1_000);
        log.info("Start PaymentListener.handlePaymentQueueMessage(QueueMessage<PaymentRequest> paymentQueueMessage)");
        log.info("paymentQueueMessage: {}", paymentQueueMessage);
        // Update componentName
        final PaymentRequest paymentRequest = this.objectMapper.convertValue(paymentQueueMessage.getContent(), PaymentRequest.class);
        paymentRequest.setComponentName(this.getClass().getSimpleName());
        VariableMap variables = Variables.putValue(BPMNConstants.paymentExecutionContextID, paymentRequest);
        // Get Process Instance
        ProcessInstance processInstance = this.camundaRunningOrchestratorService.getProcessInstanceFromBusinessKeyAndDefinitionKey(
                paymentRequest.getPaymentId(), ProcessDefinitionId.CREATE_PAYMENT
        );
        log.info("ProcessInstance Payment: {}", processInstance);
        // Continue process
        this.camundaRunningOrchestratorService.continueProcess(BPMNConstants.paymentCollector, processInstance.getId(), variables);
        log.info("End PaymentListener.handlePaymentQueueMessage(QueueMessage<PaymentRequest> paymentQueueMessage)");
    }

}
