package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Identifier {
	@NotBlank(message = "Identifier type shold not be null or empty")
	public String type;
	@NotBlank(message = "Identifier value shold not be null or empty")
	public String value;
	@NotBlank(message = "Identifier system shold not be null or empty")
	public String system;
}
