package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIPDataPushLog;


@Repository
public interface HIPDataPushLogsRepository extends JpaRepository<HIPDataPushLog, Long> {

}
