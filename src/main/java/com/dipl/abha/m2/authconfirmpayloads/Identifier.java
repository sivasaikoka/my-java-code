package com.dipl.abha.m2.authconfirmpayloads;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identifier {
	@NotEmpty(message = "type should not be null")
	public String type;
	@NotEmpty(message = "value should not be null")
	public String value;
}
