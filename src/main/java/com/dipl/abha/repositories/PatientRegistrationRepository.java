package com.dipl.abha.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.dto.PatientRegistrationDTO;
import com.dipl.abha.patient.entities.PatientDemography;

@Repository
public interface PatientRegistrationRepository extends JpaRepository<PatientDemography, Long> {

	@Query(value = "select * from vw_patient_details where status = 1 and (primaryaddress = 'Y' or isemergency = 'Y') "
			+ " and (mrno= ?1 :::: text or '-1' :::: text =?1 :::: text) "
			+ " and (firstname ilike %?2% :::: text or '-1' :::: text ilike %?2% :::: text) "
			+ " and (lastname ilike ?3 :::: text or '-1' :::: text =?3 :::: text) "
			+ " and (to_char(dob,'yyyymmdd'):::: text = ?4 or '-1' = ?4) " + " and (age = ?5 or -1 = ?5) "
			+ " and (mobileno=?6 :::: text or '-1' :::: text =?6 :::: text)"
			+ " and (area = ?7 :::: text or '-1' :::: text =?7 :::: text) "
			+ " and (companyname = ?8 :::: text or '-1' :::: text =?8 :::: text)"
			+ " and (companyschemename = ?9 :::: text or '-1' :::: text =?9 :::: text)", nativeQuery = true)
	public Page<List<Map<String, Object>>> findAllOPCreditNotes(Pageable pageable, String mrNo, String firstName,
			String lastName, String dob, Integer age, String mobileNo, String area, String companyName,
			String companySchemeName);

	@Query(value = "select d.* from vw_patient_registration_details d where (patient_id = ?1 or -1=?1) ", nativeQuery = true)
	public List<Map<String, Object>> findByPatientId(Long id);

	@Query(value = "select d.* from vw_patient_registration_details d where (mrn_no = ?1 or '-1'=?1) ", nativeQuery = true)
	public List<Map<String, String>> findByMrNo(String mrnNumber);

	@Query(value = "SELECT UHID FROM PATIENT_DEMOGRAPHY WHERE uhid =?1  AND org_id =?2 ", nativeQuery = true)
	public List<Map<String, Object>> findCheckAvailabilityByUHId(String uhId, Long orgId);

	@Query(value = "SELECT MRN_NO FROM PATIENT_DEMOGRAPHY WHERE PATIENT_FIRST_NAME= ?1   AND PATIENT_LAST_NAME= ?2  AND org_id =?3", nativeQuery = true)
	public List<Map<String, Object>> findCheckAvailabilityByMRN(String firstName, String lastName, Long orgId);

	@Query(value = "SELECT MRN_NO FROM PATIENT_DEMOGRAPHY WHERE identification_card = ?1  AND identification_type_id= ?2 AND org_id =?3", nativeQuery = true)
	public List<Map<String, Object>> findCheckAvailabilityByMRNIden(String identificationCard,
			Long identificationTypeId, Long orgId);

	@Query(value = "select * from vw_ipadt_ip_patient_search "
			+ "where (ipno =?1 :::: text or '-1' :::: text = ?1 :::: text)  "
			+ "and (mrnno= ?2 :::: text or '-1' :::: text = ?2 :::: text)   "
			+ "and (firstname = ?3 :::: text or '-1' :::: text = ?3 :::: text)  "
			+ "and (lastname = ?4 :::: text or '-1' :::: text = ?4 :::: text)  "
			+ "and (to_char(dob,'yyyymmdd') :::: text = ?5 or '-1' = ?5 )  " + "and (age = ?6 or -1 = ?6)   "
			+ "and (mobile= ?7 :::: text or '-1' :::: text =?7 :::: text)  "
			+ "and (area  = ?8 :::: text or '-1' :::: text = ?8 :::: text)   "
			+ "and (company_name = ?9 :::: text or '-1' :::: text = ?9 :::: text)  "
			+ "and (companyscheme_name = ?10 :::: text or '-1' :::: text = ?10 :::: text)  "
			+ "and (doctorname= ?11 :::: text or '-1' :::: text = ?11 :::: text)", nativeQuery = true)
	public Page<List<Map<String, Object>>> findAllOpBedPatients(Pageable pageable, String ipNo, String mrNo,
			String firstName, String lastName, String dob, int age, String mobileNo, String area, String companyName,
			String companySchemeName, String consultingDoctor);

