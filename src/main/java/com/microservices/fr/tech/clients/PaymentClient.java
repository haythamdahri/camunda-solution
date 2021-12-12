package com.microservices.fr.tech.clients;

import com.microservices.fr.tech.constants.UrisConstants;
import com.microservices.fr.tech.dto.ApiResponse;
import com.microservices.fr.tech.dto.ClientInfo;
import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.exceptions.TechnicalException;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;

@Component
@Log4j2
public class PaymentClient {

    private final WebClient clientAPIWebClient;

    public PaymentClient(@Value("${com.microservices.fr.client.uri}") final String clientUri) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(60))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);
        this.clientAPIWebClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(clientUri).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    /**
     * Get client info from Payment API
     *
     * @param clientId: Client Identifier
     * @return ClientInfo
     * @throws BusinessException:  Business Error Exception
     * @throws TechnicalException: Technical Error Exception
     */
    public ClientInfo getClientInfo(String clientId) throws BusinessException, TechnicalException {
        log.info("Start PaymentClient.getClientInfo(String clientId)");
        // Get client info
        ClientInfo clientInfo = this.clientAPIWebClient.get()
                .uri(UrisConstants.CLIENT_INFO_URI,
                        Collections.singletonMap("id", clientId)
                )
                .retrieve().onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.info("Error while invoking client info endpoint: {}", clientResponse.statusCode());
                    return clientResponse.bodyToMono(ApiResponse.class)
                            .handle((error, sink) -> sink.error(new BusinessException(error.getMessage())));
                }).onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.info("Error while invoking client info endpoint: {}",
                            clientResponse.statusCode()
                    );
                    return clientResponse.bodyToMono(ApiResponse.class)
                            .handle((error, sink) -> sink.error(new TechnicalException(error.getMessage())));
                }).bodyToMono(ClientInfo.class).block();
        log.info("End PaymentClient.getClientInfo(String clientId)");
        // Return response
        return clientInfo;
    }

}
