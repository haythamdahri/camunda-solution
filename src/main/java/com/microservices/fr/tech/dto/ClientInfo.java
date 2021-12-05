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
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = -5908887939157095917L;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("address")
    private String address;

    @JsonProperty("jobInfo")
    private JobInfo jobInfo;

}
