package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

	private String accessToken;

	private String expiresIn;

	private String refreshExpiresIn;

	private String refreshToken;

	private String tokenType;

}
