package com.dipl.abha.m2.fhirbundle;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Identifier{
    public String system;
    public String value;
    public String id;
    public String use;
    public Type type;
}