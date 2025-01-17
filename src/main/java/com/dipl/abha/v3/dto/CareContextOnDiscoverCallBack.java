package com.dipl.abha.v3.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContextOnDiscoverCallBack {
	private String transactionId;
	private ArrayList<Patient> patient;
	private LocalDateTime createdAt;
	private Response response;
}
