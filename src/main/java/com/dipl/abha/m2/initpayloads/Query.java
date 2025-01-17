package com.dipl.abha.m2.initpayloads;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Query {
	@Email(message = "ABHA Address should contail @")
	@NotEmpty(message = "ABHA address should not be empty")
	public String id;
//	@PurposeType
	@JsonProperty("purpose")
	public String purpose;
//	@NotEmpty(message = "AUTH Mode should not be empty")
//	@JsonProperty("authMode")
	public String authMode;
	@Valid
	public Requester requester;
}
