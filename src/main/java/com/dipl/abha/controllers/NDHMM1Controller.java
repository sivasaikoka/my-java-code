package com.dipl.abha.controllers;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.AccountProfile;
import com.dipl.abha.dto.CheckAndGenerateMobileOTP;
import com.dipl.abha.dto.CmSessions;
import com.dipl.abha.dto.CmToken;
import com.dipl.abha.dto.ConfirmAadharDTO;
import com.dipl.abha.dto.CreateHealthId;
import com.dipl.abha.dto.CreatePhrAddressPayload;
import com.dipl.abha.dto.DataEncrypted;
import com.dipl.abha.dto.DemographicDetails;
import com.dipl.abha.dto.ForgotVerifyMobileOtp;
import com.dipl.abha.dto.GenerateMobileOTP;
import com.dipl.abha.dto.GenerateOTP;
import com.dipl.abha.dto.HealthGererate;
import com.dipl.abha.dto.NdhmPassword;
import com.dipl.abha.dto.PatientRegistrationDTO;
import com.dipl.abha.dto.PhrLinked;
import com.dipl.abha.dto.RequestResultSetDto;
import com.dipl.abha.dto.ResendAadharOtpOrMobileOtp;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.SearchByHealthId;
import com.dipl.abha.dto.UserDetails;
import com.dipl.abha.dto.VerifyOTP;
import com.dipl.abha.repositories.PatientRegistrationRepository;
import com.dipl.abha.service.Base64Genarate;
import com.dipl.abha.service.ExtractResultSetService;
import com.dipl.abha.util.JdbcTemplateHelper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "m1")
@CrossOrigin
public class NDHMM1Controller {
	
