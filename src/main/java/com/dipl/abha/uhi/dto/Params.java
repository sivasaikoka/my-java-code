package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
	
	private String amount;
    private String mode;
    private String vpa;
    private String transaction_id;
	private String redirect_url;

	
	
}
