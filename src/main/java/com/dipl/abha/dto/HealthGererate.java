package com.dipl.abha.dto;

import java.io.Serializable;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthGererate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String refreshToken;
	private String healthIdNumber;
	private String name;
	private String gender;
	private String yearOfBirth;
	private String monthOfBirth;
	private String dayOfBirth;
	private String healthId;
	private String lastName;
	private String middleName;
	private String stateCode;
	private String districtCode;
	private String email;
	private String kycPhoto;
	private String profilePhoto;
	private String mobile;
	private String[] authMethods;
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode tags;
	@JsonProperty("new")
	private Boolean NEW;
}
