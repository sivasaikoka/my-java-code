package com.dipl.abha.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.MasterFacility;


@Repository
public interface MasterFacilityRepository extends JpaRepository<MasterFacility, Long> {

	List<MasterFacility> findByHfrCodeAndIsDeletedFalse(String hfrCode);
	
}
