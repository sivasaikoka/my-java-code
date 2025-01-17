package com.dipl.abha.uhi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Provider {

	private String id;
	private Descriptor descriptor;
	private List<Category> categories;
	private List<Fulfillment> fulfillments;
	private List<Item> items;
	private Location location;

}
