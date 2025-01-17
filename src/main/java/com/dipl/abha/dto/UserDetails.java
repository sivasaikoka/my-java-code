package com.dipl.abha.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String middleName;
	private String lastName;
	private String photo;
	private String gender;
	private String name;
	private String email;
	private String phone;
	private String pincode;
	private String birthdate;
	private String careOf;
	private String house;
	private String street;
	private String landmark;
	private String locality;
	private String villageTownCity;
	private String subDist;
	private String district;
	private String state;
	private String postOffice;
	private String aadhaar;
	private String txnId;
	private String healthIdNumber;
	private JsonNode jwtResponse;
}
