package com.dipl.abha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbhaDetail {

	@JsonProperty("index")
	private int index;

	@JsonProperty("ABHANumber")
	private String ABHANumber;

	@JsonProperty("name")
	private String name;

	@JsonProperty("gender")
	private String gender;
}
