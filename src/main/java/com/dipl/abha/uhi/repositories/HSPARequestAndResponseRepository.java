package com.dipl.abha.uhi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.uhi.entities.HSPARequestAndReponse;

@Repository
public interface HSPARequestAndResponseRepository extends JpaRepository<HSPARequestAndReponse, Long> {

}
