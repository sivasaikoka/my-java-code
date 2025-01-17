package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResendAadharOtpOrMobileOtp {
	private String txnId;
	private String authToken;
}
