package com.dipl.abha.m2.onrequestypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HiRequest{
    public String transactionId;
    public String sessionStatus;
}