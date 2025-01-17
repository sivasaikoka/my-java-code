package com.dipl.abha.repositories;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.ABHAlogs;

@Repository
public interface ABHALogsRepository extends JpaRepository<ABHAlogs, Long> {
	List<ABHAlogs> findByHealthIdAndAbhaStatusIdAndIsDeletedIsFalseOrderByCreatedOnDesc(String healthId, int i);

	@Query(value = "update abha_logs set is_deleted = true, is_active = false where id = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	void updateABHALogToFalse(Long id);
}