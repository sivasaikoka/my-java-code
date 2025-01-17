package com.dipl.abha.m2.authonconfirmpayloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient{
    public String id;
    public String name;
    public String gender;
    public int yearOfBirth;
    public Address address;
    public List<Identifier> identifiers;
}

