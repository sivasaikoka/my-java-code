package com.dipl.abha.v3.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
	
	
	   private Date from;
	   
	    @JsonProperty("to") 
	    private Date myto;

}
