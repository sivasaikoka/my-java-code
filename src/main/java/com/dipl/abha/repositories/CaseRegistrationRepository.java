package com.dipl.abha.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.CaseRegistrationEntity;

@Repository
public interface CaseRegistrationRepository extends JpaRepository<CaseRegistrationEntity, Long> {

	
	public List<CaseRegistrationEntity> findByAbhaNoAndIsDeletedFalse(String abhaNO);

	public List<CaseRegistrationEntity> findByMobileNoAndIsDeletedFalse(String mobileNo);
	
	@Query(value = "select * from case_registration where id = ?1 and is_deleted = false", nativeQuery = true)
	public Optional<CaseRegistrationEntity> findById(Long id);
	
	@Query(value = "select * from case_registration where is_deleted = false and abha_no =?1 limit 1", nativeQuery = true)
	public Optional<CaseRegistrationEntity> findByAbhaNoAndIsDeletedFalseOPT(String abhaNO);
	
	@Query(value = "select * from case_registration where is_deleted = false and abha_address =?1 limit 1", nativeQuery = true)
	public List<CaseRegistrationEntity> findByAbhaAddressAndIsDeletedFalseOPT(String abhaNO);

}
