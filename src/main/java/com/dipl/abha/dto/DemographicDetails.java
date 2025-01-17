package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemographicDetails {
	private String gender;
	private long mobile;
    private String name;
    private int yearOfBirth;
}
