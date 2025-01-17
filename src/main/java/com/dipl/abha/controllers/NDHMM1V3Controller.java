package com.dipl.abha.controllers;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.dto.BiometricPaylod;
import com.dipl.abha.dto.CmToken;
import com.dipl.abha.dto.CreatePhrAddressPayload;
import com.dipl.abha.dto.DemographicPayload;
import com.dipl.abha.dto.DrivingLicenceDetails;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.UserVerifyDto;
import com.dipl.abha.dto.VerifyOTP;
import com.dipl.abha.service.Base64Genarate;


@RestController
@RequestMapping(value = "m1/v3/enrollment")
@CrossOrigin
public class NDHMM1V3Controller {

	@Autowired
	private Base64Genarate base64Genarate;
	
	
	

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM1V3Controller.class);

	@PostMapping("/requestaadharotp")
	private ResponseEntity<?> generateAadharOTPV3(@RequestParam("aadharnumber") String aadharnumber) {
		LOGGER.info("Aadharnumber : " + aadharnumber);

		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpMessageStr = "";
		try {
			generatAadherOTP = base64Genarate.generatAadherOTPV3(aadharnumber);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) generatAadherOTP.getBody().getData();
			generateOtpMessageStr = (String) hashMap.get("message");

		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP, generateOtpMessageStr);
	}

	@PostMapping("/Aadhaarverifyotp")
	private ResponseEntity<?> verifyOTPV3(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("verify otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.verifyAadhaarOTPV3(verifyOTP), "Verified OTP Successfully");
	}

	@PostMapping("/mobilenumberupdaterequestotp")
	private ResponseEntity<?> mobileNumberUpdate(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("MobileNumberUpdate : ======>  " + verifyOTP);

		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpMessageStr = "";
		try {
			generatAadherOTP = base64Genarate.mobileNumberUpdateV3(verifyOTP);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) generatAadherOTP.getBody().getData();
			generateOtpMessageStr = (String) hashMap.get("message");

		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP, generateOtpMessageStr);
	}

	@PostMapping("/updateverifyotp")
	private ResponseEntity<?> mobileNumberUpdateVerifyOTP(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("mobileNumberUpdateVerifyOTP() Method Strtad ======= >  " + verifyOTP);
		return this.returnFinalResponse(base64Genarate.mobileNumberUpdateVerifyOTPV3(verifyOTP),
				"Verified OTP Successfully");
	}

	@PostMapping("/dlrequestotp")
	private ResponseEntity<?> genarateMobileOtpOfDl(@RequestParam("mobilenumber") String mobilenumber) {
		LOGGER.info("MobileNumber ===== " + mobilenumber);
		ResponseEntity<ResponseBean> generatMobileOTPOfDl = base64Genarate.generateMobileOTPOfDl(mobilenumber);
		;

		return this.returnFinalResponse(generatMobileOTPOfDl,
				"Successfully Generated OTP to Mobile No To Generate the ABHA Through DL");

	}

	@PostMapping("/bydrivinglicenseverifyotp")
	private ResponseEntity<?> bydrivingLicenseverifyOTPV3(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("verify otp" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.verifybyDrivingLicenseOTPV3(verifyOTP),
				"Verified OTP Successfully");
	}

	@PostMapping("/enrolabhabydrivinglicense")
	private ResponseEntity<?> verifyDLDocument(@RequestBody DrivingLicenceDetails details) {
		LOGGER.info("Driving Licence Details ====>" + details);
		return this.returnFinalResponse(base64Genarate.enrolAbhaWithDL(details),
				"Your ABHA Enroled Successfully By DrivingLicence");
	}

	@GetMapping("/ahbaaddresssuggestions")
	private ResponseEntity<?> addressSuggestions(@RequestParam("transactionid") String transactionid) {
		LOGGER.info("Transaction_Id ====== >" + transactionid);
		return this.returnFinalResponse(base64Genarate.abhaAddressSuggestionsV3(transactionid),
				"Your Ahba Address Successfully Suggestions Generated");
	}

	@PostMapping("/enrolcustomabhaaddress")
	private ResponseEntity<?> customAbhaAddress(@RequestBody CreatePhrAddressPayload addressPayload) {
		LOGGER.info("Selected Adresss ====== >" + addressPayload);
		return this.returnFinalResponse(base64Genarate.customAbhaAddressV3(addressPayload),
				"Your Ahba Address Successfully Generated");
	}

	@PostMapping("/getabhaprofile")
	private ResponseEntity<?> getAbhaProfile(@RequestBody CmToken cmToken) {
		LOGGER.info("getAbhaProfile() Started" + cmToken.getToken());
		return this.returnFinalResponse(base64Genarate.getAbhaProfileV3(cmToken), "Please find your abha profile");
	}

	@PostMapping("/getabhacard")
	private ResponseEntity<?> getAbhaCard(@RequestBody CmToken cmToken) {
		LOGGER.info("getAbhaCard() Started =======>  " + cmToken.getToken());
		return this.returnFinalResponse(base64Genarate.getAbhaCardV3(cmToken), "Abha Card Successfully Generated");
	}

	@GetMapping("/isexistsabhaaddress")
	private ResponseEntity<?> isExistsAbhaAddress(@RequestParam("abhaaddress") String abhaaddress) {
		LOGGER.info("abhaaddress ====== >" + abhaaddress);
		return this.returnFinalResponse(base64Genarate.isExistsAbhaAddressV3(abhaaddress),
				"This Ahba Address Not Exists You Can Set This :- " + abhaaddress);
	}

	@PostMapping("/abha/account/search")
	private ResponseEntity<?> acccountSearch(@RequestParam("mobilenumber") String mobilenumber) {
		LOGGER.info("Mobile Number ======= >" + mobilenumber);
		ResponseEntity<ResponseBean> bean = base64Genarate.findAbhaAccount(mobilenumber);
		return this.returnFinalResponse(bean, "please have a look on your Abha Number");
	}

	@PostMapping("/search/auth/methods")
	private ResponseEntity<?> searchAuthMethods(@RequestParam("abhaAddress") String abhaAddress) {
		LOGGER.info("AbhaAddress ======= >" + abhaAddress);
		ResponseEntity<ResponseBean> bean = base64Genarate.searchAuthMethodsV3(abhaAddress);
		return this.returnFinalResponse(bean, "please have a look on your Abha");
	}

	@PostMapping("/profile/login/request/otp")
	private ResponseEntity<?> loginRequestOtp(@RequestParam("authMethod") String authMethod,
			@RequestParam("authMethodTypeId") String authMethodTypeId,
			@RequestParam("transactionid") String transactionid) {
		LOGGER.info("loginRequestOtp Method Started ======= >  authMethod " + authMethod + " == authMethodTypeId == "
				+ authMethodTypeId + " == transactionid == " + transactionid);
		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpMessageStr = "";
		try {
			generatAadherOTP = base64Genarate.loginRequestV3(authMethod, authMethodTypeId, transactionid);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) generatAadherOTP.getBody().getData();
			generateOtpMessageStr = (String) hashMap.get("message");

		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP, generateOtpMessageStr);
	}

	@PostMapping("/requestotpforverify")
	private ResponseEntity<?> requestOtp(@RequestParam("authMethod") String authMethod,
			@RequestParam("abhaAddress") String abhaAddress) {
		LOGGER.info("requestOtp Method Started ======= >  authMethod " + authMethod + " == abhaAddress == "
				+ abhaAddress);
		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpMessageStr = "";
		try {
			generatAadherOTP = base64Genarate.requestOtpV3(authMethod, abhaAddress);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) generatAadherOTP.getBody().getData();
			generateOtpMessageStr = (String) hashMap.get("message");

		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP, generateOtpMessageStr);
	}

	@PostMapping("/profile/login/verify")
	private ResponseEntity<?> loginVerify(@RequestBody VerifyOTP verifyOTP) {
		LOGGER.info("loginVerify() Started" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.profileLoginVerify(verifyOTP),
				"Profile Login Verified OTP Successfully");
	}

	@PostMapping("/confirmwithotp")
	private ResponseEntity<?> loginAbhaVerify(@RequestBody VerifyOTP verifyOTP,
			@RequestParam("authMethod") String authMethod) {
		LOGGER.info("loginAbhaVerify() Started" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.loginAbhaVerify(verifyOTP, authMethod),
				"Login ABHA OTP Verified  Successfully");
	}

	@PostMapping("/getabhaprofileforverifyflow")
	private ResponseEntity<?> getAbhaProfileForVerifyFlow(@RequestBody CmToken cmToken) {
		LOGGER.info("getAbhaProfile() Started" + cmToken.getToken());
		return this.returnFinalResponse(base64Genarate.getAbhaProfileForVerifyFlowV3(cmToken),
				"Please find your abha profile");
	}

	@PostMapping("/getabhacardforverifyflow")
	private ResponseEntity<?> getAbhaCardForVerifyFlow(@RequestBody CmToken cmToken) {
		LOGGER.info("getAbhaCardForVerifyFlow() Started =======>  " + cmToken.getToken());
		return this.returnFinalResponse(base64Genarate.getAbhaCardForVerifyFlowV3(cmToken),
				"Abha Card For Verify Flow Successfully Generated");
	}

	@PostMapping("/forgotabharequestotp")
	private ResponseEntity<?> forgotAbhaRequestToOTP(@RequestParam("authMethod") String authMethod,
			@RequestParam("authMethodTypeId") String authMethodTypeId) {
		LOGGER.info("forgotAbhaRequestToOTP ======= >  authMethod " + authMethod + " == authMethodTypeId == "
				+ authMethodTypeId);
		ResponseEntity<ResponseBean> generatAadherOTP = null;
		String generateOtpMessageStr = "";
		try {
			generatAadherOTP = base64Genarate.forgotAbhaRequestToOTPV3(authMethod, authMethodTypeId);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) generatAadherOTP.getBody().getData();
			generateOtpMessageStr = (String) hashMap.get("message");

		} catch (Exception e) {
			ResponseBean bean = new ResponseBean();
			bean.setMessage("Exception failed");
			bean.setData(e.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);

			return this.returnFinalResponse(new ResponseEntity<>(bean, bean.getStatus()), "Something went wrong");
		}

		return this.returnFinalResponse(generatAadherOTP, generateOtpMessageStr);
	}

	@PostMapping("/forgotabhaverifyotp")
	private ResponseEntity<?> forgotAbhaVerifyOTP(@RequestBody VerifyOTP verifyOTP,
			@RequestParam("authMethod") String authMethod) {
		LOGGER.info("forgotAbhaVerifyOTP() Started" + verifyOTP);
		return this.returnFinalResponse(base64Genarate.forgotAbhaVerifyOTPV3(verifyOTP, authMethod),
				"YOUR ABHA FOUND SUCCESSFULLY");
	}
	@PostMapping("/verifyUser")
	private ResponseEntity<?> verifyUserV3(@RequestBody UserVerifyDto userVerifyDto){
		LOGGER.info("UserVerifyDto() Started  ========> "+userVerifyDto);
		return this.returnFinalResponse(base64Genarate.verifyUserV3(userVerifyDto),"YOUR ABHA SUCCESSFULLY CREATED BY DEMOGRAPHIC DETAILS" );
	}
	@PostMapping("/verify/demographic")
	private ResponseEntity<?> verifyDemographic(@RequestBody DemographicPayload demographicPayload){
		LOGGER.info("verifyDemographic() Started  ========> "+demographicPayload);
		return this.returnFinalResponse(base64Genarate.verifyDemographicV3(demographicPayload),"YOUR ABHA SUCCESSFULLY CREATED BY DEMOGRAPHIC DETAILS" );
	}
    @PostMapping("/enrol/abha/bybiometric")
    private ResponseEntity<?> enrolAbhaByBiometric(@RequestBody BiometricPaylod biometricPaylod){
    	LOGGER.info("enrolAbhaByBiometric() Started  ========> "+biometricPaylod);
		return this.returnFinalResponse(base64Genarate.enrolAbhaByBiometricV3(biometricPaylod),"YOUR ABHA SUCCESSFULLY CREATED BY BIOMETRIC" );
    }
	@PostMapping("/searchbyabhaAddress")
	private ResponseEntity<?> searchByHelthId(@RequestParam("abhaAddress") String abhaAddress){
		ResponseBean bean = new ResponseBean();
		try {
			bean = this.healthIdValidation(abhaAddress);
			if (bean.getMessage()!=null && !bean.getMessage().isEmpty()) {
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}
			abhaAddress = bean.getData().toString();
			ResponseEntity<ResponseBean> updatehealthid = base64Genarate.searchByAbhaAddress(abhaAddress);
//			if (updatehealthid.getBody().getMessage() == null) {
//				bean.setNdhmResponse(updatehealthid.getBody().getData());
//				bean.setMessage("Successfully Fetched Modes for Authenticating");
//				bean.setStatus(HttpStatus.OK);
//				PatientRegistrationDTO beneficiaryabha = patientRegRepository.findByHealthIdorHealthNumber(abhaAddress,
//						abhaAddress);
//				if (beneficiaryabha != null) {
//					bean.setStatus(HttpStatus.OK);
//					bean.setMessage("Health id available in our local database also");
//					bean.setData(beneficiaryabha);
//				} else if (beneficiaryabha == null) {
//					beneficiaryabha = patientRegRepository.findByHealthIdorHealthNumber(abhaAddress, abhaAddress);
//					if (beneficiaryabha != null) {
//						bean.setStatus(HttpStatus.OK);
//						bean.setMessage("Health Id Available in our Local database Also");
//						bean.setData(beneficiaryabha);
//					} else {
//						bean.setStatus(HttpStatus.OK);
//						bean.setMessage("Health Id Not Available in our Local DataBase");
//						bean.setData(beneficiaryabha);
//					}
//				}
			//	return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
			//} else {
			return new ResponseEntity<ResponseBean>(updatehealthid.getBody(), updatehealthid.getStatusCode());
		//}

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
	public ResponseEntity<ResponseBean> returnFinalResponse(ResponseEntity<ResponseBean> respData, String message) {
		if (respData.getBody().getMessage() == null) {
			ResponseBean bean = new ResponseBean();
			bean.setData(respData.getBody().getData());
			bean.setMessage(message);
			if (respData.getBody().getStatus() == null) {
				bean.setStatus(HttpStatus.OK);
			} else if (respData.getBody().getData() == (Object) true) {
				bean.setMessage("This Ahba Address Already Exists");
			} else {
				bean.setStatus(respData.getBody().getStatus());
			}
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} else {
			return new ResponseEntity<ResponseBean>(respData.getBody(), respData.getStatusCode());
		}
	}
	
	

}
