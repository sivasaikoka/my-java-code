package com.dipl.abha.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.CallBackApiResponse;
import com.fasterxml.jackson.databind.JsonNode;

@Repository
public interface CallBackApiResponseRepository extends JpaRepository<CallBackApiResponse, Long> {
	@Query(value = "select * from call_back_api_response where id =?1 and is_active=true ", nativeQuery = true)
	public CallBackApiResponse findById(String backApiResponse);

	@Query(value = "select * from call_back_api_response cbar where request_id  = ?1 \r\n"
			+ "and api_type ilike %?2% order by created_on desc limit 1\r\n", nativeQuery = true)
	public CallBackApiResponse findByRequestIdAndAPIType(String healthId, String apiType);

	@Query(value = "select * from call_back_api_response cbar where health_id = ?1 \r\n"
			+ "and api_type like %?2% order by created_on desc limit 1", nativeQuery = true)
	public CallBackApiResponse findByHealthIdAndAPIType(String healthId, String apiType);

	@Query(value = "update public.call_back_api_response set request = ?4, api_type = ?3 where api_type = ?2 and request_id =?1", nativeQuery = true)
	@Modifying
	@Transactional
	public void updateQuery(String requestId, String apiType, String latestApiType, JsonNode onDiscover);

	@Query(value = "select * from call_back_api_response cbar where cbar.concentid = ?1 order by id desc limit 1", nativeQuery = true)
	public CallBackApiResponse findByConcentId(String consent);
	

	@Query(value = "select * from call_back_api_response cbar where cbar.concentid = ?1 and api_type = 'NOTIFY,ON-NOTIFY' order by id desc limit 1", nativeQuery = true)
	public CallBackApiResponse findNotifyByConcentId(String consent);

	@Query(value = "select * from call_back_api_response cbar where cbar.patient_ref_no = ?1 and api_type = ?2 order by id desc limit 1", nativeQuery = true)
	public CallBackApiResponse findByPatientRefNoAndApiType(String patientRef, String apiType);
	
	@Query(value = "select * from call_back_api_response cbar where cbar.concentid = ?1 and cbar.api_type = ?2 order by id desc limit 1", nativeQuery = true)
	public CallBackApiResponse findByConcentIdAndApiType(String consent,String apiType);
	
	@Query(value = "select * from call_back_api_response cbar where transaction_id = ?1 limit 1",nativeQuery = true)
	public CallBackApiResponse findByTransactionIdAndApiType(String transactionId);
}