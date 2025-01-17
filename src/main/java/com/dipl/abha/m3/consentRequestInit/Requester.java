package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Requester{
	@NotBlank(message = "Requester name can not be null or empty")
    public String name;
	@NotNull(message = "Identifier identifier can not be null or empty")
    public Identifier identifier;
}

