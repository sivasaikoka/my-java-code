package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPayload {
	
	 private Context context;
	 private Message message;

}
