package com.dipl.abha.m2.initpayloads;

import javax.validation.constraints.NotEmpty;

import com.dipl.abha.annotations.RequesterType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Requester {

	@RequesterType
	@NotEmpty(message = "Requester Type should not be empty")
	public String type;
	@NotEmpty(message = "Requester Id should not be empty")
	public String id;
}
