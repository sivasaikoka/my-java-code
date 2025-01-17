package com.dipl.abha.m3.consentHiuNotify;

import java.util.List;

import com.dipl.abha.m3.consentRequestOnInit.ConsentArtefact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification{
    public String consentRequestId;
    public String status;
    public List<ConsentArtefact> consentArtefacts;
}

