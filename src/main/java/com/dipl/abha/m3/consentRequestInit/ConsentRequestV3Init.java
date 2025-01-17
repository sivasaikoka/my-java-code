package com.dipl.abha.m3.consentRequestInit;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ConsentRequestV3Init {
	private String requestId;
	private String consentId;
	private String timestamp;
	private String token;
	private String hiuId;
	private String consentRequestId;
	@Valid
	public Consent consent;
}
