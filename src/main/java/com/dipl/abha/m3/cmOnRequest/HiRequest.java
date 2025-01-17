package com.dipl.abha.m3.cmOnRequest;

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