	@Query(value = "select * from USP_GETTRANSFERDETAILS(?1)", nativeQuery = true)
	public List<Map<String, Object>> getIpDetails(String ipNo);

	@Query(value = "select * from getautogeneratenumbers(?1,?2,?3,?4,?5)", nativeQuery = true)
	public String generateMrno(Long orgId, Long centerId, String mrstatic, LocalDateTime localDateTime,
			String lastName);

	@Query(value = "SELECT CREDIT_COMPANY_SCHEME.COMPANYSCHEME_NAME as SCHEME,COMPANY_SCHEME_ID,MEMBER_NUMBER,MEMBER_NAME,\n"
			+ "PRINCIPLE_APPLICANT,OUTPATIENT,INPATIENT,DENTAL,OPTICAL,MATERNITY,ANC,CREDIT_COMPANY_SCHEME_MEMBER.LIMIT,\n"
			+ "STARTDATE,ENDDATE,COMPANY_ID,CREDIT_COMPANY_SCHEME_MEMBER.CMPSCHMEMBER_ID,CREDIT_COMPANY_SCHEME_MEMBER.STATUS,\n"
			+ "CASE WHEN PD.PTCRDTL_ID IS NULL THEN '1'  ELSE 0 END AS PTCRDTL_ID FROM CREDIT_COMPANY_SCHEME_MEMBER \n"
			+ "LEFT OUTER JOIN CREDIT_COMPANY_SCHEME ON CREDIT_COMPANY_SCHEME_MEMBER.COMPANY_SCHEME_ID = \n"
			+ "CREDIT_COMPANY_SCHEME.COMPANYSCHEME_ID \n"
			+ "LEFT JOIN PATIENT_CREDIT_DETAILS PD ON PD.CMPSCHMEMBER_ID= CREDIT_COMPANY_SCHEME_MEMBER.CMPSCHMEMBER_ID \n"
			+ "WHERE COMPANY_SCHEME_ID =?1", nativeQuery = true)
	public List<Map<String, Object>> getDetailsBySchemeId(Long schemeId);

