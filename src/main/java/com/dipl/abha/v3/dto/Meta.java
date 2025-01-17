package com.dipl.abha.v3.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
	private String communicationMedium;
	private String communicationHint;
	private Date communicationExpiry;

}
