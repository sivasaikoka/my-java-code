package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnCancelAudit {

	
	  private Context context;
	    private Message message;
}
