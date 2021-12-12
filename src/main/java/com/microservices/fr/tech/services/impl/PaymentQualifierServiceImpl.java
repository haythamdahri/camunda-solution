package com.microservices.fr.tech.services.impl;


import com.microservices.fr.tech.bpmn.definitions.ProcessDefinitionId;
import com.microservices.fr.tech.clients.PaymentClient;
import com.microservices.fr.tech.constants.BPMNConstants;
import com.microservices.fr.tech.dto.CancelPaymentRequestDTO;
import com.microservices.fr.tech.dto.ClientInfo;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import com.microservices.fr.tech.services.CamundaRunningOrchestratorService;
import com.microservices.fr.tech.services.PaymentQualifierService;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class PaymentQualifierServiceImpl implements PaymentQualifierService {

    private final PaymentClient paymentClient;
    private final CamundaRunningOrchestratorService camundaRunningOrchestratorService;

    public PaymentQualifierServiceImpl(PaymentClient paymentClient, CamundaRunningOrchestratorService camundaRunningOrchestratorService) {
        this.paymentClient = paymentClient;
        this.camundaRunningOrchestratorService = camundaRunningOrchestratorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void qualifyPayment(PaymentRequest paymentRequest) {
        log.info("Start PaymentQualifierServiceImpl.qualifyPayment(PaymentRequest paymentRequest)");
        // Get client info
        ClientInfo clientInfo = this.paymentClient.getClientInfo(paymentRequest.getUserIdentifier());
        log.info("ClientInfo: {}", clientInfo);
        // Fill clientInfo, componentName and paymentId
        paymentRequest.setComponentName(this.getClass().getSimpleName());
        paymentRequest.setClientInfo(clientInfo);
        paymentRequest.setPaymentId(UUID.randomUUID().toString());
        // Prepare variables
        VariableMap variables = Variables.putValue(BPMNConstants.paymentExecutionContextID, paymentRequest);
        // Start payment-process BPMN
        this.camundaRunningOrchestratorService.startProcess(ProcessDefinitionId.CREATE_PAYMENT, paymentRequest.getPaymentId(), variables);
        log.info("End PaymentQualifierServiceImpl.qualifyPayment(PaymentRequest paymentRequest)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void cancelPayment(CancelPaymentRequestDTO cancelPaymentRequest) throws BusinessException, TechnicalException {
        log.info("Start PaymentQualifierServiceImpl.cancelPayment(String paymentId)");
        // Get Process ID
        ProcessInstance processInstance = this.camundaRunningOrchestratorService
                .getProcessInstanceFromBusinessKeyAndDefinitionKey(
                        cancelPaymentRequest.getPaymentId(),
                        ProcessDefinitionId.CREATE_PAYMENT
                );
        log.info("Business Key: {} | Suspended: {} | Ended: {}",
                cancelPaymentRequest.getPaymentId(),
                processInstance.isSuspended(),
                processInstance.isEnded()
        );
        // Proceed To Cancellation
        this.camundaRunningOrchestratorService.cancelProcess(processInstance.getId());
        log.info("End PaymentQualifierServiceImpl.cancelPayment(String paymentId)");
    }

}
