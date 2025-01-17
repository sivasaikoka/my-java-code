package com.dipl.abha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerifyDto {
private String token;
@JsonProperty("ABHANumber")
private String ABHANumber;
private String txnId;
}
