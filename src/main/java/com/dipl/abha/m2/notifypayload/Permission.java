package com.dipl.abha.m2.notifypayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Permission {
	public String accessMode;
	public DateRange dateRange;
	public LocalDateTime dataEraseAt;
	public Frequency frequency;
}