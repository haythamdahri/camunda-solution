package com.microservices.fr.tech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelPaymentRequestDTO implements Serializable {

    @JsonProperty("paymentId")
    @NotEmpty
    private String paymentId;

}
