package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDto {

	private MessageAck message;
    private Error error;
    private Integer statusCodeValue;
    public ExceptionBody body;
    public String statusCode;
}
