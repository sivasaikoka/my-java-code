package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Demographic{
    public String name;
    public String gender;
    public String dateOfBirth;
    public Identifier identifier;
}
