package com.dipl.abha.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.SurveyDetails;

@Repository
public interface SurveyDetailsRepository extends JpaRepository<SurveyDetails, Long> {

	
	@Query(value = "select * from survey_details sd where sd.case_id in (?1) and \r\n"
			+ "sd.incident_no not in (select ccl.patient_interaction_id  from care_context_logs ccl)",nativeQuery = true)
	List<SurveyDetails> findByCaseId(Long caseId);

	
	@Query(value = "select concat('CASE-ID - ', cr.case_no,' - ',cr.id) as patient_ref_number,\r\n"
			+ "concat(cr.first_name,' ',cr.middle_name,' ',cr.last_name) as patient_display,\r\n"
			+ "sd.incident_no as care_context_ref, concat(ccs.survey_name,' (',ccs.survey_conducted_from,')-(',ccs.survey_conducted_to,') ' ,mf.facility_name,' - ',mf.hfr_code) as care_context_display \r\n"
			+ "from case_registration cr \r\n"
			+ "inner join survey_details sd ON sd.case_id = cr.id \r\n"
			+ "inner join campaign_config_survey ccs on ccs.id = sd.campaign_id\r\n"
			+ "inner join users u on u.id = sd.created_by  \r\n"
			+ "inner join users_location_mapping ulm on ulm.user_id = u.id \r\n"
			+ "inner join master_facility mf on mf.id = ulm.facility_id \r\n"
			+ "join reports r  on r.incident_no  = sd.incident_no  \r\n"
			+ "where cr.id in(?1) \r\n"
			+ "and sd.incident_no \r\n"
			+ "not in (select ccl.patient_interaction_id  from care_context_logs ccl) and cr.is_deleted =false\r\n"
			+ "group by 1,2,3,4"
			+ "		",nativeQuery = true)
	List<Map<String,Object>> getCareContexts(Long patientId);
	
	
	@Query(value = "select concat('CASE-ID - ', cr.case_no,' - ',cr.id) as patient_ref_number,\r\n"
			+ "concat(cr.first_name,' ',cr.middle_name,' ',cr.last_name) as patient_display,\r\n"
			+ "sd.incident_no as care_context_ref, concat(ccs.survey_name,' (',ccs.survey_conducted_from,')-(',ccs.survey_conducted_to,') ',mf.facility_name,' - ',mf.hfr_code) as care_context_display from case_registration cr \r\n"
			+ "inner join survey_details sd ON sd.case_id = cr.id \r\n"
			+ "inner join campaign_config_survey ccs on ccs.id = sd.campaign_id\r\n"
			+ "inner join users u on u.id = sd.created_by  \r\n"
			+ "inner join users_location_mapping ulm on ulm.user_id = u.id \r\n"
			+ "inner join master_facility mf on mf.id = ulm.facility_id \r\n"
			+ "join reports r  on r.incident_no  = sd.incident_no  \r\n"
			+ "where cr.is_deleted =false \r\n"
			+ "and sd.incident_no in (?1)\r\n"
			+ "group by 1,2,3,4",nativeQuery = true)
	List<Map<String,Object>> getCareContextByInsidentId(Set<Long> incidentId);
	
	
	@Query(value = "select concat('CASE-ID - ', cr.case_no,' - ',cr.id) as patient_ref_number,\r\n"
			+ "concat(cr.first_name,' ',cr.middle_name,' ',cr.last_name) as patient_display,\r\n"
			+ "sd.incident_no as care_context_ref, concat(ccs.survey_name,' (',ccs.survey_conducted_from,')-(',ccs.survey_conducted_to,') ',mf.facility_name,' - ',mf.hfr_code) as care_context_display from case_registration cr \r\n"
			+ "inner join survey_details sd ON sd.case_id = cr.id \r\n"
			+ "inner join campaign_config_survey ccs on ccs.id = sd.campaign_id\r\n"
			+ "inner join users u on u.id = sd.created_by  \r\n"
			+ "inner join users_location_mapping ulm on ulm.user_id = u.id \r\n"
			+ "inner join master_facility mf on mf.id = ulm.facility_id \r\n"
			+ "join reports r  on r.incident_no  = sd.incident_no  \r\n"
			+ "where cr.is_deleted =false \r\n"
			+ "and sd.incident_no in (?1) group by 1,2,3,4",nativeQuery = true)
	List<Map<String,Object>> getPatientAndCareContextByIncidentId(Set<Long> incidentId);
	
	
	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ "cd.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from survey_details sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n"
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join clinical_details cd on cd.incident_no = sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and cd.is_deleted = false and cr.is_deleted = false and sd.incident_no in (?1) and mft.id=1 \r\n"
			+ " and cd.created_on between ?2 and ?3)a",nativeQuery=true)
	List<Map<String,Object>> getPerscriptionWithDate(Set<Long> incidentIds,LocalDateTime fromDate, LocalDateTime toDate);
	
	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ " cd.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from survey_details sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n" 
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join clinical_details cd on cd.incident_no = sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and cd.is_deleted = false and cr.is_deleted = false and sd.incident_no in (?1) and mft.id=1)a",nativeQuery=true)
	List<Map<String,Object>> getPerscription(Set<Long> incidentIds);
	
	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ " r.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from survey_details sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n"
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "inner join sample_collection scl on scl.incident_no = sd.incident_no \r\n"
			+ "inner join sample_result sr on scl.id = sr.sample_collection_id \r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and   cr.is_deleted = false  and scl.incident_no in (?1) and \r\n"
			+ "r.created_on between ?2 and ?3  and mft.id=2)a",nativeQuery=true)
	List<Map<String,Object>> getLabReportsWithDate(Set<Long> incidentIds,LocalDateTime fromDate, LocalDateTime toDate) ;

	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ " r.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from survey_details sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n"
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "inner join sample_collection scl on scl.incident_no = sd.incident_no \r\n"
			+ "inner join sample_result sr on scl.id = sr.sample_collection_id \r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and   cr.is_deleted = false  and scl.incident_no in (?1) and mft.id=2)a",nativeQuery=true)
	List<Map<String,Object>> getLabReports(Set<Long> incidentIds);
	
	
	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ " r.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from sample_collection sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n"
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "inner join sample_collection scl on scl.incident_no = sd.incident_no \r\n"
			+ "inner join sample_result sr on scl.id = sr.sample_collection_id \r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and cr.is_deleted = false and scl.incident_no in (?1) \r\n"
			+ "and mft.id=2)a",nativeQuery = true)
	List<Map<String,Object>> getAllLabReports(Set<Long> incidentIds);

	
	
	
	@Query(value = "select distinct * from (select r.doc_ref_id, u.name as anm_name, concat(cr.first_name ,' ',cr.middle_name ,' ',cr.last_name ) \r\n"
			+ "as patient_name,\r\n"
			+ " r.created_on ,r.file_path,mft.\"name\" ,sd.incident_no \r\n"
			+ "from sample_collection sd \r\n"
			+ "inner join users u on sd.created_by = u.id \r\n"
			+ "inner join reports r on r.incident_no=sd.incident_no \r\n"
			+ "inner join master_file_type mft on mft.id = r.file_type_id \r\n"
			+ "inner join case_registration cr on sd.case_id = cr.id\r\n"
			+ "inner join sample_collection scl on scl.incident_no = sd.incident_no \r\n"
			+ "inner join sample_result sr on scl.id = sr.sample_collection_id \r\n"
			+ "where r.is_deleted=false and sd.is_deleted=false and u.is_deleted =false \r\n"
			+ "and cr.is_deleted = false and scl.incident_no in (?1) \r\n"
			+ "and mft.id=2 and r.created_on between ?2 and ?3 )a",nativeQuery = true)
	List<Map<String,Object>> getAllLabReportsWithDate(Set<Long> incidentIds,LocalDateTime fromDate, LocalDateTime toDate) ;

	
	@Query(value = "select concat('CASE-ID - ', cr.case_no,' - ',cr.id) as patient_ref_number,\r\n"
			+ "concat(cr.first_name,' ',cr.middle_name,' ',cr.last_name) as patient_display,\r\n"
			+ "sc.incident_no as care_context_ref, lr.lab_name as care_context_display \r\n"
			+ "from case_registration cr \r\n"
			+ "inner join sample_collection sc on sc.case_id = cr.id \r\n"
			+ "inner join sample_result sr on sr.sample_collection_id = sc.id \r\n"
			+ "inner join master_sample_type mst on mst.id = sc.sample_type_id\r\n"
			+ "inner join lab_registration lr on lr.id = sc.lab_id \r\n"
			+ "inner join reports r on r.incident_no = sc.incident_no \r\n"
			+ "where r.incident_no in (?1) and r.file_type_id = 2  group by 1,2,3,4",nativeQuery=true)
	List<Map<String,Object>> getLatestCareContextLab(Set<Long> incidentIds);

}
