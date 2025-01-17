package com.dipl.abha.v3.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
	
	
	private String accessMode;
    private DateRange dateRange;
    private Date dataEraseAt;
    private Frequency frequency;

}
