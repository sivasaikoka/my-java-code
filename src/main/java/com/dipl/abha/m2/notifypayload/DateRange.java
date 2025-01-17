package com.dipl.abha.m2.notifypayload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DateRange {
	@JsonProperty("from")
	public LocalDateTime from;
	@JsonProperty("to")
	public LocalDateTime myto;
}