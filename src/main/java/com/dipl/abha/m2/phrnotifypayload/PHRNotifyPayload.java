package com.dipl.abha.m2.phrnotifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PHRNotifyPayload{
    public String requestId;
    public String timestamp;
    public Notification notification;
}

