package com.dipl.abha.uhi.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.ExtractResultSetService;
import com.dipl.abha.uhi.entities.HspaDoctorMappingDetails;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HSPABulidService {

	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

	@Autowired
	private ExtractResultSetService extractResultSetService;

	public ResponseEntity<?> getHprDetails(String hprId, HttpServletRequest httpServletRequest) {
		log.info("{}==> " + hprId);
		ResponseBean bean = new ResponseBean();
		try {
			if (hprId != null) {
				List<AbhaQueryTable> requestQueries = jdbcTemplateHelper.getResults(
						"select * from public.abha_query_table where  tenant_id = " + TenantContext.getCurrentTenant()
								+ " and query_type in ('GET-HPR-ID-DETAILS')",
						AbhaQueryTable.class);

				if (requestQueries != null && !requestQueries.isEmpty()) {
//					List<String> hprDetails =extractResultSetService.excuteDynamicQueryForHprIdDetails(returnFinalQueryForDirectAuth
////							(this.streamAndReturnQuery("GET-HPR-ID-DETAILS", requestQueries),hprId));
//					if(hprDetails != null && !hprDetails.isEmpty()) {
//					bean.setData(hprDetails);
//					bean.setMessage("hpr Details");
//					bean.setStatus(HttpStatus.OK);

				} else {
					bean.setData(null);
					bean.setMessage("no recored found");
					bean.setStatus(HttpStatus.OK);
				}
			} else {
				log.info("NO QUERIES FOUND IN DB================>");
			}

//			}
//		else {
//				bean.setData(new ArrayList<>());
//				bean.setMessage("HPRID should not be null");
//				bean.setStatus(HttpStatus.EXPECTATION_FAILED);
//			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(bean, bean.getStatus());
	}

	public ResponseEntity<?> saveDoctorDetails(HspaDoctorMappingDetails hspaDoctorDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public String streamAndReturnQuery(String apiType, List<AbhaQueryTable> abhaQueriesList) {
		Optional<String> findFirst = abhaQueriesList.stream().filter(s -> s.getQueryType().equals(apiType))
				.map(p -> p.getQuery()).findFirst();
		if (findFirst.isPresent()) {
			return findFirst.get();
		}
		return "";
	}

	public String returnFinalQueryForDirectAuth(String partialQuery, String hprId) throws JsonProcessingException {
		if (hprId.isEmpty()) {
			hprId = "('')";
		} else {
			hprId = "(" + hprId + ")";
		}
		Map<String, String> replacementStrings = Map.of("?1", hprId);
		StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
		String finalQuery = sub.replace(partialQuery);
//		System.out.println("Final Query which is executing ==========>   " + finalQuery);
		log.info("Final Query which is executing ==========>   " + finalQuery);
		return finalQuery;
	}

}
