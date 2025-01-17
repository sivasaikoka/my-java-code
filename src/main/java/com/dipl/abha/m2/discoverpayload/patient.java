package com.dipl.abha.m2.discoverpayload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class patient {
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("gender")
	private Character gender;
	@JsonProperty("yearOfBirth")
	private String yearOfBirth;
	@JsonProperty("verifiedIdentifiers")
	private List<VerifiedIdentifiers> verifiedIdentifiers;
	@JsonProperty("unverifiedIdentifiers")
	private List<UnverifiedIdentifiers> unverifiedIdentifiers;



}
