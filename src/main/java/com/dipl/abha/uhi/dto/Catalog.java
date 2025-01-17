package com.dipl.abha.uhi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog {
	
	private Descriptor descriptor;
	private List<Provider> providers;

}
