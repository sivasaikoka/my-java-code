package com.dipl.abha.m2.fhirbundle;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resource{
    public String resourceType;
    public String id;
    public Object identifier;
    public String status;
    public Type type;
    public Subject subject;
    public Encounter encounter;
    public String date;
    public List<Author> author;
    public String title;
    public List<Section> section;
    public List<Name> name;
    public String gender;
    @JsonProperty("class") 
    public Class myclass;
    public Period period;
    public List<Content> content;
    public Code code;
    public String performedDateTime;
    public Asserter asserter;
    public List<Complication> complication;
    
    public Meta meta;
    public boolean active;
    public String birthDate;
    public boolean deceasedBoolean;
    public List<Address> address;
}
