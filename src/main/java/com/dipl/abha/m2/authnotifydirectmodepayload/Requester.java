package com.dipl.abha.m2.authnotifydirectmodepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Requester{
    public String type;
    public String id;
    public String name;
}

