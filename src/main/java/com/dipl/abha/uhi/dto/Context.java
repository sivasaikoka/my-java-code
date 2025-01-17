package com.dipl.abha.uhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Context {
	
	private String domain;
	private String country;
	private String city;
	private String action;
	private String core_version;
	private String consumer_id;
	private String consumer_uri;
	private String message_id;
	private String timestamp;
	private String transaction_id;
	
	private String provider_id;
    private String provider_uri;

}
