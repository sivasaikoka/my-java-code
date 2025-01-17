package com.dipl.abha.m2.discoverpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedIdentifiers {
	private String type;
	private String value;
}