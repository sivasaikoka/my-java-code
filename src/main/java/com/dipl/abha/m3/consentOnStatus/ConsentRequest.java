package com.dipl.abha.m3.consentOnStatus;

import java.util.List;

import com.dipl.abha.m3.consentRequestOnInit.ConsentArtefact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentRequest{
    public String id;
    public String status;
    public List<ConsentArtefact> consentArtefacts;
}