	@Autowired
	private Base64Genarate base64Genarate;
	@Autowired
	private PatientRegistrationRepository patientRegRepository;
	@Value("${CONSENT_MANAGER_ID}")
	private String cmId;
	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;
	@Autowired
	private ExtractResultSetService  testDynamicQueryRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM1Controller.class);

	@PostMapping("/engenerateaadharotp")
	@ApiOperation(value = "generate Aadhar OTP", response = GenerateOTP.class)
	private ResponseEntity<?> engenerateAadharOTP(@RequestBody DataEncrypted encryptedCode) {
		LOGGER.info("generateAadharOTP() method aadharnumber : " + encryptedCode);
		return this.returnFinalResponse(base64Genarate.engeneratAadherOTP(encryptedCode),
				"Successfully Generated OTP to AADHAAR Linked Mobile Number");
	}

	@GetMapping("/generateaadharotp")
	@ApiOperation(value = "generate Aadhar OTP", response = GenerateOTP.class)
	private ResponseEntity<?> generateAadharOTP(@RequestParam("aadharnumber") String aadharnumber) {
		LOGGER.info("Aadharnumber : " + aadharnumber);

		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpStr = "";
		try {

			Base64.Decoder decoder = Base64.getDecoder();
			String dStr = new String(decoder.decode(aadharnumber));
			generatAadherOTP = base64Genarate.generatAadherOTP(dStr);
			if (generatAadherOTP != null && generatAadherOTP.getBody() != null
					&& generatAadherOTP.getBody().getData() != null) {
				GenerateOTP generat = (GenerateOTP) generatAadherOTP.getBody().getData();
				if (generat != null) {
					generateOtpStr = generat.getMobileNumber();
				}
			}
		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP,
				"Successfully Generated OTP to AADHAAR Linked Mobile Number " + generateOtpStr);
	}

	@GetMapping("/generatemobileotp")
	@ApiOperation(value = "generate Mobile OTP", response = GenerateOTP.class)
	private ResponseEntity<?> generateMobileOTP(@RequestParam("mobilenumber") String mobilenumber) {
		LOGGER.info("generateAadharOTP() method aadharnumber : " + mobilenumber);
		return this.returnFinalResponse(base64Genarate.generatmobileOTP(mobilenumber),
				"Successfully Generated OTP to Mobile No ");
	}

	@GetMapping("/generateencryptecode")
	@ApiOperation(value = "generate Encrypte Code", response = String.class)
	private ResponseEntity<?> getEncrypted(@RequestParam("otp") String otp) {
		LOGGER.info("Encrypt OTP" + otp);
		return this.returnFinalResponse(base64Genarate.getPublickey(otp), "Successfully Encrypted");
	}

	@PostMapping("/verifyotp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> verifyOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("verify otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.verifyAadhaarOTP(verifyOTP), "Verified OTP Successfully");
	}

	@PostMapping("/verifymobileotp")
	@ApiOperation(value = "verify Mobile OTP", response = UserDetails.class)
	private ResponseEntity<?> verifyMoblieOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("verify mobile otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.verifymoblieOTP(verifyOTP), "Verified Mobile OTP Successfully");
	}

	@PostMapping("/checkandgeneratemobileotp")
	@ApiOperation(value = "check And Generate Mobile OTP Record", response = CheckAndGenerateMobileOTP.class)
	private ResponseEntity<?> checkAndGenerateMobileOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("generate mobile otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.checkAndGenerateMobileOTP(verifyOTP),
				"Successfully Generated Mobile OTP");
	}

	@PostMapping("/confirmaadhar")
	@ApiOperation(value = "confirm Aadhar Record", response = HealthGererate.class)
	private ResponseEntity<?> confirmAadhar(@RequestBody ConfirmAadharDTO confirmAadharDTO) {
		LOGGER.info("confirm aadhar" + confirmAadharDTO);
		return this.returnFinalResponse(base64Genarate.confirmAadhar(confirmAadharDTO),
				"Successfully Confirmed Aadhaar");
	}

	@PostMapping("/createhealthidwithpreverified")
	@ApiOperation(value = "create health id with preverified", response = HealthGererate.class)
	private ResponseEntity<?> createHealthIdWithPreVerified(@RequestBody CreateHealthId createHealthId) {
		LOGGER.info("create health id details" + createHealthId);
		return this.returnFinalResponse(base64Genarate.createHealthIdWithPreVerified(createHealthId),
				"Successfully Created Health Id With Preverified");
	}

	@PostMapping("/generatemobileotp")
	@ApiOperation(value = "Generate Mobile OTP Record", response = GenerateMobileOTP.class)
	private ResponseEntity<?> GenerateMobileOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("mobile number" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.generateMobileOTP(verifyOTP),
				"Successfully Generated Mobile OTP");
	}

	@PostMapping("/getPngCard")
	@ApiOperation(value = "Generate png Record", response = GenerateMobileOTP.class)
	private ResponseEntity<?> getPngCard(@RequestBody CmToken cmToken, @RequestParam String healthIdNumber) {
		LOGGER.info("token" + cmToken);
		return this.returnFinalResponse(base64Genarate.getPngCard(cmToken, healthIdNumber),
				"Successfully Generated PHG card");
	}

	@PostMapping("/phrlinked")
	@ApiOperation(value = "update health id", response = PhrLinked.class)
	private ResponseEntity<?> phrLinked(@RequestBody PhrLinked phrLinked) {
		LOGGER.info("update health id" + phrLinked);
		return this.returnFinalResponse(base64Genarate.phrLinkedupdate(phrLinked), "PHR LINKED");
	}

	@GetMapping("/existsbyhealthid")
	@ApiOperation(value = "Generate exists By HealthId Record")
	private ResponseEntity<?> existsByHealthId(@RequestParam("healthId") String healthId) {
		LOGGER.info("exist by health id" + healthId);
		return this.returnFinalResponse(base64Genarate.existsByHealthId(healthId), "Exists by ABHA Id");
	}

	@PostMapping("/mobileverifyotp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> mobileverifyotp(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("verify otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.mobileverifyotp(verifyOTP), "Successfully Verified Mobile OTP");
	}

	@GetMapping("/searchbyhealthid")
	@ApiOperation(value = "search By HealthId", response = PhrLinked.class)
	private ResponseEntity<?> searchByHealthId(@RequestParam(value = "healthId") String healthId) {
		LOGGER.info("search by health id " + healthId);
		ResponseBean bean = new ResponseBean();
		try {
			bean = this.healthIdValidation(healthId);
			if (bean.getMessage()!=null && !bean.getMessage().isEmpty()) {
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}
			healthId = bean.getData().toString();
			ResponseEntity<ResponseBean> updatehealthid = base64Genarate.searchByHealthId(healthId);
			if (updatehealthid.getBody().getMessage() == null) {
				SearchByHealthId searchByHealthId = (SearchByHealthId) updatehealthid.getBody().getData();
				bean.setNdhmResponse(searchByHealthId);
				bean.setMessage("Successfully Fetched Modes for Authenticating");
				bean.setStatus(HttpStatus.OK);
				PatientRegistrationDTO beneficiaryabha = patientRegRepository.findByHealthIdorHealthNumber(healthId,
						healthId);
				if (beneficiaryabha != null) {
					bean.setStatus(HttpStatus.OK);
					bean.setMessage("Health id available in our local database also");
					bean.setData(beneficiaryabha);
				} else if (beneficiaryabha == null) {
					beneficiaryabha = patientRegRepository.findByHealthIdorHealthNumber(healthId, healthId);
					if (beneficiaryabha != null) {
						bean.setStatus(HttpStatus.OK);
						bean.setMessage("Health Id Available in our Local database Also");
						bean.setData(beneficiaryabha);
					} else {
						bean.setStatus(HttpStatus.OK);
						bean.setMessage("Health Id Not Available in our Local DataBase");
						bean.setData(beneficiaryabha);
					}
				}
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
			} else {
				return new ResponseEntity<ResponseBean>(updatehealthid.getBody(), updatehealthid.getStatusCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("Internal Server Error");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);

		}
	}

	private ResponseBean healthIdValidation(String healthId) {
		ResponseBean bean = new ResponseBean();
		bean.setData(healthId);
		if (healthId != null && !healthId.isEmpty()) {
			if (healthId.contains("-")) {
				if (healthId.length() == 17) {
					String[] split = healthId.split("-");
					if (split.length != 4 || (split[0].length() != 2 || split[1].length() != 4 || split[2].length() != 4
							|| split[3].length() != 4)) {
						bean.setMessage("Please Enter Valid Abha Number of 14 digits example :(12-3456-1891-0111)");
						bean.setStatus(HttpStatus.BAD_REQUEST);
					}
				} else {
					bean.setMessage("Please Enter Valid ABHA NUMBER of 14 digits example :(12-3456-1891-0111)");
					bean.setStatus(HttpStatus.BAD_REQUEST);
				}
			} else {
				if (NDHMM2CallBackController.isNumeric(healthId)) {
					if (healthId.length() == 14) {
						healthId = healthId.substring(0, 2) + "-" + healthId.substring(2, 6) + "-"
								+ healthId.substring(6, 10) + "-" + healthId.substring(10, 14);
						bean.setData(healthId);
					} else {
						bean.setMessage("Please Enter Valid Abha Address of 14 digits");
						bean.setStatus(HttpStatus.BAD_REQUEST);
					}
				} else {
					if (!healthId.contains("@")) {
						bean.setData(healthId.concat("@" + "sbx"));
					} else {
						int count = 0;
						for (int i = 0; i < healthId.length(); i++) {
							if (healthId.charAt(i) == '@') {
								++count;
							}
						}
						if (count < 1 || count > 1) {
							bean.setMessage("Please Enter Valid ABHA ADDRESS OR ABHA NUMBER");
							bean.setStatus(HttpStatus.BAD_REQUEST);
						}
					}
				}
			}
		} else {
			bean.setMessage("Please Enter Valid ABHA ADDRESS OR ABHA NUMBER");
			bean.setStatus(HttpStatus.BAD_REQUEST);
		}
		return bean;
	}

	@GetMapping("/authinit")
	@ApiOperation(value = "auth init", response = UserDetails.class)
	private ResponseEntity<?> authinit(@RequestParam("authMethod") String authMethod,
			@RequestParam("healthId") String healthId) {
		LOGGER.info("auth init : " + authMethod + healthId);
		return this.returnFinalResponse(base64Genarate.authinit(authMethod, healthId), "Auth Init OTP Successfully");
	}

	@PostMapping("/confirmwithaadharotp")
	@ApiOperation(value = "confirm with aadharotp", response = AccessToken.class)
	private ResponseEntity<?> confirmWithAadharOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("confirm with aadhar otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.confirmWithAadharOTP(verifyOTP),
				" Successfully Verified AADHAAR Otp");
	}

	@PostMapping("/confirmwithmobileotp")
	@ApiOperation(value = "confirm with mobile otp", response = AccessToken.class)
	private ResponseEntity<?> confirmWithMobileOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("confirm with mobile otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.confirmWithMobileOTP(verifyOTP),
				"Successfully Verified MOBILE Otp");
	}

	@PostMapping("/loginviapassword")
	@ApiOperation(value = "login via password", response = AccessToken.class)
	private ResponseEntity<?> loginViaPassword(@RequestBody NdhmPassword password) {
		LOGGER.info("login via passoword : " + password);
		return this.returnFinalResponse(base64Genarate.confirmViaPassword(password), "Successfully Verified Password");
	}

	@PostMapping("/getaccountprofile")
	@ApiOperation(value = "Generate png Record", response = AccountProfile.class)
	private ResponseEntity<?> getProfile(@RequestBody CmToken cmtoken) {
		LOGGER.info("profile details : " + cmtoken);
		return this.returnFinalResponse(base64Genarate.accountProfileNew(cmtoken),
				"Successfully fetched ABHA profile Details");
	}

	@PostMapping("/getaccountqrcode")
	@ApiOperation(value = "Generate png Record", response = AccountProfile.class)
	private Object getQrCode(@RequestBody CmSessions cmSessions) {
		LOGGER.info("account qr code : " + cmSessions);
		return this.returnFinalResponse(base64Genarate.getQrCode(cmSessions), "Successfully fetched QR Code");
	}

	@GetMapping("/forgothealthidgenerateaadharotp")
	@ApiOperation(value = "generate Aadhar OTP", response = GenerateOTP.class)
	private ResponseEntity<?> forgotGenerateAadharOTP(@RequestParam("aadharnumber") String aadharnumber) {
		LOGGER.info("generateAadharOTP() method aadharnumber : " + aadharnumber);
		return this.returnFinalResponse(base64Genarate.forgothealthidgeneratAadherOTP(aadharnumber),
				"Successfully Generated OTP to AADHAAR linked Mobile No");
	}

	@GetMapping("/forgothealthidgeneratemobileotp")
	@ApiOperation(value = "generate Mobile OTP", response = GenerateOTP.class)
	private ResponseEntity<?> forgotGenerateMobileOTP(@RequestParam("mobilenumber") String mobilenumber) {
		LOGGER.info("generate mobile otp : " + mobilenumber);
		return this.returnFinalResponse(base64Genarate.forgotgeneratmobileOTP(mobilenumber),
				"Successfully Generated OTP to Mobile No");
	}

	@PostMapping("/forgotverifyaadharotp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> forgotaadharverifyOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("Aadhaar verify otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.forgotaadharverifyOTP(verifyOTP),
				"Verified AADHAAR Otp Successfully ");
	}

	@PostMapping("/verifythroughdemographicdetails")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> verifyDemographic(@RequestBody DemographicDetails demographicDetails) {
		LOGGER.info("demographic details : " + demographicDetails);
		return this.returnFinalResponse(base64Genarate.verifyDemographic(demographicDetails),
				"Verified Demography Details SuccessFully");
	}

	@PostMapping("/forgotverifymobileotp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> forgothealthidverifymobileotp(@RequestBody ForgotVerifyMobileOtp forgotVerifyMobileOtp) {
		LOGGER.info("validate mobile otp, forgot abha details : " + forgotVerifyMobileOtp);
		return this.returnFinalResponse(base64Genarate.forgotverifyMobileotp(forgotVerifyMobileOtp),
				"Verified OTP Successfully");
	}

	@GetMapping("/forgothealthidgenerateaadharotps")
	@ApiOperation(value = "generate Mobile OTP", response = GenerateOTP.class)
	private ResponseEntity<?> forgotGenerateaadharOTP(@RequestParam("aadharnumber") String aadharnumber) {
		LOGGER.info("generate mobile otp : " + aadharnumber);
		Base64.Decoder decoder = Base64.getDecoder();
		String dStr = new String(decoder.decode(aadharnumber));
		return this.returnFinalResponse(base64Genarate.forgotgenerataadharOTP(dStr), "OTP Generated Successfully");
	}

	@PostMapping("/forgotverifyaadharotps")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> forgotaadharverifyOTP2(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("aadhar verify otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.forgotaadharverifyOTP2(verifyOTP),
				"Verified AADHAAR OTP Successfully");
	}

	@PostMapping("aadhaar/resend-aadhaar-otp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> resendAadhaarOtp(@RequestBody ResendAadharOtpOrMobileOtp verifyOTP) {
		LOGGER.info("aadhar verify otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.resendAadhaarOtp(verifyOTP), "AADHAAR OTP Resent Successfully");
	}

	@PostMapping("mobile/resend-mobile-otp")
	@ApiOperation(value = "verify OTP", response = UserDetails.class)
	private ResponseEntity<?> resendMobileOtp(@RequestBody ResendAadharOtpOrMobileOtp verifyOTP) {
		LOGGER.info("aadhar verify otp : " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.resendMobileOtp(verifyOTP), "MOBILE OTP Resent Successfully");
	}

	public ResponseEntity<ResponseBean> returnFinalResponse(ResponseEntity<ResponseBean> respData, String message) {
		if (respData.getBody().getMessage() == null) {
			ResponseBean bean = new ResponseBean();
			bean.setData(respData.getBody().getData());
			bean.setMessage(message);
			if (respData.getBody().getStatus() == null) {
				bean.setStatus(HttpStatus.OK);
			} else {
				bean.setStatus(respData.getBody().getStatus());
			}
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} else {
			return new ResponseEntity<ResponseBean>(respData.getBody(), respData.getStatusCode());
		}
	}

	@PostMapping("link/profile-details")
	private ResponseEntity<?> linkProfileDetails(@RequestBody ResendAadharOtpOrMobileOtp authToken) {
		LOGGER.info("LINK PROFILE DETAILS : " + authToken);
		return this.returnFinalResponse(base64Genarate.linkProfileDetails(authToken.getAuthToken()),
				"LINK PROFILE DETAILS SUCCESSFULL");
	}

	@PostMapping("abha-address/suggestions")
	private ResponseEntity<?> abhaAddressSuggestions(@RequestBody ResendAadharOtpOrMobileOtp transactionId) {
		LOGGER.info("abhaAddressSuggestions for : " + transactionId);
		return this.returnFinalResponse(base64Genarate.abhaAddressSuggestions(transactionId.getTxnId()),
				"ABHA ADDRESS SUGGESTIONS SUCCESSFULL");
	}

	@PostMapping("create/phr/address")
	private ResponseEntity<?> createPhrAddress(@RequestBody CreatePhrAddressPayload phrAddress) {
		return this.returnFinalResponse(base64Genarate.createPhrAddress(phrAddress),
				"PHR ADDRESS CREATED SUCCESSFULLY");
	}
	
	@GetMapping("/test")
	private String testMyCode() {
		List<AbhaQueryTable> requestQueries = jdbcTemplateHelper.getResults(
				"select * from public.abha_query_table where  tenant_id = " + TenantContext.getCurrentTenant()
						+ " and query_type in ('CONSULTATION-WITH-DATE-RANGE-FOR-REQUEST','LAB-REPORTS-WITH-DATE-RANGE-FOR-REQUEST','RADIOLOGY-REPORTS-WITH-DATE-RANGE-FOR-REQUEST')",
				AbhaQueryTable.class);
		if(!requestQueries.isEmpty()) {
			requestQueries.forEach(s-> System.out.println(s.toString()));
		}
		
		List<RequestResultSetDto> executeDynamicQuery = testDynamicQueryRepository.executeDynamicQueryFromDBForRequest("SELECT ot.op_triage_id as result_id,pd.mrn_no as patient_referencenumber,'' as file_path,case when (pd.patient_middle_name = '' or pd.patient_middle_name is null) then concat(pd.patient_first_name,' ',pd.patient_last_name) else concat(pd.patient_first_name,' ',pd.patient_middle_name,' ',pd.patient_last_name) end as patient_display ,dcid as careContexts_referenceNumber,concat(d.firstname,' ',d.middlename,' ',d.lastname)  as doctor ,odc.created_date as consultation_date, ot.patient_id,'OPCONSULTATION' as report_type, concat('CONSULTATION_DATE : '||to_char(OT.created_date::::date,'dd-mm-yyyy')) as careContexts_display from op_doctor_consultation odc  join op_triage ot  on ot.op_triage_id= odc.op_triage_id  join patient_demography pd  on pd.patient_id =odc.patient_id left join doctors d on d.doctor_id =odc.doctor_id");
		if(!executeDynamicQuery.isEmpty()) {
			executeDynamicQuery.forEach(s-> System.out.println(s.toString()));
		}
		return "";
	}
	
	

}
