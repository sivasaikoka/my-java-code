package com.dipl.abha.m3.consentRequestInit;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Consent {
	@Valid
	@NotNull(message = "purpose cannot not be null")
	public Purpose purpose;
	@Valid
	@NotNull(message = "patient cannot not be null")
	public Patient patient;
	public Hip hip;
	public List<CareContext> careContexts;
	@Valid
	@NotNull(message = "hiu cannot not be null")
	public Hiu hiu;
	@Valid
	@NotNull(message = "requester cannot not be null")
	public Requester requester;
	@NotEmpty(message = "hiTypes cannot be empty.")
	public List<String> hiTypes;

	@NotNull(message = "permission cannot not be null")
	@Valid
	public Permission permission;
}
