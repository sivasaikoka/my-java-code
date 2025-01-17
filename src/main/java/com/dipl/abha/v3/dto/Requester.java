package com.dipl.abha.v3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requester {
	
	private String name;
    private Identifier identifier;

}
