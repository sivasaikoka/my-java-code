package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotAbhaVerifyMobileOtpResponse {
	public String healthId;
	public String healthIdNumber;
	public ResponseToken jwtResponse;
}
