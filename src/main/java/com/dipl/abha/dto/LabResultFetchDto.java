package com.dipl.abha.dto;

public interface LabResultFetchDto {
	
//	@Value("#{target.serviceid}")
	String getServiceId();
//	@Value("#{target.billno}")
	String getBillNo();
//	@Value("#{target.departmentname}")
	String getDepartmentName();
//	@Value("#{target.servicename}")
	String getServiceName();
//	@Value("#{target.testname}")
	String getTestName();
//	@Value("#{target.testresult}")
	String getTestResult();
//	@Value("#{target.units}")
	String getUnits();
//	@Value("#{target.remarks}")
	String getRemarks();
//	@Value("#{target.documentpath}")
	String getDocumentPath();
}
