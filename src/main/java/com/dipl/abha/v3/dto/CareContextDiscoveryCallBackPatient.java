package com.dipl.abha.v3.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContextDiscoveryCallBackPatient {
	private String id;
	private List<UnverifiedIdentifier> verifiedIdentifiers;
	private List<UnverifiedIdentifier> unverifiedIdentifiers;
	private String name;
	private String gender;
	private Integer yearOfBirth;
}
