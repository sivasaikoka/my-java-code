package com.dipl.abha.m2.requestypayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayload {
		public String privateKey;
	    public String transactionId;
	    public String requestId;
	    public LocalDateTime timestamp;
	    public HiRequest hiRequest;
	    public String hiuId;
	}
