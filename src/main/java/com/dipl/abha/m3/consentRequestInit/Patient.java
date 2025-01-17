package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient{
	@NotBlank(message = "Patient Id should not be null or empty")
    public String id;
}

