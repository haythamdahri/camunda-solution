package com.microservices.fr.tech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueMessage<T> implements Serializable {

    private static final long serialVersionUID = -3551837066766789249L;

    private T content;
    private String businessKey;

}
