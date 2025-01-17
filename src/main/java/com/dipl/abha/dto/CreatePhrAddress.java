package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePhrAddress {
	public String token;
	public int expiresIn;
	public String refreshToken;
	public int refreshExpiresIn;
	public String phrAdress;
	public String authTs;
}