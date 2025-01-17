package com.dipl.abha.v3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContextOnInitV3 {
	private String transactionId;
	private Link link;
	private Response response;
}
