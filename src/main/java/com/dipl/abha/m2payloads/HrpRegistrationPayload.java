package com.dipl.abha.m2payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrpRegistrationPayload{
    public String id;
    public String name;
    public String type;
    public boolean active;
    public List<String> alias;
}