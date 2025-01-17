package com.dipl.abha.m2.initpayloads;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dipl.abha.annotations.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchAndInitPayload {

	@NotEmpty(message = "Request Id should not be null or empty")
	@JsonProperty("requestId")
	public String requestId;
	@NotNull
	public LocalDateTime timestamp;
	@Valid
	public Query query;
}
