package com.dipl.abha.m2.onshareprofilepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareAcknowledgement{
    public String status;
    public String healthId;
    public String tokenNumber;
}

