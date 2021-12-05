package com.microservices.fr.tech.services.impl;

import com.microservices.fr.tech.bpmn.definitions.ProcessDefinitionId;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.ProcessNotFoundException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import com.microservices.fr.tech.services.CamundaRunningOrchestratorService;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.util.annotation.NonNull;

import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@Service
@Log4j2
public class CamundaRunningOrchestratorServiceImpl implements CamundaRunningOrchestratorService {

    private final RuntimeService runtimeService;

    public CamundaRunningOrchestratorServiceImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void startProcess(ProcessDefinitionId processDefinitionId, String businessKey, Map<String, Object> variables) throws BusinessException, TechnicalException {
        this.runtimeService.startProcessInstanceByKey(processDefinitionId.toString(), businessKey, variables);
    }

    @Override
    /**@Retryable(
            value = {RuntimeException.class},
            maxAttemptsExpression = "${com.camunda.process-lookup.retryable.max-attempts}",
            backoff = @Backoff(
                    delayExpression = "${com.camunda.process-lookup.retryable.backoff.delay}",
                    multiplierExpression = "${com.camunda.process-lookup.retryable.backoff.delay.multiplier}",
                    maxDelayExpression = "${com.camunda.process-lookup.retryable.backoff.delay.max-delay-expression}"
            )
    )*/
    public void continueProcess(String eventName, String correlationID, Map<String, Object> variables) throws BusinessException, TechnicalException {
        this.runtimeService.createMessageCorrelation(eventName)
                .processInstanceId(correlationID)
                .setVariables(variables)
                .correlate();
    }

    @Override
    public ProcessInstance getProcessInstanceFromBusinessKeyAndDefinitionKey(
            @NonNull final String businessKey,
            @NonNull final ProcessDefinitionId processDefinitionId
    ) throws ProcessNotFoundException {
        final String processDefinitionKey = processDefinitionId.toString();
        return runtimeService
                .createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .processDefinitionKey(processDefinitionKey)
                .list()
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    final String message = String.format("paymentId: %s, No process instance found", businessKey);
                    log.error(message);
                    return new ProcessNotFoundException(message);
                });
    }

}