	@Query(value = "select mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			where (pd.patient_id=?1 or -1=?1) and (odc.dcid = ?2 or -1=?2) and odc.dcid not in \r\n"
			+ "			(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextId(Long patientId, Long interactionId);

	@Query(value = "select mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			where odc.dcid in(?1) and odc.dcid not in \r\n"
			+ "			(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getCareContextsByInteractionIds(Set<Long> interatcionIds);

	@Query(value = "select mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			where odc.dcid in(?1) and odc.dcid::::varchar not in \r\n"
			+ "			(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getCareContextsByInteractionIdsNew(Set<Long> interatcionIds);

	@Transactional
	@Modifying
	@Query(value = "update op_doctor_consultation set direct_auth_status=?1 where dcid=?2", nativeQuery = true)
	public void updateDirectAuthStatus(String directAuthStatus, long parseLong);

	@Transactional
	@Modifying
	@Query(value = "update op_doctor_consultation set iscare_context_linked =?1 where dcid=?2", nativeQuery = true)
	public void updateCareContextRecord(boolean b, Long interactionsId);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob) as year_of_birth,"
			+ "pd.national_health_id, pd.national_health_number,pd.patient_first_name as first_name,pd.patient_middle_name as middle_name, \r\n"
			+ "pd.patient_last_name as last_name, pd.referral_id from patient_demography pd inner join patient_address pa on pd.patient_id = pa.patient_id \r\n"
			+ "where (pd.national_health_id = ?1 or pd.national_health_number= ?2) limit 1", nativeQuery = true)
	public PatientRegistrationDTO findByHealthIdorHealthNumber(String ndhmHealthId, String healthNumber);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob) as year_of_birth,"
			+ "pd.national_health_id, pd.national_health_number,pd.patient_first_name as first_name,pd.patient_middle_name as middle_name, \r\n"
			+ "pd.patient_last_name as last_name from patient_demography pd inner join patient_address pa on pd.patient_id = pa.patient_id \r\n"
			+ "where (pd.national_health_id = ?1) limit 1", nativeQuery = true)
	public PatientRegistrationDTO findByHealthId(String ndhmHealthId);

	@Query(value = "select * from patient_demography pd where pd.national_health_id =?1 limit 1\r\n"
			+ "", nativeQuery = true)
	public PatientDemography findByHealthIdNew(String ndhmHealthId);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob) as year_of_birth,\r\n"
			+ "pd.national_health_id, pd.national_health_number,pd.patient_first_name as first_name,pd.patient_middle_name as middle_name, \r\n"
			+ "pd.patient_last_name as last_name from patient_demography pd inner join patient_address pa on pd.patient_id = pa.patient_id \r\n"
			+ "where (pd.national_health_id = ?1) and (pa.mobile_no = ?2) limit 1", nativeQuery = true)
	public PatientRegistrationDTO findByHealthIdAndMobileNo(String healthId, String mobileNo);

	@Query(value = "select pd.* from patient_demography pd inner join patient_address pa on pa.patient_id = pd.patient_id \r\n"
			+ "where pd.national_health_id = ?1 and pa.mobile1 = ?2 limit 1", nativeQuery = true)
	PatientDemography findByHealthIdAndMobileNoReturnDemo(String healthId, String mobileNo);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob)\r\n"
			+ "			as year_of_birth,pd.national_health_id, pd.national_health_number,pd.is_abha_linked,pd.patient_first_name as first_name, \r\n"
			+ "			pd.patient_middle_name as middle_name, pd.patient_last_name as last_name from patient_demography pd\r\n"
			+ "			inner join patient_address pa on pd.patient_id = pa.patient_id where pa.mobile1  =?1", nativeQuery = true)
	public List<PatientRegistrationDTO> findByMobile(String newMobile);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob)\r\n"
			+ "			as year_of_birth,pd.national_health_id, pd.national_health_number,pd.is_abha_linked,pd.patient_first_name as first_name, \r\n"
			+ "			pd.patient_middle_name as middle_name, pd.patient_last_name as last_name from patient_demography pd\r\n"
			+ "			inner join patient_address pa on pd.patient_id = pa.patient_id where pa.mobile1  =?1 limit 1", nativeQuery = true)
	public PatientRegistrationDTO findByMobileNew(String newMobile);

	@Query(value = "select mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			where (odc.dcid in(?1) or (-1) in (?1))  and\r\n"
			+ "			odc.created_date::::date between  ?2 and ?3", nativeQuery = true)
	public List<Map<String, Object>> getCareContextsByInteractionIdsAndDate(Set<Long> interatcionIds,
			LocalDate fromDate, LocalDate toDate);

	@Query(value = "SELECT distinct case when (br.middle_name !='' and br.middle_name is not null) then \r\n"
			+ "concat(br.first_name,' ',br.middle_name,' ',br.last_name) else concat(br.first_name,' ',br.last_name) end as patient_display,\r\n"
			+ "br.id as patient_id, br.id as patient_referencenumber,\r\n"
			+ "lob.bill_no as careContexts_referenceNumber ,\r\n"
			+ "concat('RESULT DATE ', TO_CHAR(lob.bill_date :: DATE, 'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "from  beneficiary_registration br\r\n"
			+ "join lab_op_billing lob on lob.case_no=br.id and is_self=true\r\n"
			+ "left join beneficiary_member_details bmd on bmd.id=lob.case_no and is_self=false\r\n"
			+ "join lab_op_billing_details lobd ON lobd.opbill_id = lob.id\r\n"
			+ "join lab_op_dc_diagnostics lobdd ON lobdd.opbill_dtls_id = lobd.id\r\n"
			+ "join lab_result lr on lr.op_bill_id=lob.id\r\n"
			+ "join (select beneficiary_id,document_path  from (select beneficiary_id, document_path,\r\n"
			+ "row_number() over (partition by beneficiary_id order by created_on desc)rn from retail.beneficiary_documents where file_type_id=53\r\n"
			+ ")z where rn =1)aa on aa.beneficiary_id=lob.case_no\r\n"
			+ "left join retail.beneficiary_member_documents bmdd on bmdd.beneficiary_member_id=bmd.id\r\n"
			+ " join retail.lab_registration lrr on lrr.user_id=lob.center_id \r\n"
			+ " where lob.bill_no ::::varchar in (?1) and lob.bill_no not in (select ccl.patient_interaction_id from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfLab(String string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,\r\n"
			+ "						pd.patient_id,\r\n"
			+ "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM LAB_RD_ORDER_BOOKING LROB\r\n"
			+ "			LEFT JOIN PATIENT_DEMOGRAPHY PD ON LROB.PATIENT_ID=PD.PATIENT_ID\r\n"
			+ "			INNER JOIN LAB_RD_ORDER_BOOKING_DETAIL LROBD ON LROB.LB_RD_ORDER_ID=LROBD.LB_RD_ORDER_ID\r\n"
			+ "			INNER JOIN SERVICES S ON LROBD.TEST_ID = S.SERVICE_ID \r\n"
			+ "			LEFT JOIN LAB_RESULT LR ON LROBD.RESULT_ID=LR.RESULT_ID \r\n"
			+ "			where LROBD.RESULT_ID is not null and result_no is not null and LR.result_no \r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl) and (lr.result_no in(?1))", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfLabByList(Set<String> string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,\r\n"
			+ "						pd.patient_id,\r\n"
			+ "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM LAB_RD_ORDER_BOOKING LROB\r\n"
			+ "			LEFT JOIN PATIENT_DEMOGRAPHY PD ON LROB.PATIENT_ID=PD.PATIENT_ID\r\n"
			+ "			INNER JOIN LAB_RD_ORDER_BOOKING_DETAIL LROBD ON LROB.LB_RD_ORDER_ID=LROBD.LB_RD_ORDER_ID\r\n"
			+ "			INNER JOIN SERVICES S ON LROBD.TEST_ID = S.SERVICE_ID \r\n"
			+ "			LEFT JOIN LAB_RESULT LR ON LROBD.RESULT_ID=LR.RESULT_ID \r\n"
			+ "			where LROBD.RESULT_ID is not null and result_no is not null and (lr.result_no in(?1))", nativeQuery = true)
	public List<Map<String, Object>> getCareContextOfLab(Set<String> string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,\r\n"
			+ "						pd.patient_id,LROB.created_date as consultation_date,\r\n"
			+ "			LR.result_no as careContexts_referenceNumber,lr.result_id,'LAB' as report_type,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM LAB_RD_ORDER_BOOKING LROB\r\n"
			+ "			LEFT JOIN PATIENT_DEMOGRAPHY PD ON LROB.PATIENT_ID=PD.PATIENT_ID\r\n"
			+ "			INNER JOIN LAB_RD_ORDER_BOOKING_DETAIL LROBD ON LROB.LB_RD_ORDER_ID=LROBD.LB_RD_ORDER_ID\r\n"
			+ "			INNER JOIN SERVICES S ON LROBD.TEST_ID = S.SERVICE_ID \r\n"
			+ "			LEFT JOIN LAB_RESULT LR ON LROBD.RESULT_ID=LR.RESULT_ID \r\n"
			+ "			where LROBD.RESULT_ID is not null and result_no is not null and (lr.result_no in (?1))\r\n"
			+ "			and LR.created_date ::::date between ?2 and ?3", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfLabByDate(Set<String> interatcionIds, LocalDate fromDate,
			LocalDate toDate);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,			\r\n"
			+ "			pd.patient_id,\r\n" + "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM\r\n" + "			    LAB_RD_ORDER_BOOKING_DETAIL LROBD  \r\n"
			+ "			    LEFT JOIN rd_results LR ON LROBD.RESULT_ID=LR.RESULT_ID\r\n"
			+ "			    JOIN RD_TESTS RTS ON LROBD.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN SERVICES SER ON SER.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN LAB_RD_ORDER_BOOKING LROB ON LROBD.LB_RD_ORDER_ID = LROB.LB_RD_ORDER_ID\r\n"
			+ "			    LEFT JOIN PATIENT_DEMOGRAPHY PD ON PD.PATIENT_ID = LROB.PATIENT_ID\r\n"
			+ "			where LROBD.RESULT_ID is not null and RESULT_NO is not null and LR.result_no \r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl) and (LR.result_no = ?1)", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfRD(String string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,			\r\n"
			+ "			pd.patient_id,\r\n" + "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM\r\n" + "			    LAB_RD_ORDER_BOOKING_DETAIL LROBD  \r\n"
			+ "			    LEFT JOIN rd_results LR ON LROBD.RESULT_ID=LR.RESULT_ID\r\n"
			+ "			    JOIN RD_TESTS RTS ON LROBD.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN SERVICES SER ON SER.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN LAB_RD_ORDER_BOOKING LROB ON LROBD.LB_RD_ORDER_ID = LROB.LB_RD_ORDER_ID\r\n"
			+ "			    LEFT JOIN PATIENT_DEMOGRAPHY PD ON PD.PATIENT_ID = LROB.PATIENT_ID\r\n"
			+ "			where LROBD.RESULT_ID is not null and RESULT_NO is not null and LR.result_no \r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl) and (LR.result_no in (?1))", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfRDByList(Set<String> string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,			\r\n"
			+ "			pd.patient_id,\r\n" + "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM\r\n" + "			    LAB_RD_ORDER_BOOKING_DETAIL LROBD  \r\n"
			+ "			    LEFT JOIN rd_results LR ON LROBD.RESULT_ID=LR.RESULT_ID\r\n"
			+ "			    JOIN RD_TESTS RTS ON LROBD.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN SERVICES SER ON SER.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN LAB_RD_ORDER_BOOKING LROB ON LROBD.LB_RD_ORDER_ID = LROB.LB_RD_ORDER_ID\r\n"
			+ "			    LEFT JOIN PATIENT_DEMOGRAPHY PD ON PD.PATIENT_ID = LROB.PATIENT_ID\r\n"
			+ "			where LROBD.RESULT_ID is not null and RESULT_NO is not null and (LR.result_no in (?1))", nativeQuery = true)
	public List<Map<String, Object>> getCareContextOfRDBy(Set<String> string);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,			\r\n"
			+ "			pd.patient_id,\r\n" + "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display,'RADIOLOGY' as report_type,LROB.created_date as consultation_date\r\n"
			+ "			FROM\r\n" + "			    LAB_RD_ORDER_BOOKING_DETAIL LROBD  \r\n"
			+ "			    LEFT JOIN rd_results LR ON LROBD.RESULT_ID=LR.RESULT_ID\r\n"
			+ "			    JOIN RD_TESTS RTS ON LROBD.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN SERVICES SER ON SER.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN LAB_RD_ORDER_BOOKING LROB ON LROBD.LB_RD_ORDER_ID = LROB.LB_RD_ORDER_ID\r\n"
			+ "			    LEFT JOIN PATIENT_DEMOGRAPHY PD ON PD.PATIENT_ID = LROB.PATIENT_ID\r\n"
			+ "			where LROBD.RESULT_ID is not null and RESULT_NO is not null and (LR.result_no in (?1))\r\n"
			+ "			and LR.created_date ::::date between ?2 and ?3", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfRdByDate(Set<String> interatcionIds, LocalDate fromDate,
			LocalDate toDate);

	@Query(value = "select distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid::::varchar as result_no,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			 where (odc.dcid = ?1) and odc.dcid ::::varchar\r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfConsultations(Long string);

	@Query(value = "select distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid::::varchar as result_no,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			 where (odc.dcid in (?1)) and odc.dcid ::::varchar\r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfConsultationsByList(Set<Long> string);

	@Query(value = "select distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid::::varchar as result_no,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			 where (odc.dcid in (?1))", nativeQuery = true)
	public List<Map<String, Object>> getCareContextOfConsultations(Set<Long> string);

	@Query(value = "	select distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid::::varchar as result_no,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			where (odc.dcid in (?1)) and odc.created_date ::::date between ?2 and ?3", nativeQuery = true)
	public List<Map<String, Object>> getLatestCareContextOfConsultationsWithDateRange(Set<Long> interatcionIds,
			LocalDate fromDate, LocalDate toDate);

	@Query(value = "SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,\r\n"
			+ "						pd.patient_id,\r\n"
			+ "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM LAB_RD_ORDER_BOOKING LROB\r\n"
			+ "			LEFT JOIN PATIENT_DEMOGRAPHY PD ON LROB.PATIENT_ID=PD.PATIENT_ID\r\n"
			+ "			INNER JOIN LAB_RD_ORDER_BOOKING_DETAIL LROBD ON LROB.LB_RD_ORDER_ID=LROBD.LB_RD_ORDER_ID\r\n"
			+ "			INNER JOIN SERVICES S ON LROBD.TEST_ID = S.SERVICE_ID \r\n"
			+ "			LEFT JOIN LAB_RESULT LR ON LROBD.RESULT_ID=LR.RESULT_ID \r\n"
			+ "			where LROBD.RESULT_ID is not null and (mrn_no=?1) and result_no is not null and LR.result_no \r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl)\r\n"
			+ "			union all\r\n" + "			SELECT distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end as patient_display,			\r\n"
			+ "			pd.patient_id,\r\n" + "			LR.result_no as careContexts_referenceNumber,lr.result_id,\r\n"
			+ "			concat('RESULT_DATE : '||to_char(LROB.created_date::::date,'dd-mm-yyyy')) as careContexts_display\r\n"
			+ "			FROM\r\n" + "			    LAB_RD_ORDER_BOOKING_DETAIL LROBD  \r\n"
			+ "			    LEFT JOIN rd_results LR ON LROBD.RESULT_ID=LR.RESULT_ID\r\n"
			+ "			    JOIN RD_TESTS RTS ON LROBD.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN SERVICES SER ON SER.SERVICE_ID = RTS.RD_TEST_ID\r\n"
			+ "			    JOIN LAB_RD_ORDER_BOOKING LROB ON LROBD.LB_RD_ORDER_ID = LROB.LB_RD_ORDER_ID\r\n"
			+ "			    LEFT JOIN PATIENT_DEMOGRAPHY PD ON PD.PATIENT_ID = LROB.PATIENT_ID\r\n"
			+ "			where LROBD.RESULT_ID is not null and\r\n"
			+ "			(mrn_no=?1) and RESULT_NO is not null and LR.result_no \r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl)\r\n"
			+ "			union all\r\n" + "			select distinct pd.mrn_no as patient_referencenumber,\r\n"
			+ "			case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' - ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' - ',pd.patient_middle_name,' - ',pd.patient_last_name) end\r\n"
			+ "			as patient_display  ,\r\n"
			+ "			pd.patient_id,odc.dcid::::varchar as result_no,odc.dcid as careContexts_referenceNumber ,\r\n"
			+ "			concat('CONSULTATION DATE : ',TO_CHAR(odc.created_date :::: DATE, 'dd-mm-yyyy')) as careContexts_display \r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join patient_demography pd on pd.patient_id=odc.patient_id\r\n"
			+ "			 where (pd.mrn_no  = ?1) and odc.dcid ::::varchar\r\n"
			+ "			not in(select ccl.patient_interaction_id  from care_context_logs ccl)", nativeQuery = true)
	public List<Map<String, Object>> getAllCareContextBaseONMrnNo(String mrnNo);

	@Query(value = "select ot.op_triage_id,case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) \r\n"
			+ "			then concat(pd.patient_first_name,' ',pd.patient_last_name)\r\n"
			+ "			else concat(pd.patient_first_name,' ',pd.patient_middle_name,' ',pd.patient_last_name) end\r\n"
			+ "			as patient_name ,dcid,concat(d.firstname,' ',d.middlename,' ',d.lastname)  as doctor ,odc.created_date as consultation_date,\r\n"
			+ "			ot.patient_id,'OPCONSULTATION' as report_type\r\n"
			+ "			from op_doctor_consultation odc \r\n"
			+ "			join op_triage ot  on ot.op_triage_id= odc.op_triage_id \r\n"
			+ "			join patient_demography pd  on pd.patient_id =odc.patient_id \r\n"
			+ "			left join doctors d on d.doctor_id =odc.doctor_id \r\n"
			+ "			where (odc.dcid in(?1)) and odc.created_date::::date between ?2 and ?3", nativeQuery = true)
	public List<Map<String, Object>> getConsultationDetailsToGenerateReport(Set<Long> consultationIds,
			LocalDate fromDate, LocalDate toDate);

	@Transactional
	@Modifying
	@Query(value = "update lab_result set iscare_context_linked=?1 where result_no= ?2", nativeQuery = true)
	public void updateCareContextStatusForLab(boolean b, String interactionsId);

	@Transactional
	@Modifying
	@Query(value = "update lab_result set direct_auth_status=?1 where result_no=?2", nativeQuery = true)
	public void updateDirectAuthStatusForLab(String status, String interactionsId);

	@Transactional
	@Modifying
	@Query(value = "update rd_results set direct_auth_status=?1 where result_no=?2", nativeQuery = true)
	public void updateDirectAuthStatusForRR(String status, String interactionsId);

	@Transactional
	@Modifying
	@Query(value = "update rd_results set iscare_context_linked=?1 where result_no=?2", nativeQuery = true)
	public void updateCareContextForRR(boolean status, String interactionsId);

	@Query(value = "SELECT DISTINCT COALESCE(pat.patient_id, 0::::bigint) AS patient_id,\r\n"
			+ "			 pat.mrn_no,\r\n" + "			 COALESCE(pat.salutation_id, 0::::bigint) AS salutation_id,\r\n"
			+ "			 pat.patient_first_name,\r\n" + "			 pat.patient_middle_name,\r\n"
			+ "			 pat.patient_last_name,\r\n" + "			 pat.dob,\r\n"
			+ "			 pa.mobile1 as mobile_no,\r\n" + "			 COALESCE(pat.age, 0) AS age,\r\n"
			+ "			 COALESCE(pat.gender_id, 0::::bigint) AS gender_id,\r\n"
			+ "			 COALESCE(pat.maritalstatus, 0::::bigint) AS maritalstatus,\r\n"
			+ "			 COALESCE(pat.identification_type_id, 0::::bigint) AS identification_type_id,\r\n"
			+ "			 pat.identification_card,\r\n"
			+ "			 COALESCE(pat.nationality, 0::::bigint) AS nationality,\r\n"
			+ "			 COALESCE(pat.education_id, 0::::bigint) AS education_id,\r\n"
			+ "			 COALESCE(pat.occupation_id, 0::::bigint) AS occupation_id,\r\n"
			+ "			 COALESCE(pat.incomegroup_id, 0::::bigint) AS incomegroup_id,\r\n"
			+ "			 COALESCE(pat.religion_id, 0::::bigint) AS religion_id,\r\n"
			+ "			 COALESCE(pat.employee_id, 0::::bigint) AS employee_id,\r\n" + "			 emp.empcode,\r\n"
			+ "			 (((emp.firstname::::text || '  '::::text) || emp.middlename::::text) || '  '::::text) || emp.lastname::::text AS empfullname,\r\n"
			+ "			 COALESCE(pat.relationship_id, 0::::bigint) AS relationship_id,\r\n"
			+ "			 rel.description AS relationshipname,\r\n"
			+ "			 COALESCE(emp.ip_limit, 0::::numeric) AS ip_limit,\r\n"
			+ "			 COALESCE(emp.op_limit, 0::::numeric) AS op_limit,\r\n"
			+ "			 COALESCE(pat.patient_type, 0::::bigint) AS patient_type,\r\n"
			+ "			 COALESCE(pat.patient_category_id, 0::::bigint) AS patient_category_id,\r\n"
			+ "			 COALESCE(pat.tariff_category, 0::::bigint) AS tariff_category,\r\n"
			+ "			 COALESCE(pat.payment_type_id, 0::::bigint) AS payment_type_id,\r\n"
			+ "			 COALESCE(pat.casetype_id, 0::::bigint) AS casetype_id,\r\n"
			+ "			 pat.may_call_this_number,\r\n" + "			 pat.may_leave_message,\r\n"
			+ "			 pat.nhif_cardno,\r\n" + "			 COALESCE(pat.status, 0) AS status,\r\n"
			+ "			 COALESCE(pat.center_id, 0::::bigint) AS center_id,\r\n" + "			 pat.declared,\r\n"
			+ "			 pat.is_emergency,\r\n" + "			 COALESCE(pat.uhid, ''::::character varying) AS uhid,\r\n"
			+ "			 COALESCE(pat.patientsource_id, 0::::bigint) AS patientsource_id,\r\n"
			+ "			 COALESCE(pat.patientsourcedtls, '0'::::character varying) AS patientsourcedtls,\r\n"
			+ "			 COALESCE(pat.mlc, ''::::character varying::::bpchar) AS mlc,\r\n"
			+ "			 COALESCE(pat.blood_group, 0) AS blood_group,\r\n"
			+ "			 COALESCE(pat.hospital_confirmation, ''::::character varying::::bpchar) AS hospital_confirmation,\r\n"
			+ "			 COALESCE(pat.police_station, ''::::character varying) AS police_station,\r\n"
			+ "			 COALESCE(pat.organ_donation_status, ''::::character varying::::bpchar) AS organ_donation_status,\r\n"
			+ "			 COALESCE(pat.famliy_size, 0) AS famliy_size,\r\n"
			+ "			 COALESCE(pat.treating_doctor_id, ''::::character varying) AS treating_doctor_id,\r\n"
			+ "			 pat.can_view_in_other_centers,\r\n" + "			 pat.patient_photo,\r\n"
			+ "			 pt.pt_category,\r\n" + "			 pc.patient_category_type,\r\n"
			+ "			 pat.patient_document,\r\n" + "			    emp.reldate,t.description,\r\n"
			+ "				 pt.description as paymenttype,\r\n"
			+ "				 pc.patient_category_desc,pat.referral_id as referral_id \r\n" + "			FROM patient_demography pat\r\n"
			+ "			  LEFT JOIN employees emp ON emp.employee_id = pat.employee_id\r\n"
			+ "			  inner join patient_address pa on pa.patient_id = pat.patient_id \r\n"
			+ "			  LEFT JOIN relationship rel ON rel.relationship_id = pat.relationship_id\r\n"
			+ "			  LEFT JOIN payment_type pt ON pat.payment_type_id = pt.pt_id\r\n"
			+ "			  LEFT JOIN patient_category pc ON pat.patient_category_id = pc.patient_category_id left join tariff  t on t.tariff_id=pat.tariff_category\r\n"
			+ "			   where (pat.patient_id =?1 or -1=?1) and (mrn_no=?2 or '-1'=?2)", nativeQuery = true)
	public Map<String, Object> findByPatientId2(Long id, String mrnNo);

	@Modifying
	@Transactional
	@Query(value = "update patient_demography set is_abha_linked = ?1 where national_health_id = ?2", nativeQuery = true)
	public void updateAbhaLinkedStatus(int i, String healthId);

	@Query(value = "select pd.patient_id, pd.mrn_no, pa.mobile1 as mobile_no,pd.gender_id,extract('year' from pd.dob) as year_of_birth,\r\n"
			+ "pd.national_health_id, pd.national_health_number,pd.patient_first_name as first_name,pd.patient_middle_name as middle_name, \r\n"
			+ "pd.patient_last_name as last_name from patient_demography pd inner join patient_address pa on pd.patient_id = pa.patient_id \r\n"
			+ "where (pd.national_health_number = ?1) limit 1", nativeQuery = true)
	public PatientRegistrationDTO findByHealthNumber(String healthNumber);

	@Query(value = "select * from patient_demography pd where pd.national_health_number  =?1 limit 1", nativeQuery = true)
	public PatientDemography findByHealthNumberNew(String healthNumber);
	
	@Query(value = "SELECT lpad(cast(cast(right(max(MRN_NO),8) as bigint)+1 as varchar),8,'0')  "
			+ "	FROM patient_demography  	WHERE mrn_no is not null  and mrn_no like ?1 ", nativeQuery = true)
	public String generateMrnoNewR(String CODENOCHECK);
}
