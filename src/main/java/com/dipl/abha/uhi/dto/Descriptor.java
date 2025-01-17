package com.dipl.abha.uhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Descriptor {
	
	private String code;
	private String name;
	private String images;
	private String short_desc;
	private String long_desc;

}
