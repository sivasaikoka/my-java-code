package com.dipl.abha.repositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.CareContextLogs;

@Repository
public interface CareContextLogsRepository extends JpaRepository<CareContextLogs, Long> {

	List<CareContextLogs> findByBeneficiaryId(Long id);
 
	@Query(value = "select sd.incident_no as care_context_ref, concat(ccs.survey_name,' (',ccs.survey_conducted_from,')-(',ccs.survey_conducted_to,') ' ,mf.facility_name,' - ',mf.hfr_code) as care_context_display from case_registration cr \r\n"
			+ "inner join survey_details sd ON sd.case_id = cr.id \r\n"
			+ "inner join campaign_config_survey ccs on ccs.id = sd.campaign_id\r\n"
			+ "inner join users u on u.id = sd.created_by  \r\n"
			+ "inner join users_location_mapping ulm on ulm.user_id = u.id \r\n"
			+ "inner join master_facility mf on mf.id = ulm.facility_id \r\n"
			+ "where sd.incident_no in (?1)\r\n"
			+ "group by 1,2", nativeQuery = true)
	List<Map<String, Object>> getCCIDandCCDN(Set<Long> incidentNo);

	@Query(value = "UPDATE reports set direct_auth_status = ?1 where incident_no = ?2 and file_type_id = 1", nativeQuery = true)
	@Transactional
	@Modifying
	void updateDirectAuthStatus(String directauth, Long incidentId);
	
	@Query(value = "UPDATE reports set is_carecontext_added = true where incident_no in (?1) and file_type_id = 1", nativeQuery = true)
	@Transactional
	@Modifying
	void updateIncidentStatus(Set<Long> incidentId);
}
