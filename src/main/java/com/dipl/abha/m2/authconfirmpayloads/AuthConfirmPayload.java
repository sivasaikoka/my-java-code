package com.dipl.abha.m2.authconfirmpayloads;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthConfirmPayload {
	@NotEmpty(message = "Requester Id should not be empty")
	public String requestId;
	@NotNull
	public LocalDateTime timestamp;
	@NotEmpty(message = "transactionId should not be null or empty")
	public String transactionId;
	@Valid
	public Credential credential;
}
