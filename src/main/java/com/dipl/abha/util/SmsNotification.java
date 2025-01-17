package com.dipl.abha.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsNotification {

	@JsonProperty("toNumbers")
	private String toNumbers;
	
	@JsonProperty("messages")
	private String messages;
	
	@JsonProperty("templateId")
	private String templateId;
	
}
