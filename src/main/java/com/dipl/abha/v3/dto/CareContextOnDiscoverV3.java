package com.dipl.abha.v3.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dipl.abha.dto.Error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContextOnDiscoverV3 {
	@Valid
	@NotBlank(message = "transactionId should not be empty")
	private String transactionId;
	private List<Patient> patient;
	private String display;

	@NotNull(message = "matchedBy should not be null")
	private List<String> matchedBy;
	@NotNull(message = "response should not be null")
	private Response response;
	private String timestamp;
	private String requestId;

	private Error error;
}
