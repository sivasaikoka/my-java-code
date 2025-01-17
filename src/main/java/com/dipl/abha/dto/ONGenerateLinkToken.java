package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ONGenerateLinkToken{
    public String abhaAddress;
    public String linkToken;
    public Response response;
  	public Error error;
}
