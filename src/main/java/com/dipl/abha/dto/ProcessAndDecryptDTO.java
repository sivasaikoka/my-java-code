package com.dipl.abha.dto;

import java.io.Serializable;
import java.util.List;

import com.dipl.abha.entities.HIUResponse;
import com.dipl.abha.m2.datapushpayload.DataPushEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessAndDecryptDTO implements Serializable {
	
	private static final long serialVersionUID = -5191270175555692140L;
	
	private List<HIUResponse> consentIdAndArtifactId;
	private List<DataPushEntry> dataEntries;
	private String transactionId;
}