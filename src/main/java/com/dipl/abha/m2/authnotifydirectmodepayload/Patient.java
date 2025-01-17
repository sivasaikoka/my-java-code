package com.dipl.abha.m2.authnotifydirectmodepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient{
    public String id;
    public String name;
    public String gender;
    public int yearOfBirth;
    public Address address;
    public String healthIdNumber;
    public List<Identifier> identifiers;
}

