package com.dipl.abha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Error {

	
	@JsonProperty("code")
	private Integer status;
	
	
	@JsonProperty("message")
	private String message;

}
