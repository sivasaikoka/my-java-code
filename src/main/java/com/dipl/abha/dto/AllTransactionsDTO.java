package com.dipl.abha.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface AllTransactionsDTO {

	@Value("#{target.doctor_id}")
	Long getDoctorId();
	@Value("#{target.beneficiary_id}")
	Long getBeneficiaryId();
	@Value("#{target.consultation_id}")
	Long getConsultationId();
	@Value("#{target.order_no}")
	public String getOrderNo();
	@Value("#{target.payment_status}")
	Boolean getPaymentStatus();
	@Value("#{target.amount}")
	BigDecimal getAmount();
	@Value("#{target.transaction_date}")
	LocalDateTime getTransactionDate();
	@Value("#{target.patient_name}")
	public String getPatientname();
	@Value("#{target.call_status}")
	public String getCallStatus();
	@Value("#{target.payment_type}")
	public String getPaymentType();
	@Value("#{target.agent_id}")
	Long getAgentId();
	
		
}
