package com.dipl.abha.m2.discoverpayload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscoverPayload {
	
	@JsonProperty("patient")
	private patient patient;
	
	@JsonProperty("requestId")
	private String requestId;
	@JsonProperty("timestamp")
	private LocalDateTime timestamp;
	@JsonProperty("transactionId")
	private String transactionId;
}
