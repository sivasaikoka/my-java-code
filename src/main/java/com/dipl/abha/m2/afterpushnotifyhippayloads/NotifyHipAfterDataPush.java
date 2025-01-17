package com.dipl.abha.m2.afterpushnotifyhippayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyHipAfterDataPush{
    public String requestId;
    public String timestamp;
    public Notification notification;
}

