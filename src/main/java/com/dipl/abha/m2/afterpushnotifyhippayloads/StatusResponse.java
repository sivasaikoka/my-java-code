package com.dipl.abha.m2.afterpushnotifyhippayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse{
    public String careContextReference;
    public String hiStatus;
    public String description;
}

