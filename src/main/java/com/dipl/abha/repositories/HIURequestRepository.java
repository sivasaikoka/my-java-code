package com.dipl.abha.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIURequest;

@Repository
public interface HIURequestRepository extends JpaRepository<HIURequest, Long> {

	Optional<HIURequest> findByRequestCode(String requestId);
	
	@Query(value = "select hr.id,hr.request_code  as requestId,cast(hr.request_json as varchar) as request,\r\n"
			+ "cast(hcb.response_json as varchar) as response,hr.abha_no,hcb.txn_code as \"txnId\" from hip_request hr\r\n"
			+ "left join hip_call_back hcb on hr.request_code = hcb.request_code\r\n"
			+ "where hr.request_code = ?1",nativeQuery = true)
	public Map<String,Object> findByRequestIdNew(String requestType);
	
	@Query(value = "WITH latest_hrs AS (\r\n"
			+ "				    SELECT \r\n"
			+ "				        *,\r\n"
			+ "				        ROW_NUMBER() OVER (PARTITION BY consent_request_code ORDER BY id DESC) AS rn\r\n"
			+ "				    FROM \r\n"
			+ "				       	hiu_request_status\r\n"
			+ "				)\r\n"
			+ "				SELECT \r\n"
			+ "				    CONCAT(pd.patient_first_name , ' ', pd.patient_middle_name, ' ', pd.patient_last_name) AS patient_full_name,\r\n"
			+ "				    pd.national_health_id AS abha_address, tmp2.status as status_history, TO_CHAR(hr.created_on, 'YYYY-MM-DD HH:MI:SS AM') as consent_created_on,\r\n"
			+ "				    CASE \r\n"
			+ "				        WHEN hrs.status_type IS NULL then 'REQUESTED'\r\n"
			+ "				        ELSE hrs.status_type\r\n"
			+ "				    END AS request_status,\r\n"
			+ "				    CASE \r\n"
			+ "				        WHEN tmp.data_erase_at IS NULL THEN TO_CHAR((hrm.request_json  -> 'consent'  -> 'permission' ->> 'dataEraseAt')::::timestamp + INTERVAL '5 hours 30 minutes', 'YYYY-MM-DD HH:MI:SS AM')\r\n"
			+ "				        ELSE TO_CHAR((tmp.data_erase_at)::::timestamp , 'YYYY-MM-DD HH:MI:SS AM')\r\n"
			+ "				    END AS data_erase_at,\r\n"
			+ "				    CASE \r\n"
			+ "					    WHEN tmp.from_date IS NULL THEN request_json -> 'consent' -> 'permission' -> 'dateRange' ->> 'from' \r\n"
			+ "					    ELSE tmp.from_date \r\n"
			+ "					END AS data_from,\r\n"
			+ "				    CASE \r\n"
			+ "					    WHEN tmp.from_date IS NULL THEN  request_json -> 'consent' -> 'permission' -> 'dateRange' ->> 'to'\r\n"
			+ "					    ELSE tmp.to_date \r\n"
			+ "					END AS data_to,\r\n"
			+ "					CASE \r\n"
			+ "				 		WHEN tmp.requested_hi_types IS NULL THEN request_json -> 'consent' ->> 'hiTypes' \r\n"
			+ "				 		ELSE tmp.requested_hi_types\r\n"
			+ "				   	END	AS requested_hi_types,\r\n"
//			+ "				   	 request_json ->> 'timestamp' AS consent_created_on,\r\n"
			+ "				    hr.response_json -> 'consentRequest' ->> 'id' AS consentId\r\n"
			+ "				FROM \r\n"
			+ "				    hiu_request hrm \r\n"
			+ "				INNER JOIN \r\n"
			+ "				    patient_demography pd  ON pd.patient_id = hrm.beneficiary_id  \r\n"
			+ "				INNER JOIN \r\n"
			+ "				    hiu_response hr ON hr.request_code = hrm.request_code \r\n"
			+ "				LEFT JOIN \r\n"
			+ "				    latest_hrs hrs ON hrs.consent_request_code = hr.response_json -> 'consentRequest' ->> 'id' AND hrs.rn = 1\r\n"
			+ "				LEFT JOIN \r\n"
			+ "				    (SELECT hr.consent_request_code, hr.response_json -> 'consent' -> 'consentDetail' -> 'permission' -> 'dateRange' ->>'from' as from_date,\r\n"
			+ "				          hr.response_json -> 'consent' -> 'consentDetail' -> 'permission' -> 'dateRange' ->>'to' as to_date,\r\n"
			+ "				         (hr.response_json -> 'consent' -> 'consentDetail' -> 'permission' ->> 'dataEraseAt')::::timestamp + INTERVAL '5 hours 30 minutes' AS data_erase_at,\r\n"
			+ "				          hr.response_json -> 'consent' -> 'consentDetail' ->> 'hiTypes' AS requested_hi_types\r\n"
			+ "				    FROM hiu_response hr\r\n"
			+ "				    WHERE hr.hiu_request_type_id = 6\r\n"
			+ "				    GROUP BY 1,2,3,4,5) tmp ON tmp.consent_request_code = hr.response_json -> 'consentRequest' ->> 'id'\r\n"
			+ "				left join \r\n"
			+ "					(select\r\n"
			+ "						hrs.consent_request_code ,\r\n"
			+ "						string_agg(concat(UPPER(hrs.status_type), ' AT (', TO_CHAR((hrs.created_on)::::timestamp , 'DD-MM-YYYY HH:MI:SS AM'), ')'), ', ') as status\r\n"
			+ "						from\r\n"
			+ "						hiu_request_status hrs\r\n"
			+ "						group by\r\n"
			+ "						hrs.consent_request_code)tmp2 on tmp2.consent_request_code = hr.response_json -> 'consentRequest' ->> 'id'\r\n"
			+ "				WHERE \r\n"
			+ "				    hrm.hiu_request_type_id = 1 \r\n"
			+ "				    AND (hrm.created_by = ?1 or -1=?1) and hrm.beneficiary_id = ?2\r\n"
			+ "				ORDER BY \r\n"
			+ "				    hrm.created_on DESC;"
			,nativeQuery=true)
	List<Map<String, Object>> findConsentsByDoctorId(Long doctorId,Long beneficiaryId);
	
	
	
	@Query(value = "select hr.id as id,hr.request_code as requestId,cast(hr.request_json as varchar) as request, \r\n"
			+ "cast(hr2.response_json as varchar) as response,hr.abha_no  from hiu_request hr  \r\n"
			+ "left join hiu_response hr2 on hr.request_code = hr2.request_code \r\n"
			+ "where hr.request_code = ?1",nativeQuery = true)
	public Map<String,Object> findByM3RequestIdNew(String requestType);

}
