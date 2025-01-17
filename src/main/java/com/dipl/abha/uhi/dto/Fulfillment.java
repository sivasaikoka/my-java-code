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
public class Fulfillment {

	private Agent agent;
	private String type;
	private Start start;
	private End end;
	private Tags tags;
	private String id;
	private Time time;
	 private Person person;
	 private List<Slots> slots;

}
