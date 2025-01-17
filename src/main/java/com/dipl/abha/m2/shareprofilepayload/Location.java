package com.dipl.abha.m2.shareprofilepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location{
    public String latitude;
    public String longitude;
}
