package com.dipl.abha.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.dto.LabResultFetchDto;
import com.dipl.abha.entities.HIUResponse;

@Repository
public interface HIUResponseRepository extends JpaRepository<HIUResponse,Long>{

	List<HIUResponse> findByConsentArtefactCode(String consentArtifactId);

	@Query(value = "select * from hiu_response hr where hr.txn_code = ?1\r\n"
			+ "and (hr.consent_request_code is not null and hr.consent_request_code != '')\r\n"
			+ "and (hr.consent_artefact_code  is not null and hr.consent_artefact_code != '')\r\n"
			+ "and hr.hiu_request_type_id = 8", nativeQuery = true)
	List<HIUResponse> findByTransactionId(String transactionId);

	@Query(value = "select * from hiu_response hr where hr.request_code = ?1 and hr.hiu_request_type_id = 10 order by id desc limit 1", nativeQuery = true)
	HIUResponse findByRequestCodeLimit1(String requestId);

	@Query(value = "select consent_request_code from hiu_response hr where hr.hiu_request_type_id =3 \r\n"
			+ "and consent_request_code  is not null and consent_artefact_code =?1 limit 1", nativeQuery = true)
	String getConsentRequestIdByConsentArtifactCode(String consentArtifaceCode);

	@Query(value = "	SELECT service_id AS serviceId, bill_no AS billNo, department_name AS departmentName, \n"
			+ "service_name AS serviceName, testname AS testName, testresult AS testResult, \n"
			+ "units, coalesce(remarks|| '','') as remarks , CONCAT('/vidmedfiles/BENEFICIARY/', bd.beneficiary_id, \n"
			+ "CONCAT('/', mft2.file_type, '/', bd.document_path)) AS documentPath\n"
			+ "FROM nhw.vw_rpt_lab_result_done tmp \n"
			+ "LEFT JOIN beneficiary_documents bd ON bd.lab_investigation_id = tmp.service_id \n"
			+ "AND bd.resultno = tmp.resultno AND bd.file_type_id = 56 \n"
			+ "LEFT JOIN master.master_file_type mft2 ON mft2.id = bd.file_type_id \n"
			+ "WHERE (bill_no = 'LBILL-767-2024286' OR '-1' = 'LBILL-767-2024286' )  ", nativeQuery = true)
	List<LabResultFetchDto> findLabResults(String billNo);

}
