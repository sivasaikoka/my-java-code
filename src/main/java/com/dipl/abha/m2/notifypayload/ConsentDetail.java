package com.dipl.abha.m2.notifypayload;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ConsentDetail {
	public String consentId;
	public LocalDateTime createdAt;
	public Purpose purpose;
	public Patient patient;
	public ConsentManager consentManager;
	public Hip hip;
	public List<String> hiTypes;
	public Permission permission;
	public List<CareContext> careContexts;
}