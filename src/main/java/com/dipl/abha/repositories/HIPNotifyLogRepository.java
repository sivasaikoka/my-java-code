package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIPNotifyLog;

@Repository
public interface HIPNotifyLogRepository extends JpaRepository<HIPNotifyLog, Long> {

	
	@Query(value = "select * from hip_notify_logs hnl where hnl.consent_request_code = ?1 and status = 'GRANTED'",nativeQuery =true)
	HIPNotifyLog getHealthIdByConsentId(String healthId);
}
