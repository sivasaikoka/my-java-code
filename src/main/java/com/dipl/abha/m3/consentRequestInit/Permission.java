package com.dipl.abha.m3.consentRequestInit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission {
	@NotBlank(message = "accessMode should not be null or empty")
	public String accessMode;
	@NotNull(message = "dateRange should not be null")
	public DateRange dateRange;
	@NotBlank(message = "dataEraseAt should not be null or empty")
	public String dataEraseAt;
	@NotNull(message = "frequency should not be null")
	public Frequency frequency;
}
