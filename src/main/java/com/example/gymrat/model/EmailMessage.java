package com.example.gymrat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {

    @JsonProperty("to")
    private String to;

    @JsonProperty("verificationUrl")
    private String verificationUrl;

    @JsonProperty("emailType")
    private EmailType emailType;
}