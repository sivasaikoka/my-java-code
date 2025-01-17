package com.dipl.abha.v3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnverifiedIdentifier {
	private String type;
	private String value;

}
