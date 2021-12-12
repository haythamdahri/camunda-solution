package com.microservices.fr.tech.services;

import com.microservices.fr.tech.bpmn.definitions.ProcessDefinitionId;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.ProcessNotFoundException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.lang.NonNull;

import java.util.Map;

public interface CamundaRunningOrchestratorService {

    /**
     * Start a new BPMN Process
     *
     * @param processDefinitionId: Process Identifier
     * @param businessKey:         Business Key Identifier
     * @param variables:           Execution variables
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void startProcess(
            @NonNull ProcessDefinitionId processDefinitionId,
            @NonNull String businessKey,
            Map<String, Object> variables
    ) throws BusinessException, TechnicalException;

    /**
     * Continue an existing BPMN Process
     *
     * @param eventName:     Event Name
     * @param correlationId: Correlation Identifier
     * @param variables:     Execution variables
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void continueProcess(
            @NonNull String eventName,
            @NonNull String correlationId,
            Map<String, Object> variables
    ) throws BusinessException, TechnicalException;

    /**
     * Cancel an existing BPMN Process
     *
     * @param correlationId: Correlation Identifier
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    void cancelProcess(
            @NonNull String correlationId
    ) throws BusinessException, TechnicalException;

    /**
     * Retrieve Process Instance from provided input
     *
     * @param businessKey:         Business Key
     * @param processDefinitionId: Process Identifier
     * @return ProcessInstance
     * @throws ProcessNotFoundException: Process Not Found Exception Error
     */
    ProcessInstance getProcessInstanceFromBusinessKeyAndDefinitionKey(
            @NonNull final String businessKey,
            @NonNull final ProcessDefinitionId processDefinitionId
    ) throws ProcessNotFoundException;

}
