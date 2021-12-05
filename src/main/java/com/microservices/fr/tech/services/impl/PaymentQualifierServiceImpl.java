package com.microservices.fr.tech.services.impl;


import com.microservices.fr.tech.bpmn.definitions.ProcessDefinitionId;
import com.microservices.fr.tech.constants.BPMNConstants;
import com.microservices.fr.tech.constants.UrisConstants;
import com.microservices.fr.tech.dto.ApiResponse;
import com.microservices.fr.tech.dto.ClientInfo;
import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import com.microservices.fr.tech.services.CamundaRunningOrchestratorService;
import com.microservices.fr.tech.services.PaymentQualifierService;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Service
@Log4j2
public class PaymentQualifierServiceImpl implements PaymentQualifierService {

    private final WebClient clientAPIWebClient;
    private final CamundaRunningOrchestratorService camundaRunningOrchestratorService;

    public PaymentQualifierServiceImpl(@Value("${com.microservices.fr.client.uri}") final String clientUri, CamundaRunningOrchestratorService camundaRunningOrchestratorService) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(60))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);
        this.clientAPIWebClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(clientUri).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        this.camundaRunningOrchestratorService = camundaRunningOrchestratorService;
    }

    @Override
    @Async
    public void qualifyPayment(PaymentRequest paymentRequest) {
        log.info("Start PaymentQualifierServiceImpl.qualifyPayment(PaymentRequest paymentRequest)");
        // Get client info
        ClientInfo clientInfo = this.clientAPIWebClient.get()
                .uri(UrisConstants.CLIENT_INFO_URI, Collections.singletonMap("id", paymentRequest.getUserIdentifier()))
                .retrieve().onStatus(HttpStatus::is4xxClientError, clientResponse -> {
            log.info("Error while invoking client info endpoint: {}", clientResponse.statusCode());
            return clientResponse.bodyToMono(ApiResponse.class).handle((error, sink) -> sink.error(new BusinessException(error.getMessage())));
        }).onStatus(HttpStatus::is5xxServerError, clientResponse -> {
            log.info("Error while invoking client info endpoint: {}", clientResponse.statusCode());
            return clientResponse.bodyToMono(ApiResponse.class).handle((error, sink) -> sink.error(new TechnicalException(error.getMessage())));
        }).bodyToMono(ClientInfo.class).block();
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

}
