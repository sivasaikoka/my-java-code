package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemographicPayload {
private String aadhaarNumber;
private String districtCode;
private String stateCode;
private String dateOfBirth;
private String gender;
private String fullName;
private String mobileNumber;
}
