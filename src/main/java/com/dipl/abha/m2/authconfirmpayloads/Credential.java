package com.dipl.abha.m2.authconfirmpayloads;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
	
	public String authCode;
	@Valid
	public Demographic demographic;
}
