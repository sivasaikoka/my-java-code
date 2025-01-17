package com.dipl.abha.dto;

import java.util.ArrayList;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountProfile {
	private String healthIdNumber;
    private String healthId;
    private String mobile;
    private String firstName;
    private String middleName;
    private String lastName;
    private String name;
    private String yearOfBirth;
    private String dayOfBirth;
    private String monthOfBirth;
    private String gender;
    private String email;
    private String profilePhoto;
    private String stateCode;
    private String districtCode;
    private String subDistrictCode;
    private String villageCode;
    private String townCode;
    private String wardCode;
    private String pincode;
    private String address;
    private String kycPhoto;
    private String stateName;
    private String districtName;
    private String subdistrictName;
    private String villageName;
    private String townName;
    private String wardName;
    private ArrayList<String> authMethods;
    @Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode tags;
    private boolean kycVerified;
    private String verificationStatus;
    private String verificationType;
    private ArrayList<String> phrAddress;
    @JsonProperty("new") 
    private boolean mynew;
    private boolean emailVerified;
}




