package com.dipl.abha.m2.linkoninit;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinksOnInit {
	@Valid
	@NotBlank(message = "requestId should not be empty")
	private String requestId;
	private LocalDateTime timestamp;
	private String transactionId;
	@NotNull(message = "Link should not be null")
	private Link link;
	private Error error;
	private Resp resp;
//	@NotBlank(message = "hiuId should not be empty")
	private String hiuId;
	@JsonProperty("response")
	private Resp response;

}