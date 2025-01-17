package com.dipl.abha.m2.notifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Notification {
	public ConsentDetail consentDetail;
	public String status;
	public String signature;
	public String consentId;
	public boolean grantAcknowledgement;
}