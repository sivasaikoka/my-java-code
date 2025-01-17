package com.dipl.abha.uhi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.uhi.entities.EUARequestAndResponse;

@Repository
public interface EUARequestAndResponseRepository extends JpaRepository<EUARequestAndResponse, Long> {

	@Query(value="select * from eua_request_response where api_type=?1 limit 1",nativeQuery = true)
	List<EUARequestAndResponse> FindByApiType(String apiType);

}
