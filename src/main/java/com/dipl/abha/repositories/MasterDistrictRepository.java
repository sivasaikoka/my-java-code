package com.dipl.abha.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dipl.abha.entities.MasterDistrict;


@Repository
public interface MasterDistrictRepository extends JpaRepository<MasterDistrict, Integer> {

	@Query(value = "select * from master_district where id = ?1 and is_deleted = false", nativeQuery = true)
	public Optional<MasterDistrict> findById(Integer id);

	@Query(value = "select * from master_district where is_deleted = false ", nativeQuery = true)
	public List<MasterDistrict> findAll();

	@Modifying
	@Transactional
	@Query(value = "update master_district set is_deleted=true where id = ?1 ", nativeQuery = true)
	public void deleteByIdSoft(Integer id);

	@Query(value = "select district_code from master_district where id = ?1 and is_deleted = false", nativeQuery = true)
	public String getDistrictCodeById(Integer districtId);
}
