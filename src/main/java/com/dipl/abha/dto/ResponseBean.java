package com.dipl.abha.dto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {
	
	private HttpStatus status;
	private String message;
	private Object data;
	private Object ndhmResponse;


}
