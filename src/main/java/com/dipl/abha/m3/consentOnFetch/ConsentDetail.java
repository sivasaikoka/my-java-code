package com.dipl.abha.m3.consentOnFetch;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentDetail {
	public String schemaVersion;
	public String consentId;
	public LocalDateTime createdAt;
	public Patient patient;
	public List<CareContext> careContexts;
	public Purpose purpose;
	public Hip hip;
	public Hiu hiu;
	public ConsentManager consentManager;
	public Requester requester;
	public List<String> hiTypes;
	public Permission permission;
}
