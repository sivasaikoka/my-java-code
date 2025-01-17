package com.dipl.abha.m2.linksonconfirm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient{
    public String referenceNumber;
    public String display;
    public List<CareContext> careContexts;
    public String  hiType;
    public Integer count;
}

