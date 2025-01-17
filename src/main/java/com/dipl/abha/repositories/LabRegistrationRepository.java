package com.dipl.abha.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.LabRegistration;


@Repository
public interface LabRegistrationRepository extends JpaRepository<LabRegistration, Long> {

	List<LabRegistration> findByHfrIdAndIsDeletedFalse(String hfrId);
	
}
