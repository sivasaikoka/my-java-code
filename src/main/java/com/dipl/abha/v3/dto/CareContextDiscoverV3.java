package com.dipl.abha.v3.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CareContextDiscoverV3 {
	private Hip hip;
	private List<UnverifiedIdentifier> unverifiedIdentifier;
}
