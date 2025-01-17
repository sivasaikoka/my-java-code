package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIPRequest;

@Repository
public interface HIPRequestRepository extends JpaRepository<HIPRequest, Long> {

	HIPRequest findByRequestCodeAndHipRequestTypeId(String requestId, int i);

	HIPRequest findFirstByConsentArtefactCodeAndHipRequestTypeIdOrderByCreatedOnDesc(String consent, int i);

	@Query(value = "select * from hip_request hcb where hcb.hip_request_type_id = 10 "
			+ "and hcb.txn_code = (select txn_code from hip_call_back hcb2 where hcb2.hip_request_type_id = 11 "
			+ "and patient_ref_no = ?1 order by hcb2.id desc limit 1) order by hcb.id desc limit 1", nativeQuery = true)
	HIPRequest findOnDiscover(String patienRef);

}
