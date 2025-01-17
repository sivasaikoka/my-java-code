package com.dipl.abha.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.patient.entities.Center;


@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {

	Optional<Center> findByIdAndOrgId(Long centerId, Long orgId);


}
