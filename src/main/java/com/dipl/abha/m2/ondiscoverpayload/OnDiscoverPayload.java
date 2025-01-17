package com.dipl.abha.m2.ondiscoverpayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnDiscoverPayload {
	@JsonProperty("requestId")
	private String requestId;
	@JsonProperty("timestamp")
	private LocalDateTime timeStamp;
	@JsonProperty("transactionId")
	private String transactionId;
	@JsonProperty("patient")
	private PatientOnDiscover patient;
	@JsonProperty("resp")
	private Resp resp;
	@JsonProperty("error")
	private Error error;
}
