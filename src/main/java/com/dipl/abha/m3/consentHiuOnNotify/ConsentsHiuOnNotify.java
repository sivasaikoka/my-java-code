package com.dipl.abha.m3.consentHiuOnNotify;

import java.util.List;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentsHiuOnNotify{
	public String statusForRef;
    public String requestId;
    public String timestamp;
    public List<Acknowledgement> acknowledgement;
    public Error error;
    public Resp resp;
    
    public String hiuId;
    public Resp response;
}