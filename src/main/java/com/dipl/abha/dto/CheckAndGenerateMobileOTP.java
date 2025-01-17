package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckAndGenerateMobileOTP {

	private String txnId;
	
	private Boolean mobileLinked;

}
