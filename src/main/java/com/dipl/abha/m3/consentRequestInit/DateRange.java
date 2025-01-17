package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRange {
	@NotBlank(message = "DateRange from should not be empty")
	public String from;
	@NotBlank(message = "DateRange to should not be empty")
	@JsonProperty("to")
	public String myto;
}
