package com.dipl.abha.m2.afterpushnotifyhippayloads;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification{
    public String consentId;
    public String transactionId;
    public String doneAt;
    public Notifier notifier;
    public StatusNotification statusNotification;
}
