package com.dipl.abha.dto;

import java.util.List;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NDHMResponse{
    public String healthIdNumber;
    public String healthId;
    public String mobile;
    public String firstName;
    public String middleName;
    public String lastName;
    public String name;
    public String yearOfBirth;
    public String dayOfBirth;
    public String monthOfBirth;
    public String gender;
    public Object email;
    public String profilePhoto;
    public String stateCode;
    public String districtCode;
    public Object subDistrictCode;
    public Object villageCode;
    public Object townCode;
    public Object wardCode;
    public String pincode;
    public String address;
    public String kycPhoto;
    public String stateName;
    public String districtName;
    public Object subdistrictName;
    public Object villageName;
    public String townName;
    public Object wardName;
    public List<String> authMethods;
    @Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode tags;
    public boolean kycVerified;
    public Object verificationStatus;
    public Object verificationType;
    public List<String> phrAddress;
    public boolean emailVerified;
    @JsonProperty("new") 
    public boolean mynew;
}
