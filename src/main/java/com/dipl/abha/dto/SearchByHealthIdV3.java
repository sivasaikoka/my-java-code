package com.dipl.abha.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByHealthIdV3 {
	@JsonProperty("healthIdNumber")
    private String healthIdNumber;

    @JsonProperty("abhaAddress")
    private String abhaAddress;

    @JsonProperty("authMethods")
    private List<String> authMethods;

    @JsonProperty("blockedAuthMethods")
    private List<String> blockedAuthMethods;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("mobile")
    private String mobile;
}
