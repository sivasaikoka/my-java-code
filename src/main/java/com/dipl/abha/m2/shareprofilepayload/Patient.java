package com.dipl.abha.m2.shareprofilepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient{
    public String healthId;
    public String healthIdNumber;
    public String name;
    public String gender;
    public Address address;
    public int yearOfBirth;
    public int dayOfBirth;
    public int monthOfBirth;
    public List<Identifier> identifiers;
}