package com.dipl.abha.uhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Breakup {
	
	  private String title;
	   
	  private Price price;
	  
	  @JsonProperty("./dhp-0_7_1.consultation")
	  private String consultation;
	  
	  @JsonProperty("./dhp-0_7_1.cgst")
	  private String cgst;
	  
	  @JsonProperty("./dhp-0_7_1.sgst")
	  private String sgst;

}
