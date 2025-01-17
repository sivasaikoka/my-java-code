package com.dipl.abha.m2.notifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Frequency {
	public String unit;
	public int value;
	public int repeats;
}