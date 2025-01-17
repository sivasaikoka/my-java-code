package com.dipl.abha.m2.userauthinitpayload;

import com.dipl.abha.annotations.NotEmpty;
import com.dipl.abha.annotations.RequesterType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requester{
	@RequesterType
    public String type;
    @NotEmpty
    public String id;
}

