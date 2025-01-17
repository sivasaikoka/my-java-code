package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.ABDMRequestMapping;

@Repository
public interface AbdmRequstMappingRepository extends JpaRepository<ABDMRequestMapping, String> {

	@Query(value = "select * from abdm_request_mapping where request_id = ?1 limit 1",nativeQuery = true)
	ABDMRequestMapping findByRequestIdLimit1(String requestId);

	@Query(value = "select * from abdm_request_mapping where consent_id = ?1 limit 1",nativeQuery = true)
	ABDMRequestMapping findByConsentIdLimit1(String requestId);
	
	@Query(value = "select * from abdm_request_mapping where txn_id = ?1 limit 1",nativeQuery = true)
	ABDMRequestMapping findByTxnIdLimit1(String requestId);

}
