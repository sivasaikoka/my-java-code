package com.dipl.abha.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultFetchDtoC {
	private String serviceId;
	private String billNo;
	private String departmentName;
	private String serviceName;
	private String testName;
	private String testResult;
	private String units;
	private String documentPath;
	private String remarks;

}
