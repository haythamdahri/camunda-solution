package com.microservices.fr.tech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobInfo implements Serializable {

    private static final long serialVersionUID = -5908887939157095917L;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("jobDescription")
    private String jobDescription;

    @JsonProperty("employerName")
    private String employerName;

    @JsonProperty("employerFiscalNumber")
    private String employerFiscalNumber;

    @JsonProperty("employerAddress")
    private String employerAddress;

}
