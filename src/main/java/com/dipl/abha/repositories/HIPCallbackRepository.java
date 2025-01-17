package com.dipl.abha.repositories;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIPCallback;

@Repository
public interface HIPCallbackRepository extends JpaRepository<HIPCallback, Long> {

	HIPCallback findFirstByConsentRequestCodeAndHipRequestTypeIdOrderByCreatedOnDesc(String consent, int i);

	HIPCallback findFirstByPatientRefNoAndHipRequestTypeIdOrderByIdDesc(String consent, int i);

	@Query(value = "SELECT cast(hcb.response_json as text),hr.interaction_id  from hip_call_back hcb inner join\r\n"
			+ "hip_request hr on hr.request_code = hcb.request_code\r\n"
			+ "where hcb.txn_code = ?1 and hcb.hip_request_type_id = ?2",nativeQuery = true)
	Map<String,Object> findFirstByTxnCodeNoAndHipRequestTypeIdOrderByIdDesc(String transactionId, int i);
	
	
	@Query(value = "select * from hip_call_back hcb where request_code = ?1 limit 1",nativeQuery = true)
	HIPCallback findByRequestCodeLimit1(String requestId);

	@Query(value = "update hip_call_back set consent_request_code =?1 where request_code = ?2",nativeQuery = true)
	@Transactional
	@Modifying
	void updatePatientMobileNoInHIPCallBack(String mobileNo, String requestId);
}
