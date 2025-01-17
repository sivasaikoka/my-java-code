package com.dipl.abha.m2.onfetchpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auth {

	private String purpose;

	private String[] modes;
}
