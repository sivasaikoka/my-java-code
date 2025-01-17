package com.dipl.abha.m2payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auth {

	@JsonProperty("purpose")
	private String purpose;

	@JsonProperty("modes")
	private String[] modes;
}
