package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Purpose {
	@NotBlank(message = "Purpose text should not be null or empty")
	public String text;
	@NotBlank(message = "Purpose code should not be null or empty")
	public String code;
	public String refUri = "https://www.abdm.gov.in";
}
