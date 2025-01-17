package com.dipl.abha.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dipl.abha.entities.MasterState;


@Repository
public interface MasterStateRepository extends JpaRepository<MasterState, Integer> {

	@Query(value = "select * from master_state where id = ?1 and is_deleted = false", nativeQuery = true)
	public Optional<MasterState> findById(Integer id);

	@Query(value = "select * from master_state where is_deleted = false ", nativeQuery = true)
	public List<MasterState> findAll();

	@Modifying
	@Transactional
	@Query(value = "update master_state set is_deleted=true where id = ?1 ", nativeQuery = true)
	public void deleteByIdSoft(Integer id);

	@Query(value = "select state_code from master_state where id = ?1 and is_deleted = false ", nativeQuery = true)
	public String getStateCodeById(Integer stateId);
}
