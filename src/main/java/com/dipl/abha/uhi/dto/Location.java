package com.dipl.abha.uhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
	
	private String id;
    private Descriptor descriptor;
    private City city;
    private Country country;
    private String gps;
    private String address;

}
