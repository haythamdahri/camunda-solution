package com.microservices.fr.tech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PaymentRequest implements Serializable {

    private static final long serialVersionUID = 8965155707378703067L;

    @JsonProperty("userIdentifier")
    @NotEmpty
    private String userIdentifier;

    @JsonProperty("amount")
    @NotNull
    private BigDecimal amount;

    @JsonProperty("currency")
    @NotEmpty
    private String currency;

    @JsonProperty("executionDateTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime executionDateTime;

    /**
     * Property to be filled by business service {@link com.microservices.fr.tech.services.impl.PaymentQualifierServiceImpl}
     */
    private ClientInfo clientInfo;

    /**
     * Property to be filled by each process at the end of processing
     */
    private String componentName;

    /**
     * Property to be filled by each process at the beginning of qualifier service
     */
    private String paymentId;

}
