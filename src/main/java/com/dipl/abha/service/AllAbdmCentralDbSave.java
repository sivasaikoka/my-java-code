package com.dipl.abha.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.m3.consentHiuNotify.ConsentHiuNotify;
import com.dipl.abha.m3.consentRequestOnInit.ConsentArtefact;
import com.dipl.abha.uhi.entities.EUARequestAndResponse;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AllAbdmCentralDbSave {

	private static final Logger LOGGER = LoggerFactory.getLogger(AllAbdmCentralDbSave.class);

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

	public void saveAbhaRegistration(Integer tenantId, String apiType, String abhaNo, String abhaAddress,
			String abhaProfile, Long patientId, String gender, Integer yearOfBirth, String mobileNo, int abhaStatus,
			String fullName) {
		Connection conn = null;
		LOGGER.info("Entering into Central ABDM save....");
		LOGGER.info("DB URL====================================>" + dbUrl);
		LOGGER.info("DB USER NAME============================>" + dbUserName);
		LOGGER.info("DB PASSWORD==============================>" + dbPassword);
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String getExistingRecord = "select * from abha_registration where tenant_id=? and abha_no = ? and patient_id = ?";
			PreparedStatement statement = conn.prepareStatement(getExistingRecord);
			statement.setInt(1, tenantId);
			statement.setString(2, abhaNo);
			statement.setLong(3, patientId);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				String sql = "INSERT INTO public.abha_registration\r\n"
						+ "(tenant_id, api_type, abha_no, abha_address, abha_profile, patient_id, gender, year_of_birth, created_on, mobile_no,is_abha_linked, full_name)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), ?,?,?);";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setInt(1, tenantId);
				preparedStatement.setString(2, apiType);
				preparedStatement.setString(3, abhaNo);
				preparedStatement.setString(4, abhaAddress);
				preparedStatement.setString(5, abhaProfile);
				preparedStatement.setLong(6, patientId);
				preparedStatement.setString(7, gender);
				preparedStatement.setLong(8, yearOfBirth);
				preparedStatement.setString(9, mobileNo);
				preparedStatement.setInt(10, abhaStatus);
				preparedStatement.setString(11, fullName);
				System.out.println(sql);
				preparedStatement.executeUpdate();
				System.out.println("Inserted records into the abha registration table...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updatePatientId(Integer tenantId, String abhaNo, Long patientId) {
		Connection conn = null;
		LOGGER.info("Entering into Central ABDM save.... to update patient Id");
		LOGGER.info("DB URL====================================>" + dbUrl);
		LOGGER.info("DB USER NAME============================>" + dbUserName);
		LOGGER.info("DB PASSWORD==============================>" + dbPassword);
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String getExistingRecord = "update abha_registration set patient_id = ? where abha_no = ? and tenant_id = ?";
			PreparedStatement statement = conn.prepareStatement(getExistingRecord);
			statement.setLong(1, patientId);
			statement.setString(2, abhaNo);
			statement.setLong(3, tenantId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveAbhaRequestMapping(String requestId, Integer tenantId, String url, Integer apiTypeId,
			String requestPayload) {
		Connection conn = null;
		try {
			LOGGER.info("Entering into abdm request mapping save...");
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "INSERT INTO public.abdm_request_mapping\r\n"
					+ "(request_id, tenant_id, api_type, api_request_type_id, request_payload, created_on)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, now());";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			if (requestId.isEmpty()) {
				preparedStatement.setString(1, UUID.randomUUID().toString());
			} else {
				preparedStatement.setString(1, requestId);
			}
			preparedStatement.setInt(2, tenantId);
			preparedStatement.setString(3, url);
			preparedStatement.setInt(4, apiTypeId);
			preparedStatement.setString(5, requestPayload);
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveAbhaRequestMappingNotify(String requestId, Integer tenantId, String url, Integer apiTypeId,
			String requestPayload, String consentId, String consentArtifactId) {
		Connection conn = null;
		try {
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "INSERT INTO public.abdm_request_mapping\r\n"
					+ "(request_id, tenant_id, api_type, api_request_type_id, request_payload, created_on, consent_id, consent_artifact_id)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, now(),?,?);";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			if (requestId.isEmpty()) {
				preparedStatement.setString(1, UUID.randomUUID().toString());
			} else {
				preparedStatement.setString(1, requestId);
			}
			preparedStatement.setInt(2, tenantId);
			preparedStatement.setString(3, url);
			preparedStatement.setInt(4, apiTypeId);
			preparedStatement.setString(5, requestPayload);
			preparedStatement.setString(6, consentId);
			preparedStatement.setString(7, consentArtifactId);
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String buildUrl(HttpServletRequest servletRequest) {
		System.out.println("Server URL============>" + servletRequest.getScheme() + "s:/"
				+ servletRequest.getServerName() + servletRequest.getContextPath());
		return servletRequest.getScheme() + "s://" + servletRequest.getServerName() + servletRequest.getContextPath();
	}

	public void processPayload(String requestPayload, Integer apiTypeId, HttpServletRequest servletRequest) {
		String requestId = "";
		if (requestPayload != null) {
			JSONObject json = new JSONObject(requestPayload);
			requestId = json.get("requestId").toString();
		}
		this.saveAbhaRequestMapping(requestId, Integer.parseInt(TenantContext.getCurrentTenant()),
				this.buildUrl(servletRequest), apiTypeId, requestPayload);
	}

	public void processPayloadForHIUNotify(ConsentHiuNotify consentHiuNotify, Integer apiTypeId,
			HttpServletRequest servletRequest) throws JsonMappingException, JsonProcessingException {
		String requestId = "";
		if (consentHiuNotify != null && consentHiuNotify.getNotification() != null
				&& consentHiuNotify.getNotification().getConsentArtefacts() != null
				&& !consentHiuNotify.getNotification().getConsentArtefacts().isEmpty()) {
			for (ConsentArtefact s : consentHiuNotify.getNotification().getConsentArtefacts()) {
				this.saveAbhaRequestMappingNotify(requestId, Integer.parseInt(TenantContext.getCurrentTenant()),
						this.buildUrl(servletRequest), apiTypeId, objMapper.writeValueAsString(consentHiuNotify),
						consentHiuNotify.getNotification().getConsentRequestId(), s.getId());
			}

		}

	}

	public void saveConsentRequestId(String requestPayload, Integer apiTypeId, ConsentHiuNotify consentOnStatus,
			HttpServletRequest servletRequest) {
		String requestId = "";
		if (requestPayload != null) {
			JSONObject json = new JSONObject(requestPayload);
			requestId = json.getString("requestId");
		}
		this.saveAbhaRequestMapping(requestId, Integer.parseInt(TenantContext.getCurrentTenant()),
				this.buildUrl(servletRequest), apiTypeId, requestPayload);
	}

	public void processPayloadNotify(String requestPayload, Integer apiTypeId, HttpServletRequest servletRequest) {
		String requestId = "";
		String consentId = "";
		if (requestPayload != null) {
			JSONObject json = new JSONObject(requestPayload);
			requestId = json.getString("requestId");
			consentId = json.getJSONObject("notification").getString("consentId");
		}
		this.saveAbhaRequestMappingNotify(requestId, Integer.parseInt(TenantContext.getCurrentTenant()),
				this.buildUrl(servletRequest), apiTypeId, requestPayload, consentId, "");
	}

	public void processPayloadRequest(String requestPayload, Integer apiTypeId, HttpServletRequest servletRequest) {
		String requestId = "";
		String consentId = "";
		if (requestPayload != null) {
			JSONObject json = new JSONObject(requestPayload);
			requestId = json.getString("requestId");
			consentId = json.getJSONObject("hiRequest").getJSONObject("consent").getString("id");
		}
		this.saveAbhaRequestMappingNotify(requestId, Integer.parseInt(TenantContext.getCurrentTenant()),
				this.buildUrl(servletRequest), apiTypeId, requestPayload, consentId, "");
	}

	public void updateCallBackInCentralDb(Integer tenantId, String payload, String consentId, String requestId,
			String txnId) {
		Connection conn = null;
		LOGGER.info("Entering into Central ABDM Save.... for CALL BACK");
		LOGGER.info("DB URL====================================>" + dbUrl);
		LOGGER.info("DB USER NAME============================>" + dbUserName);
		LOGGER.info("DB PASSWORD==============================>" + dbPassword);
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String getExistingRecord = "update public.abdm_request_mapping set callback_response = ?, txn_id = ?,consent_id = ? where request_id = ?";
			PreparedStatement statement = conn.prepareStatement(getExistingRecord);
			statement.setString(1, payload);
			statement.setString(2, txnId);
			statement.setString(3, consentId);
			statement.setString(4, requestId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insertHipTenantMapping(Integer tenantId, String hipCode) {
		Connection conn = null;
		LOGGER.info("Entering into Central ABDM Save.... for HIP TENANT MAPPING");
		LOGGER.info("DB URL====================================>" + dbUrl);
		LOGGER.info("DB USER NAME============================>" + dbUserName);
		LOGGER.info("DB PASSWORD==============================>" + dbPassword);
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			if (tenantId != null) {
				String checkTenant = "select * from orgnization_registration org where org.id = ?";
				PreparedStatement checkTenantStatement = conn.prepareStatement(checkTenant);
				checkTenantStatement.setInt(1, tenantId);
				ResultSet tenantResult = checkTenantStatement.executeQuery();
				if (tenantResult != null && tenantResult.next()) {
					String getExistingRecord = "select * from hip_tenant_mapping htm where htm.tenant_id = ? and htm.hfr_id = ?";
					PreparedStatement statement = conn.prepareStatement(getExistingRecord);
					statement.setInt(1, tenantId);
					statement.setString(2, hipCode.trim());
					ResultSet resultSet = statement.executeQuery();
					if (resultSet!=null && !resultSet.next()) {
						String insertQuery = "INSERT INTO public.hip_tenant_mapping\r\n"
								+ "(hfr_id, tenant_id, api_url, created_on)\r\n" + "VALUES(?, ?, ?, now())";
						PreparedStatement save = conn.prepareStatement(insertQuery);
						save.setString(1, hipCode.trim());
						save.setInt(2, tenantId);
						save.setString(3, "v0.5/consents/hip/notify?TENANT_ID=" + tenantId);
						save.executeUpdate();
					}
				} else {
					LOGGER.info("InValid TENANT ID {}", tenantId);
				}
			} else {
				LOGGER.info("TENANT ID IS NULL UNABLE TO SAVE HIP TENANT MAPPING");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveEuaRequest(String apiType,String transactionId,String order_id, String responsejsonNode , String requestjsonNode) {
		Connection conn = null;
		try {
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "INSERT INTO public.eua_request_response\r\n"
					+ "(api_type,type ,transaction_id,order_id, response_json, request_json, created_on)\r\n"
					+ "VALUES(?, ?, ?, ?,?,?,now());";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			preparedStatement.setString(1, apiType);
			preparedStatement.setString(2, "EUA");
			if (transactionId.isEmpty()) {
				preparedStatement.setString(3, UUID.randomUUID().toString());
			} else {
				preparedStatement.setString(3, transactionId);
			}
			preparedStatement.setString(4, order_id);
			if(responsejsonNode != null) {
				preparedStatement.setString(5, responsejsonNode);
				}else {
					preparedStatement.setString(5, null);
				}
				
				if(requestjsonNode != null) {
				preparedStatement.setString(6, requestjsonNode);
				}
				else {
					preparedStatement.setString(6, null);
				}
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public void saveHspaRequest(String apiType,String transactionId,String order_id,  String responsejsonNode,String requestjsonNode) {
		Connection conn = null;
		try {
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "INSERT INTO public.hspa_request_response\r\n"
					+ "(api_type,type ,transaction_id,order_id, response_json, request_json, created_on)\r\n"
					+ "VALUES(?, ?, ?, ?,?,?,now());";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, apiType);
			preparedStatement.setString(2, "HSPA");
			if (transactionId.isEmpty()) {
				preparedStatement.setString(3, UUID.randomUUID().toString());
			} else {
				preparedStatement.setString(3, transactionId);
			}
			preparedStatement.setString(4, order_id);
			if(responsejsonNode != null) {
			preparedStatement.setString(5, responsejsonNode);
			}else {
				preparedStatement.setString(5, null);
			}
			
			if(requestjsonNode != null) {
			preparedStatement.setString(6, requestjsonNode);
			}
			else {
				preparedStatement.setString(6, null);
			}
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<EUARequestAndResponse> FindByApiType(String apiType,String transactionId) {
		Connection conn = null;
		List<EUARequestAndResponse> requestAndResponses = new ArrayList<>();
		try {
			
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "SELECT * FROM  eua_request_response where api_type= ?  and transaction_id= ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, apiType);
			preparedStatement.setString(2, transactionId);
			System.out.println(sql);
			System.out.println(preparedStatement);
			
			 ResultSet resultSet = preparedStatement.executeQuery();
			 while (resultSet.next()) {
				  EUARequestAndResponse requestAndResponse = new EUARequestAndResponse();
				  requestAndResponse.setId(resultSet.getLong("id"));
				  requestAndResponse.setApiType(resultSet.getString("api_type"));
				  requestAndResponse.setType(resultSet.getString("type"));
				  requestAndResponse.setOrderId(resultSet.getString("order_id"));
				  requestAndResponse.setRequestJson(resultSet.getString("request_json"));
				  requestAndResponse.setResponseJson(resultSet.getString("response_json"));
				  requestAndResponse.setTransactionId(resultSet.getString("transaction_id"));
				  requestAndResponses.add(requestAndResponse);
				 
				  
			 }
			 System.out.println("Fectched  records from the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return requestAndResponses;
	}

	public void saveAppointmentBookingDetails(String transaction_id, String orderId, String hprId, String patintAbhaId,String consulationStatus) {
		Connection conn = null;
		try {
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "INSERT INTO public.appoinments_booking_details\r\n"
					+ "(transaction_id,order_id ,doctor_hpr_id,patient_abha_id,consulation_status , created_on)\r\n"
					+ "VALUES(?, ?, ?, ?,?,now());";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, transaction_id);
			preparedStatement.setString(2, orderId);
			preparedStatement.setString(3, hprId);
			preparedStatement.setString(4, patintAbhaId);
			preparedStatement.setString(5, consulationStatus);
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void updateConsulationStatus(String orderId, String status, String cancel_by) {
		Connection conn = null;
		try {
			LOGGER.info("DB URL====================================>" + dbUrl);
			LOGGER.info("DB USER NAME============================>" + dbUserName);
			LOGGER.info("DB PASSWORD==============================>" + dbPassword);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String sql = "update public.appoinments_booking_details set consulation_status = ?,cancel_by = ? where order_id= ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setString(2, cancel_by);
			preparedStatement.setString(3, orderId);
			
			
			System.out.println(sql);
			preparedStatement.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	

}
