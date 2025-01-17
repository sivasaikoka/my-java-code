package com.dipl.abha.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dipl.abha.dto.HIPTenantMappingDto;
import com.dipl.abha.m2.datapushpayload.DataPushPayload;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Component
@Slf4j
public class MultiTenantInterceptor extends HandlerInterceptorAdapter {

	private String tenantHeader = "TENANT_ID";
	@Value("${DBURL}")
	private String dbUrl;
	@Value("${DBUSERNAME}")
	private String dbUserName;
	@Value("${DBPASSWORD}")
	private String dbPassword;
	@Autowired
	private JdbcTemplateHelper jdbcTemplate;

	@Autowired
	private ObjectMapper objMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String baseUrl = request.getScheme() + "s://" + request.getServerName() + "/";
		log.info("URL ==============> {}", request.getRequestURL());
		String tenantId = "";
		tenantId = request.getParameter(tenantHeader);
		if (tenantId != null && !tenantId.isEmpty()) {
			this.setClientSecrete(baseUrl, request, tenantId);
		} else if (request.getParameter(tenantHeader) == null) {
			this.setClientSecrete(baseUrl, request, null);
		}
		return true;
	}

	public void setClientSecrete(String baseUrl, HttpServletRequest request, String tenantId) {
		Connection connection = null;
		try {
			if (tenantId != null) {
				List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
						"select id as tenant_id,ndhm_client_id, ndhm_client_secrete_key  from orgnization_registration or1 where or1.id="
								+ tenantId,
						HIPTenantMappingDto.class);
				if (!results.isEmpty()) {
					TenantContext.setCurrentTenant(results.get(0).getTenantId() + "");
					TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
					TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
				}
			} else if (ConstantUtil.ABHA_URLS.contains(request.getRequestURI())) {
				tenantId = this.returnTenantIdOnRequestId(request.getAttribute("Payload").toString());
				if (tenantId != null && !tenantId.isEmpty()) {
					List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
							"select id as tenant_id,ndhm_client_id, ndhm_client_secrete_key from public.orgnization_registration where id = "
									+ tenantId,
							HIPTenantMappingDto.class);
					if (!results.isEmpty()) {
						TenantContext.setCurrentTenant(tenantId + "");
						TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
						TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
						log.info("=================IM A TRUE==============");
					}
				}
			} else if (ConstantUtil.ABHA_V3_ON_GENERATE_TOKEN.contains(request.getRequestURI())) {
				tenantId = this.returnTenantIdOnRequestIdV3(request.getAttribute("Payload").toString());
				if (tenantId != null && !tenantId.isEmpty()) {
					List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
							"select id as tenant_id,ndhm_client_id, ndhm_client_secrete_key from public.orgnization_registration where id = "
									+ tenantId,
							HIPTenantMappingDto.class);
					if (!results.isEmpty()) {
						TenantContext.setCurrentTenant(tenantId + "");
						TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
						TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
						log.info("=================IM A TRUE==============");
					}
				}
			} 
			else if (ConstantUtil.ABHA_UHI_URLS.contains(request.getRequestURI())) {
				log.info("======ENTER INTO UHI URLS===============");
				tenantId = this.returnTenantIdOnRequestIdUhi(request.getAttribute("Payload").toString());
				if (tenantId != null && !tenantId.isEmpty()) {
					List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
							"select id as tenant_id,ndhm_client_id, ndhm_client_secrete_key from public.orgnization_registration where id = "
									+ tenantId,
							HIPTenantMappingDto.class);
					if (!results.isEmpty()) {
						TenantContext.setCurrentTenant(tenantId + "");
						TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
						TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
						log.info("=================IM A TRUE==============");
					}
				}
			}
			
			
			
			
			else {
				this.checkApiTypeAndGetTenantId(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void checkApiTypeAndGetTenantId(HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		if ((ConstantUtil.ABHA_NOTIFY_API_END_POINT_v05).contains(request.getRequestURI())
				|| ConstantUtil.ABHA_DISCOVER_URLS.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_DISCOVER_LINK_INIT.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_DISCOVER_LINK_CONFIRM.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_DIRECT_AUTH_URL.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_SHARE_PROFILE.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_REQUEST_API_END_POINT_v05.contains(request.getRequestURI())
				|| ConstantUtil.ABHA_V3_DISCOVER_APIS.contains(request.getRequestURI())) {
			String hipId = request.getHeader("X-HIP-ID");
			this.setClientIdSecrete(request, hipId);
		} else if (ConstantUtil.ABHA_HIU_NOTIFY_API_END_POINT_v1.contains(request.getRequestURI())) {
			String hipId = request.getHeader("X-HIU-ID");
			log.info("<========Called===URL======> " + request.getRequestURL() + " <==INVOKED==HFR_CODE====>" + hipId);
			this.setClientIdSecrete(request, hipId);
		} else if (ConstantUtil.ABHA_DATA_PUSH_API_END_POINT_v05.contains(request.getRequestURI())) {
			Object obj = this.returnRespectiveObjectFromPayload(request);
			log.info("<========Called===URL======> " + request.getRequestURL());
			if (obj != null) {
				DataPushPayload dataPushPayload = objMapper.readValue(objMapper.writeValueAsString(obj),
						DataPushPayload.class);
				if (dataPushPayload != null) {
					List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
							"select or2.id as tenant_id,or2.ndhm_client_id , or2.ndhm_client_secrete_key from\r\n"
									+ "public.orgnization_registration or2 inner join public.abdm_request_mapping arm on arm.tenant_id::int8 = or2.id \r\n"
									+ "where arm.txn_id ='" + dataPushPayload.getTransactionId() + "'",
							HIPTenantMappingDto.class);
					if (!results.isEmpty()) {
						log.info("<========INVOKED===TENANT======> " + results.get(0).getTenantId());
						TenantContext.setCurrentTenant(results.get(0).getTenantId() + "");
						TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
						TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
					}
				}
			}
		} else if (ConstantUtil.PHR_V3_DISCOVER_APIS.contains(request.getRequestURI())) {
			String hiuId = request.getHeader("x-hiu-id");
			this.setClientIdSecrete(request, hiuId);
		}

	}

	public void setClientIdSecrete(HttpServletRequest request, String hipId) {
		log.info("<========Called===URL======> " + request.getRequestURL() + " <==INVOKED==HFR_CODE====>" + hipId);
		List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
				"select or2.id as tenant_id,or2.ndhm_client_id , or2.ndhm_client_secrete_key  from public.hip_tenant_mapping htm\r\n"
						+ "inner join public.orgnization_registration or2 on or2.id =  htm.tenant_id where htm.hfr_id ='"
						+ hipId + "'",
				HIPTenantMappingDto.class);
		if (!results.isEmpty()) {
			log.info("<========INVOKED===TENANT======> " + results.get(0).getTenantId());
			TenantContext.setCurrentTenant(results.get(0).getTenantId() + "");
			TenantRoleContext.setNdhmClientId(results.get(0).getNdhmClientId());
			TenantRoleContext.setNdhmClientSecreteKey(results.get(0).getNdhmClientSecrete());
		}
	}

	public Object returnRespectiveObjectFromPayload(HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		Object obj = null;
		if (request != null && request.getAttribute("Payload") != null) {
			log.info("Payload===========>{}", request.getAttribute("Payload").toString());
			obj = objMapper.readValue(request.getAttribute("Payload").toString(), Object.class);
		}
		return obj;
	}

	private String returnTenantIdOnRequestId(String request) throws SQLException {
		String tenantId = null;
		String requestId = "";
		log.info("PAYLOAD ==============> {}", request);
		JSONObject jsonObject = new JSONObject(request);

		if (jsonObject.has("resp")) {
			requestId = jsonObject.getJSONObject("resp").getString("requestId");

		} else if (jsonObject.has("response")) {
			requestId = jsonObject.getJSONObject("response").getString("requestId");
		}

		if (!requestId.isEmpty()) {
			List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
					"select arm.tenant_id ,ndhm_client_id, ndhm_client_secrete_key  from orgnization_registration orr \r\n"
							+ "inner join public.abdm_request_mapping arm on arm.tenant_id::int = orr.id \r\n"
							+ "where arm.request_id = '" + requestId + "'",
					HIPTenantMappingDto.class);
			if (!results.isEmpty()) {
				tenantId = results.get(0).getTenantId().toString();
			}

		}
		return tenantId;
	}
	
	private String returnTenantIdOnRequestIdUhi(String request) throws SQLException {
		String tenantId = null;
		String requestId = "";
		log.info("PAYLOAD ==============> {}", request);
		JSONObject jsonObject = new JSONObject(request);

		if (jsonObject.has("context")) {
			requestId = jsonObject.getJSONObject("context").getString("transaction_id");

		} 
		if (!requestId.isEmpty()) {
			List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
					"select arm.tenant_id ,ndhm_client_id, ndhm_client_secrete_key  from orgnization_registration orr \r\n"
							+ "inner join public.abdm_request_mapping arm on arm.tenant_id::int = orr.id \r\n"
							+ "where arm.request_id = '" + requestId + "'",
					HIPTenantMappingDto.class);
			if (!results.isEmpty()) {
				tenantId = results.get(0).getTenantId().toString();
			}

		}
		return tenantId;
	}

	private String returnTenantIdOnRequestIdV3(String request) throws SQLException {
		String tenantId = null;
		String requestId = "";
		log.info("PAYLOAD ==============> {}", request);
		JSONObject jsonObject = new JSONObject(request);
		if (jsonObject.getJSONObject("response") != null) {
			requestId = jsonObject.getJSONObject("response").getString("requestId");
			if (!requestId.isEmpty()) {
				List<HIPTenantMappingDto> results = jdbcTemplate.getResults(
						"select arm.tenant_id ,ndhm_client_id, ndhm_client_secrete_key  from orgnization_registration orr \r\n"
								+ "inner join public.abdm_request_mapping arm on arm.tenant_id::int = orr.id \r\n"
								+ "where arm.request_id = '" + requestId + "'",
						HIPTenantMappingDto.class);
				if (!results.isEmpty()) {
					tenantId = results.get(0).getTenantId().toString();
				}
			}
		}
		return tenantId;
	}

	public static String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();
		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
	}
}