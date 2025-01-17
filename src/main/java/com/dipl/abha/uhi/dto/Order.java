package com.dipl.abha.uhi.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
	
	private String id;
	private String state;
	private Provider provider;
	private ArrayList<Item> items;
	private Quote quote;  
	private Item item;
	private Fulfillment fulfillment;
	private Billing billing;
	private Customer customer;
	private Payment payment;

}
