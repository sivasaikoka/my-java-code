package com.dipl.abha.v3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareContext {

	private String referenceNumber;

	private String display;

	private String patientReference;

	private String careContextReference;
}
