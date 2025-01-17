package com.dipl.abha.m2.fhirbundle;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FhirBundle{
    public String resourceType;
    public String id;
    public Meta meta;
    public Identifier identifier;
    public String type;
    public String timestamp;
    public List<Entry> entry;
}