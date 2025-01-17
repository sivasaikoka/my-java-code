package com.dipl.abha.m2.notifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Purpose {
	public String text;
	public String code;
	public Object refUri;
}