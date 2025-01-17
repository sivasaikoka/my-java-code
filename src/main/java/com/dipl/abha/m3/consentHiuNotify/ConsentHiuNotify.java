package com.dipl.abha.m3.consentHiuNotify;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentHiuNotify {
	public String requestId;
	public LocalDateTime timestamp;
	public Notification notification;
}
