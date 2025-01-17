package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingLicenceDetails {
	public String txnId;
    public String documentId;
    public String firstName;
    public String middleName;
    public String lastName;
    public String dob;
    public String gender;
    public String frontSidePhoto;
    public String backSidePhoto;
    public String address;
    public String state;
    public String district;
    public String pinCode;
}
