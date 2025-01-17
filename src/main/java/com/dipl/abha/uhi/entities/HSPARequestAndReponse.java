package com.dipl.abha.uhi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hspa_request_response")
@Entity
@Data
public class HSPARequestAndReponse {

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
		@Type(type = "com.dipl.abha.util.JsonNodeUserType")
		private JsonNode responseJson;
		
		@Column(name = "request_json")
		@Type(type = "com.dipl.abha.util.JsonNodeUserType")
		private JsonNode requestJson;
		
		@Column(name = "created_on")
		private Date createdOn;
		
}
