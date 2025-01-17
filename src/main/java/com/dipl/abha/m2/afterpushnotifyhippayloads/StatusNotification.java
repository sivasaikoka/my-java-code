package com.dipl.abha.m2.afterpushnotifyhippayloads;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusNotification{
    public String sessionStatus;
    public String hipId;
    public List<StatusResponse> statusResponses;
}