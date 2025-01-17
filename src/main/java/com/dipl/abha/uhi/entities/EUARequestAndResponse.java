package com.dipl.abha.uhi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eua_request_response")
@Entity
@Data
public class EUARequestAndResponse {
	
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Id
   private Long id; 
	
	@Column(name = "api_type")
	private String apiType;
	
	private String type;
	
	@Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="order_id")
	private String orderId;
	
	@Column(name = "response_json")
	private String responseJson;
	
	@Column(name = "request_json")
	private String requestJson;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	

}
