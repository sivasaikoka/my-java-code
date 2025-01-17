package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContextOnConfirmv3 {
	@Valid
	@NotNull(message = "Patient should not be null")
	private ArrayList<Patient> patient;
	@NotNull(message = "response should not be null")
	private Response response;
}
