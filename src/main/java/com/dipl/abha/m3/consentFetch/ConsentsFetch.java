package com.dipl.abha.m3.consentFetch;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentsFetch {
	public String requestId;
	public LocalDateTime timestamp;
	public String consentId;
}