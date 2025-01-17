package com.dipl.abha.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HipLinkTokenEntity;


@Repository
public interface HipLinkTokenRepository extends JpaRepository<HipLinkTokenEntity, String> {

	@Query(value = "select * from hip_link_token where hip_id=?1 and patient_id =?2 ", nativeQuery = true)
	List<HipLinkTokenEntity> getByHipIdAndPatientId(String hipid, Long patientid);

}