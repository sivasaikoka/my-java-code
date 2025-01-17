package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.patient.entities.PatientAddress;

@Repository
public interface PatientAddressRepository extends JpaRepository<PatientAddress, Long> {

	
	@Query(value = "select * from patient_address pa where pa.patient_id =?1 limit 1",nativeQuery = true)
	PatientAddress findByPatientId(int id);

}
