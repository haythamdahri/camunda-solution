package com.microservices.fr.tech.mocks.controllers;

import com.microservices.fr.tech.exceptions.BusinessException;
import com.microservices.fr.tech.dto.ClientInfo;
import com.microservices.fr.tech.dto.JobInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/mocks/v1/clients")
public class ClientMockController {

    private static final Map<String, ClientInfo> CLIENTS = new HashMap<>();

    static {
        // Add mock clients
        CLIENTS.put("850",
                ClientInfo.builder()
                        .address("13 Route de saclay, Palaiseau")
                        .firstname("Haytham")
                        .lastname("DAHRI")
                        .jobInfo(
                                JobInfo.builder()
                                        .jobName("Full Stack Java Engineer | Microservices | DevOps")
                                        .jobDescription("Design, development of Microservices OMS Architecture")
                                        .employerName("Carrefour, SIDEXIA")
                                        .employerAddress("Massy, Boulogne-Billancourt")
                                        .employerFiscalNumber("458751487512")
                                        .build()
                        ).build()
                );
    }

    @RequestMapping(path = "/{id}")
    public ResponseEntity<ClientInfo> getClientInfo(@PathVariable(name = "id") String clientId) throws InterruptedException {
        Thread.sleep(10000L);
        if( !CLIENTS.containsKey(clientId) ) {
            throw new BusinessException("No client found with ID " + clientId);
        }
        return ResponseEntity.ok(CLIENTS.get(clientId));
    }

}
