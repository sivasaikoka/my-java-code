package com.dipl.abha.m2.fhirbundle;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Name{
    public String text;
	
    public List<String> prefix;
	
    public List<String> suffix;
    public String id;
    public String family;
    public List<String> given;
}