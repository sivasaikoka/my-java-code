package com.dipl.abha.m2.authconfirmpayloads;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demographic {
	@NotEmpty(message = "Name should not be null")
	public String name;
	@NotEmpty(message = "Gender should not be null")
	public String gender;
	@NotEmpty(message = "dateOfBirth should not be null")
	public String dateOfBirth;
	@Valid
	public Identifier identifier;
}
