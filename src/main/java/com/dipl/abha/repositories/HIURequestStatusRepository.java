package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIURequestStatus;

@Repository
public interface HIURequestStatusRepository extends JpaRepository<HIURequestStatus, Long>{

}
