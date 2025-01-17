package com.dipl.abha.service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Cipher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.AccountProfile;
import com.dipl.abha.dto.BiometricPaylod;
import com.dipl.abha.dto.CheckAndGenerateMobileOTP;
import com.dipl.abha.dto.CmSessions;
import com.dipl.abha.dto.CmToken;
import com.dipl.abha.dto.ConfirmAadharDTO;
import com.dipl.abha.dto.CreateHealthId;
import com.dipl.abha.dto.CreatePhrAddress;
import com.dipl.abha.dto.CreatePhrAddressPayload;
import com.dipl.abha.dto.DataEncrypted;
import com.dipl.abha.dto.DemographicDetails;
import com.dipl.abha.dto.DemographicPayload;
import com.dipl.abha.dto.DemographicResponse;
import com.dipl.abha.dto.DrivingLicenceDetails;
import com.dipl.abha.dto.ForgotAbhaVerifyMobileOtpResponse;
import com.dipl.abha.dto.ForgotHealthId;
import com.dipl.abha.dto.ForgotVerifyMobileOtp;
import com.dipl.abha.dto.GenerateMobileOTP;
import com.dipl.abha.dto.GenerateOTP;
import com.dipl.abha.dto.HealthGererate;
import com.dipl.abha.dto.LinkProfileDetails;
import com.dipl.abha.dto.NdhmPassword;
import com.dipl.abha.dto.PhrLinkStatus;
import com.dipl.abha.dto.PhrLinked;
import com.dipl.abha.dto.ResendAadharOtpOrMobileOtp;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.ResponseToken;
import com.dipl.abha.dto.SearchAbhaAccount;
import com.dipl.abha.dto.SearchByHealthId;
import com.dipl.abha.dto.SearchByHealthIdV3;
import com.dipl.abha.dto.UserDetails;
import com.dipl.abha.dto.UserVerifyDto;
import com.dipl.abha.dto.VerifyOTP;
import com.dipl.abha.entities.ABHAlogs;
import com.dipl.abha.repositories.ABHALogsRepository;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class Base64Genarate {
	@Value("${CLIENTSECRET}")
	private String clientSecret;
	@Value("${CLIENTID}")
	private String clientId;
	@Value("${GETSESSION}")
	private String getsession;
	@Value("${CERT}")
	private String cert;
	@Value("${AADHAARGENERATEOTP}")
	private String aadhaargenerateotp;
	@Value("${SEARCH-BY-ABHA-ADDRESS}")
	private String searchByAbhaAddress;
	@Value("${AADHAARGENERATEOTPV3}")
	private String aadhaargenerateotpv3;
	@Value("${MOBILEGENERATEOTP}")
	private String mobilegenerateotp;
	@Value("${MOBILEGENERATEOPTOFDL}")
	private String mobilegenerateoptofdl;
	@Value("${AADHAARVERIFYOTP}")
	private String aadhaarverifyotp;
	@Value("${AADHAARVERIFYOTPV3}")
	private String aadhaarverifyotpv3;
	@Value("${DRIVINGLICENSEREFERREDOTP}")
	private String drivinglicensereferredotp;
	@Value("${ENROLLINGABHAWITHDL}")
	private String enrollingabhawithdl;
	@Value("${VERIFYMOBILEOTP}")
	private String verifymobileotp;
	@Value("${CONFIRM}")
	private String confirm;
	@Value("${USER-VERIFY-V3}")
	private String verifyUserV3;
	@Value("${CHECKANDGENERATEMOBILEOTP}")
	private String checkandgeneratemobileotp;
	@Value("${CREATEHEALTHIDWITHPREVERIFIED}")
	private String createhealthidwithpreverified;
	@Value("${CONSENT}")
	private String consent;
	@Value("${CONSENTVERSION}")
	private String consentversion;
	@Value("${GENERATEMOBILEOTP}")
	private String generatemobileotp;
	@Value("${CMSESSIONS}")
	private String cmsessions;
	@Value("${GETPNGCARD}")
	private String getpngcard;
	@Value("${PHR-LINKED}")
	private String phrlinked;
	@Value("${EXISTSBYHEALTHID}")
	private String existsbyhealthid;
	@Value("${MOBILE_VERIFYOTP}")
	private String mobile_verifyotp;
	@Value("${SEARCHBYHEALTHID}")
	private String searchbyhealthid;
	@Value("${INIT}")
	private String init;
	@Value("${CONFIRMWITHAADHAROTP}")
	private String confirmwithaadharotp;
	@Value("${CONFIRMWITHMOBILEOTP}")
	private String confirmwithmobileotp;
	@Value("${AUTHPASSWORD}")
	private String authpassword;
	@Value("${ACCOUNTPROFILE}")
	private String accountprofile;
	@Value("${QRCODE}")
	private String qrcode;
	@Value("${FORGOT_GENERATE_AADHAR_OTP}")
	private String forgotgenerateaadharotp;
	@Value("${FORGOT_VERIFY_AADHAR_OTP}")
	private String forgotverifyaadharotp;
	@Value("${FORGOT_GENERATE_MOBILE_OTP}")
	private String forgotgeneratemobileotp;
	@Value("${FORGOT_VERIFY_MOBILE_OTP}")
	private String forgotverifymobileotp;
	@Value("${FORGOT_DEMOGRAPHIC}")
	private String forgotdemographic;
	@Value("${FORGOT_GENERATE_AADHAR_OTP2}")
	private String forgotgenerateaadharotp2;
	@Value("${FORGOT_VERIFY_AADHAR_OTP2}")
	private String forgotverifyaadharotp2;
	@Value("${RESEND-AADHAAR-OTP}")
	private String resendAadhaarOtpUrl;
	@Value("${RESEND-MOBILE-OTP}")
	private String resendMobileOtpUrl;
	@Value("${LINK-PROFILE-DETAILS}")
	private String linkProfileDetails;
	@Value("${ABHA-ADDRESS-SUGGESTIONS}")
	private String abhaAddressSuggestions;
	@Value("${ABHA-ADDRESS-SUGGESTIONS-V3}")
	private String abhaAddressSuggestionsV3;
	@Value("${CREATE-PHR-ADDRESS}")
	private String createPhrAddress;
	@Value("${CREATE-CUSTOM-ABHA-ADDRESS}")
	private String createcustomabhaaddress;
	@Value("${ISEXISTS-ABHA-ADDRESS}")
	private String isexistsabhaaddress;
	@Value("${SEARCH-ABHA-ACCOUNT}")
	private String searchabhaaccount;
	@Value("${LOGIN-REQUEST}")
	private String loginrequest;
	@Value("${LOGIN-PROFILE-OTP}")
	private String loginprofileotp;
	@Value("${GET-ABHA-PROFILE}")
	private String getabhaprofile;
	@Value("${SEARCH-AUTH-METHODS-V3}")
	private String searchauthmethodsV3;
	@Value("${REQUEST-OTP-V3}")
	private String requestotpV3;
	@Value("${LOGIN-ABHA-OTP-VERIFY}")
	private String loginabhaotpverify;
	@Value("${GET-ABHA-PROFILE-FOR-VERIFY-FLOW}")
	private String getabhaprofileforverifyflow;
	@Value("${GET-ABHA-CARD-V3}")
	private String getabhacardv3;
	@Value("${GET-ABHA-CARD-FOR-VERIFY-FLOW-V3}")
	private String getabhacardforverifyflowv3;
	@Value("${MOBILE-NUMBER-UPDATE-REQUEST-OTP-V3}")
	private String mobilenumberupdaterequestotpv3;
	@Value("${MOBILE-NUMBER-UPDATE-VERIFY-OTP-V3}")
	private String mobilenumberupdateverifyotpv3;
	@Value("${FORGOT-ABHA-REQUEST-TO-OTP-V3}")
	private String forgotabharequesttootpv3;
	@Value("${FORGOT-ABHA-VERIFY-OTP-V3}")
	private String forgotabhaverifyotpv3;
	@Value ("${DEMOGRAPHIC-V3}")
	private String demographicv3;
	@Value ("${ENROL-ABHA-BY-BIOMETRIC-V3}")
	private String enrolabhabybiometricv3;
//	@Autowired
//	private AllAbdmCentralDbSave allAbdmCentralDbSaveService;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private RestTemplate template;
	@Autowired
	private ABHALogsRepository abhaLogRepository;
	
	private static final String AES_ALGORITHM = "HEX";

	private static final String SECRET_KEY = "VMED-DHANUSH@!@#";

	List<AccessToken> accesTokenList = new LinkedList<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(Base64Genarate.class);
	
	public  String decrypt(String encryptedText) throws Exception {
//		SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES_ALGORITHM);
//		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//		cipher.init(Cipher.DECRYPT_MODE, secretKey);
//		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
//		return new String(decryptedBytes);
		// Convert the hex-encoded string to bytes
		 byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
	        
	        // Convert the decoded bytes to a String
	        return new String(decodedBytes);
	}

	public static String encrypt(String data, String publicKey) {
		LOGGER.info("encrypt() method date = " + data + " publicKey = " + publicKey);
		String encodeToString = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
			encodeToString = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		} catch (Exception exception) {
			LOGGER.error("encrypt() method error: " + exception);
			exception.printStackTrace();
		}
		return encodeToString;
	}

	public static String encryptForV3(String data, String publicKey) {
		LOGGER.info("encrypt() method date = " + data + " publicKey = " + publicKey);
		String encodeToString = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
			encodeToString = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		} catch (Exception exception) {
			LOGGER.error("encrypt() method error: " + exception);
			exception.printStackTrace();
		}
		return encodeToString;
	}

	public static PublicKey getPublicKey(String base64PublicKey) {
		LOGGER.info("getPublicKey() method base64PublicKey = " + base64PublicKey);
		PublicKey publicKey = null;
		try {
			byte[] decode = Base64.getDecoder().decode(base64PublicKey.replace("\n", "").replace("\r", ""));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public AccessToken generateToken() {
		AccessToken convertValue = null;
		try {
			CertSession certSession = new CertSession();
			certSession.setClientId(clientId);
			certSession.setClientSecret(clientSecret);
			ResponseEntity<String> postForEntity = template.postForEntity(getsession, certSession, String.class);
			if (postForEntity != null) {
				convertValue = mapper.readValue(postForEntity.getBody().toString(), AccessToken.class);
			}
		} catch (Exception exception) {
			LOGGER.error("generateToken() method error: " + exception);
			exception.printStackTrace();
		}
		return convertValue;
	}

	public CmToken cmgenerateToken(CmSessions cmSessions) {
		CmToken converCmToken = null;
		try {
			AccessToken convertValue = generateToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", cmSessions.getUsername());
			jsonObject.put("password", cmSessions.getPassword());
			jsonObject.put("grantType", cmSessions.getGrantType());
			HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
			ResponseEntity<String> postForEntity = template.postForEntity(cmsessions, entity, String.class);
			converCmToken = mapper.readValue(postForEntity.getBody().toString(), CmToken.class);
		} catch (Exception exception) {
			LOGGER.error("generateToken() method error: " + exception);
			exception.printStackTrace();
		}
		return converCmToken;
	}

	public ResponseEntity<ResponseBean> getPublickey(String encryptedcode) {
		ResponseBean bean = new ResponseBean();
		LOGGER.info("getPublicKey(String) method encryptedcode = " + encryptedcode);
		String encrypt = null;
		try {
			encrypt = encrypt(encryptedcode, template.getForEntity(cert, String.class).getBody()
					.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").trim());
			LOGGER.info("encryptedcode = " + encrypt);
		} catch (Exception exception) {
			LOGGER.error("getPublickey() method error: " + exception);
			bean.setData(null);
			bean.setMessage("getPublickey() method" + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		bean.setData(encrypt);
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	public ResponseEntity<ResponseBean> getPublickeyForV3(String encryptedcode) {
		ResponseBean bean = new ResponseBean();
		LOGGER.info("getPublicKey(String) method encryptedcode = " + encryptedcode);
		String encrypt = null;
		try {
			encrypt = encryptForV3(encryptedcode, template.getForEntity(cert, String.class).getBody()
					.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").trim());
			LOGGER.info("encryptedcode = " + encrypt);
		} catch (Exception exception) {
			LOGGER.error("getPublickey() method error: " + exception);
			bean.setData(null);
			bean.setMessage("getPublickey() method" + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		bean.setData(encrypt);
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	public ResponseEntity<ResponseBean> engeneratAadherOTP(DataEncrypted encryptedCode) {
		LOGGER.info("generatAadherOTP() method aadharnumber = " + encryptedCode);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("aadhaar", encryptedCode.getEncryptedCode());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, aadhaargenerateotp, jsonObject, "GENERATE AADHAAR OTP"),
					"GENERATE AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("engeneratAadherOTP() method error: " + exception);
			bean.setMessage(exception.getMessage());
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> generatAadherOTP(String aadharnumber) {
		LOGGER.info("generatAadherOTP() method aadharnumber = " + aadharnumber);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("aadhaar", getPublickey(aadharnumber).getBody().getData());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, aadhaargenerateotp, jsonObject, "GENERATE AADHAAR OTP"),
					"GENERATE AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("generatAadherOTP() method error: " + exception);
			bean.setData(null);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> generatAadherOTPV3(String aadharnumber) {
		LOGGER.info("generatAadherOTP() method aadharnumber = " + aadharnumber);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			array.put("abha-enrol");
			jsonObject.put("txnId", "");
			jsonObject.put("scope", array);
			jsonObject.put("loginHint", "aadhaar");
			jsonObject.put("loginId", getPublickeyForV3(this.decrypt(aadharnumber)).getBody().getData());
			jsonObject.put("otpSystem", "aadhaar");
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, aadhaargenerateotpv3, jsonObject, "GENERATE AADHAAR OTP FOR V3"),
					"GENERATE AADHAAR OTP FOR V3", "");
		} catch (Exception exception) {
			LOGGER.error("generatAadherOTP() method error: " + exception);
			bean.setData(null);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> returnSuccessOrFailure(ResponseEntity<ResponseBean> respData, String apiType,
			String healthIdNumber) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		try {
			if (respData.getStatusCode() == HttpStatus.OK || respData.getStatusCode() == HttpStatus.ACCEPTED) {
				switch (apiType) {
				case "GENERATE AADHAAR OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), GenerateOTP.class));
					break;
				case "GENERATE AADHAAR OTP FOR V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "ENROL ABHA BY BIOMETRIC V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
					
				case "SEARCH-BY-ABHA-ADDRESS":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "MOBILE NUMBER UPDATE REQUEST OTP V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "DEMOGRAPHIC V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "FORGOT ABHA OTP VERYFY V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "FORGOT AHBA REQUEST TO OTP V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "GENERATE MOBILE OTP OF DL":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "GET ABHA PROFILE":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "GET ABHA PROFILE FOR VERIFY FLOW":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "LOGIN ABHA OTP VERYFY V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "MOBILE NUMBER UPDATE VERIFY OTP V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
					
				case "USER-VERIFY-V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
					
				case "VERYFY LOGIN OTP FOR V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "Search Abha Account":
					List<SearchAbhaAccount> abhaAccounts = mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<List<SearchAbhaAccount>>() {
							});
					LOGGER.info("Search Abha Account Details" + mapper.writeValueAsString(abhaAccounts));
					bean.setData(abhaAccounts);
					break;
				case "Search Auth Methods":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<SearchByHealthIdV3>() {
							}));
					break;
				case "LogIn Request V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "Request OTP V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "IsExists AbhaAddress":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), PhrLinkStatus.class));
					break;
				case "GENERATE MOBILE OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), GenerateOTP.class));
					break;
				case "VERYFY AADHAAR OTP":
					UserDetails userDetails = mapper.readValue(respData.getBody().getData().toString(),
							UserDetails.class);
					bean.setData(this.buildFirstNameLastName(userDetails));
					break;
				case "VERYFY AADHAAR OTP FOR V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "VERIFY MOBILE OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), UserDetails.class));
					break;
				case "VERYFY DrivingLicense Referred Number OTP FOR V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "ABHA ADDRESS SUGGESTIONS V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "CUSTOM ABHA ADDRESS V3":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "VERYFYING DRIVING LICENSE DOCUMENT TO ENROL ABHA":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							new TypeReference<HashMap<String, Object>>() {
							}));
					break;
				case "CHECK AND GENERATE MOBILE OTP":
					bean.setData(
							mapper.readValue(respData.getBody().getData().toString(), CheckAndGenerateMobileOTP.class));
					break;
				case "GET PNG CARD":
					String enCryptedBytes = Base64.getEncoder().encodeToString(
							mapper.readValue(mapper.writeValueAsString(respData.getBody().getData()), byte[].class));
					bean.setData(enCryptedBytes);
					break;

				case "GET ABHA CARD FOR VERIFY FLOW V3":
					String enCryptedCardBytes = Base64.getEncoder().encodeToString(
							mapper.readValue(mapper.writeValueAsString(respData.getBody().getData()), byte[].class));
					bean.setData(enCryptedCardBytes);
					break;
				case "GET ABHA CARD V3":
					String enCryptedCardBytes2 = Base64.getEncoder().encodeToString(
							mapper.readValue(mapper.writeValueAsString(respData.getBody().getData()), byte[].class));
					bean.setData(enCryptedCardBytes2);
					break;
				case "PHR LINKED":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), PhrLinkStatus.class));
					break;
				case "EXISTS BY ABHA ID":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), PhrLinkStatus.class));
					break;
				case "CREATE ABHA":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), HealthGererate.class));
					break;
				case "SEARCH BY HEALTH ID":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), SearchByHealthId.class));
					break;
				case "CONFIRM WITH AADHAAR OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), ResponseToken.class));
					break;
				case "CONFIRM WITH MOBILE OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), ResponseToken.class));
					break;
				case "FORGOT ABHA CONFIRM WITH MOBILE OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							ForgotAbhaVerifyMobileOtpResponse.class));
					break;
				case "GET ACCOUNT PROFILE":
//					AccountProfile profile = mapper.readValue(respData.getBody().getData().toString(),
//							AccountProfile.class);
//					this.processDataAndStoreInCentralDb(profile);
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), AccountProfile.class));
					break;
				case "GET QR CODE":
					bean.setData(respData.getBody().getData().toString().getBytes());
					break;
				case "VERIFY AADHAR OTP IF FORGOT ABHA":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), ForgotHealthId.class));
					break;
				case "VERIFY DEMOGRAPHY DETAILS":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), DemographicResponse.class));
					break;
				case "FORGOT ABHA VERIFY AADHAR OTP 2":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), ForgotHealthId.class));
					break;
				case "GENERATE MOBILE OTP GET TXN ID":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), GenerateMobileOTP.class));
					break;
				case "CONFIRM AADHAAR OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), HealthGererate.class));
					break;
				case "AUTH INIT":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), GenerateOTP.class));
					break;
				case "RESEND AADHAAR OTP":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(),
							ResendAadharOtpOrMobileOtp.class));
					break;
				case "RESEND MOBILE OTP":
					bean.setData(respData.getBody());
					break;

				case "LINK PROFILE DETAILS":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), LinkProfileDetails.class));
					break;
				case "ABHA ADDRESS SUGGESTIONS":
					// bean.setData(mapper.readValue(respData.getBody().getData().toString(),
					// ArrayList.class));
					bean.setData(respData.getBody().getData());
					break;

				case "CREATE PHR ADDRESS":
					bean.setData(mapper.readValue(respData.getBody().getData().toString(), CreatePhrAddress.class));
					break;

				}

			} else {
				bean = new ResponseBean(respData.getStatusCode(), respData.getBody().getMessage(), null,
						respData.getBody().getData().toString());
			}
		} catch (JsonMappingException e) {
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}
	
//	private void processDataAndStoreInCentralDb(AccountProfile profile)
//			throws NumberFormatException, JsonProcessingException {
//		allAbdmCentralDbSaveService.saveAbhaRegistration(Integer.parseInt(TenantContext.getCurrentTenant()),
//				"ABHA Registration", profile.getHealthIdNumber(), profile.getHealthId(),
//				mapper.writeValueAsString(profile), 1L, profile.getGender(), Integer.parseInt(profile.getYearOfBirth()),
//				profile.getMobile());
//
//	}

	private ABHAlogs saveAbhaLogsAndHealthCard(String enCryptedBytes, String healthIdNumber) {
		ABHAlogs abhaLogs = new ABHAlogs();
		abhaLogs.setAbhaStatusId((int) 1);
		abhaLogs.setBeneficiaryId(null);
		abhaLogs.setCreatedOn(LocalDateTime.now());
		abhaLogs.setHealthIdNumber(healthIdNumber);
		abhaLogs.setIsActive(true);
		abhaLogs.setPngBytes(enCryptedBytes);
		abhaLogs.setIsDeleted(false);
		abhaLogs.setHealthCardPath("");
		ABHAlogs saveAbhaLog = abhaLogRepository.save(abhaLogs);
		return saveAbhaLog;
	}

	private UserDetails buildFirstNameLastName(UserDetails userDetails) {
		String[] split = userDetails.getName().split(" ");
		if (split.length == 1) {
			userDetails.setFirstName(split[0]);
		}
		if (split.length == 2) {
			userDetails.setFirstName(split[0]);
			userDetails.setLastName(split[1]);
		}
		if (split.length == 3) {
			userDetails.setFirstName(split[0]);
			userDetails.setMiddleName(split[1]);
			userDetails.setLastName(split[2]);
		}
		if (split.length == 4) {
			userDetails.setFirstName(split[0]);
			userDetails.setMiddleName(split[1]);
			userDetails.setLastName(split[2] + " " + split[3]);
		}
		if (split.length == 5) {
			userDetails.setFirstName(split[0] + " " + split[1]);
			userDetails.setMiddleName(split[2]);
			userDetails.setLastName(split[3] + " " + split[4]);
		}
		if (split.length == 6) {
			userDetails.setFirstName(split[0] + " " + split[1]);
			userDetails.setMiddleName(split[2] + " " + split[3]);
			userDetails.setLastName(split[4] + " " + split[5]);
		}
		if (split.length == 7) {
			userDetails.setFirstName(split[0] + " " + split[1]);
			userDetails.setMiddleName(split[2] + " " + split[3]);
			userDetails.setLastName(split[4] + " " + split[5] + " " + split[6]);
		}
		if (split.length == 8) {
			userDetails.setFirstName(split[0] + " " + split[1]);
			userDetails.setMiddleName(split[2] + " " + split[3]);
			userDetails.setLastName(split[4] + " " + split[5] + " " + split[6]);
		}
		if (split.length == 9) {
			userDetails.setFirstName(split[0] + " " + split[1] + " " + split[2]);
			userDetails.setMiddleName(split[3] + " " + split[4] + " " + split[5]);
			userDetails.setLastName(split[6] + " " + split[7] + " " + split[8]);
		}
		return userDetails;
	}

	public ResponseEntity<ResponseBean> generatmobileOTP(String mobilenumber) {
		LOGGER.info("generatmobileOTP() method mobilenumber = " + mobilenumber);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mobile", mobilenumber);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, mobilegenerateotp, jsonObject, "GENERATE MOBILE OTP"), "GENERATE MOBILE OTP",
					"");
		} catch (Exception exception) {
			LOGGER.error("generatmobileOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> generateMobileOTPOfDl(String mobilenumber) {
		LOGGER.info("generateMobileOTPOfDl method ==== >" + mobilenumber);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();

			// Add the "scope" array first so it appears at the top
			JSONArray scopeArray = new JSONArray();
			scopeArray.put("abha-enrol");
			scopeArray.put("mobile-verify");
			scopeArray.put("dl-flow");
			jsonObject.put("scope", scopeArray);

			// Add the remaining key-value pairs
			jsonObject.put("loginHint", "mobile");
			jsonObject.put("loginId", getPublickeyForV3(this.decrypt(mobilenumber)).getBody().getData());

			jsonObject.put("otpSystem", "abdm");
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, mobilegenerateoptofdl, jsonObject, "GENERATE MOBILE OTP OF DL"),
					"GENERATE MOBILE OTP OF DL", "");

		} catch (Exception e) {
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	public ResponseEntity<ResponseBean> verifyAadhaarOTP(VerifyOTP verifyOTP) {
		LOGGER.info("verifyAadhaarOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			LOGGER.info(
					"verifyAadhaarOTP encripted = " + getPublickey(verifyOTP.getOtp()).getBody().getData().toString());
			jsonObject.put("otp", getPublickey(verifyOTP.getOtp()).getBody().getData());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, aadhaarverifyotp, jsonObject, "VERYFY AADHAAR OTP"), "VERYFY AADHAAR OTP",
					"");
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> verifyAadhaarOTPV3(VerifyOTP verifyOTP) {
		LOGGER.info("verifyAadhaarOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = null;
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));
			LOGGER.info("verifyAadhaarOTP encripted = "
					+ getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
			// Creating the authData JSONObject
			JSONObject authData = new JSONObject();
			authData.put("authMethods", new JSONArray().put("otp"));

			// Creating the otp JSONObject
			JSONObject otp = new JSONObject();
			String currentTimestamp = Instant.now().toString();
			otp.put("timeStamp", currentTimestamp);
			otp.put("txnId", verifyOTP.getTxnId());
			otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());
			otp.put("mobile", verifyOTP.getMobile());

			// Adding otp to authData
			authData.put("otp", otp);

			// Creating the consent JSONObject
			JSONObject consent = new JSONObject();
			consent.put("code", "abha-enrollment");
			consent.put("version", "1.4");

			// Creating the final payload JSONObject
			JSONObject payload = new JSONObject();
			payload.put("authData", authData);
			payload.put("consent", consent);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, aadhaarverifyotpv3, payload, "VERYFY AADHAAR OTP FOR V3"),
					"VERYFY AADHAAR OTP FOR V3", "");
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> mobileNumberUpdateV3(VerifyOTP verifyOTP) {
		LOGGER.info("generateMobileOTPOfDl method ==== >" + verifyOTP);
		ResponseBean bean = null;
		try {
			verifyOTP.setMobile(this.decrypt(verifyOTP.getMobile()));			
			JSONObject jsonObject = new JSONObject();

			// Add the "scope" array first so it appears at the top
			JSONArray scopeArray = new JSONArray();
			scopeArray.put("abha-enrol");
			scopeArray.put("mobile-verify");

			jsonObject.put("scope", scopeArray);
			jsonObject.put("txnId", verifyOTP.getTxnId());
			// Add the remaining key-value pairs
			jsonObject.put("loginHint", "mobile");
			jsonObject.put("loginId", getPublickeyForV3(verifyOTP.getMobile()).getBody().getData());
			jsonObject.put("otpSystem", "abdm");
			return this.returnSuccessOrFailure(this.restCallApiM1V3(null, mobilenumberupdaterequestotpv3, jsonObject,
					"MOBILE NUMBER UPDATE REQUEST OTP V3"), "MOBILE NUMBER UPDATE REQUEST OTP V3", "");

		} catch (Exception e) {
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> mobileNumberUpdateVerifyOTPV3(VerifyOTP verifyOTP) {
		LOGGER.info("mobileNumberUpdateVerifyOTPV3() method verifyOTP = " + verifyOTP);
		ResponseBean bean = null;
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));			
			LOGGER.info("mobileNumberUpdateVerifyOTPV3 encripted = "
					+ getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
			// Creating the authData JSONObject
			JSONObject authData = new JSONObject();
			authData.put("authMethods", new JSONArray().put("otp"));

			JSONArray array = new JSONArray();
			array.put("abha-enrol");
			array.put("mobile-verify");

			// Creating the otp JSONObject
			JSONObject otp = new JSONObject();
			String currentTimestamp = Instant.now().toString();
			otp.put("timeStamp", currentTimestamp);
			otp.put("txnId", verifyOTP.getTxnId());
			otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

			// Adding otp to authData
			authData.put("otp", otp);

			// Creating the final payload JSONObject
			JSONObject payload = new JSONObject();
			payload.put("scope", array);
			payload.put("authData", authData);
			return this.returnSuccessOrFailure(this.restCallApiM1V3(null, mobilenumberupdateverifyotpv3, payload,
					"MOBILE NUMBER UPDATE VERIFY OTP V3"), "MOBILE NUMBER UPDATE VERIFY OTP V3", "");
		} catch (Exception exception) {
			LOGGER.error("verifybyDrivingLicenseOTP method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> verifybyDrivingLicenseOTPV3(VerifyOTP verifyOTP) {
		LOGGER.info("verifybyDrivingLicenseOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = null;
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));
			LOGGER.info("verifybyDrivingLicenseOTP encripted = "
					+ getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
			// Creating the authData JSONObject
			JSONObject authData = new JSONObject();
			authData.put("authMethods", new JSONArray().put("otp"));

			JSONArray array = new JSONArray();
			array.put("abha-enrol");
			array.put("mobile-verify");
			array.put("dl-flow");

			// Creating the otp JSONObject
			JSONObject otp = new JSONObject();
			String currentTimestamp = Instant.now().toString();
			otp.put("timeStamp", currentTimestamp);
			otp.put("txnId", verifyOTP.getTxnId());
			otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

			// Adding otp to authData
			authData.put("otp", otp);

			// Creating the consent JSONObject
			JSONObject consent = new JSONObject();
			consent.put("code", "abha-enrollment");
			consent.put("version", "1.4");

			// Creating the final payload JSONObject
			JSONObject payload = new JSONObject();
			payload.put("scope", array);
			payload.put("authData", authData);
			payload.put("consent", consent);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, drivinglicensereferredotp, payload,
							"VERYFY DrivingLicense Referred Number OTP FOR V3"),
					"VERYFY DrivingLicense Referred Number OTP FOR V3", "");
		} catch (Exception exception) {
			LOGGER.error("verifybyDrivingLicenseOTP method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> enrolAbhaWithDL(DrivingLicenceDetails details) {
		LOGGER.info("enrolAbhaWhithDL() method DrivingLicenceDetails = " + details);
		ResponseBean bean = null;
		try {
			// Creating the consent JSONObject
			JSONObject consent = new JSONObject();
			consent.put("code", "abha-enrollment");
			consent.put("version", "1.4");

			// Creating the final payload JSONObject
			JSONObject payload = new JSONObject();
			payload.put("txnId", details.getTxnId());
			payload.put("documentType", "DRIVING_LICENCE");
			payload.put("documentId", details.getDocumentId());
			payload.put("firstName", details.getFirstName());
			payload.put("middleName", details.getMiddleName());
			payload.put("lastName", details.getLastName());
			payload.put("dob", details.getDob());
			payload.put("gender", details.getGender());
			payload.put("frontSidePhoto", details.getFrontSidePhoto());
			payload.put("backSidePhoto", details.getBackSidePhoto());
			payload.put("address", details.getAddress());
			payload.put("state", details.getState());
			payload.put("district", details.getDistrict());
			payload.put("pinCode", details.getPinCode());
			payload.put("consent", consent);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, enrollingabhawithdl, payload,
							"VERYFYING DRIVING LICENSE DOCUMENT TO ENROL ABHA"),
					"VERYFYING DRIVING LICENSE DOCUMENT TO ENROL ABHA", "");

		} catch (Exception exception) {
			LOGGER.error("enrolAbhaWhithDL() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	public ResponseEntity<ResponseBean> abhaAddressSuggestionsV3(String transactionid) {
		LOGGER.info("abhaAddressSuggestions = " + transactionid);
		ResponseBean bean = null;
		try {
			JSONObject payload = new JSONObject();
			payload.put("txnId", transactionid);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, abhaAddressSuggestionsV3, payload, "ABHA ADDRESS SUGGESTIONS V3"),
					"ABHA ADDRESS SUGGESTIONS V3", "");
		} catch (Exception exception) {
			LOGGER.error("abhaAddressSuggestions() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> customAbhaAddressV3(CreatePhrAddressPayload addressPayload) {
		LOGGER.info("abhaAddressPayload  = " + addressPayload);
		ResponseBean bean = null;
		try {
			JSONObject payload = new JSONObject();
			payload.put("txnId", addressPayload.getTransactionId());
			payload.put("abhaAddress", addressPayload.getPhrAddress());
			payload.put("preferred", "1");
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, createcustomabhaaddress, payload, "CUSTOM ABHA ADDRESS V3"),
					"CUSTOM ABHA ADDRESS V3", "");
		} catch (Exception exception) {
			LOGGER.error("abhaAddressSuggestions() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	public ResponseEntity<ResponseBean> isExistsAbhaAddressV3(String abhaaddress) {
		LOGGER.error("isExistsAbhaAddress() method error: " + abhaaddress);
		ResponseBean bean = null;
		try {
			JSONObject payload = new JSONObject();
			payload.put("abhaaddress", abhaaddress);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, isexistsabhaaddress, payload, "IsExists AbhaAddress"),
					"IsExists AbhaAddress", "");
		} catch (Exception exception) {
			LOGGER.error("isExistsAbhaAddress() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> findAbhaAccount(String mobilenumber) {
		LOGGER.error("isExistsAbhaAddress() started: " + mobilenumber);
		ResponseBean bean = null;
		try {
			JSONArray array = new JSONArray();
			array.put("search-abha");
			JSONObject payload = new JSONObject();
			payload.put("scope", array);
			payload.put("mobile", getPublickeyForV3(mobilenumber).getBody().getData());
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, searchabhaaccount, payload, "Search Abha Account"),
					"Search Abha Account", "");
		} catch (Exception exception) {
			LOGGER.error("findAbhaAccount() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> searchAuthMethodsV3(String abhaAddress) {
		LOGGER.error("isExistsAbhaAddress() started: " + abhaAddress);
		ResponseBean bean = null;
		try {
			JSONObject payload = new JSONObject();
			payload.put("abhaAddress", abhaAddress);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, searchauthmethodsV3, payload, "Search Auth Methods"),
					"Search Auth Methods", "");
		} catch (Exception exception) {
			LOGGER.error("findAbhaAccount() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> loginRequestV3(String authMethod, String authMethodTypeId,
			String transactionid) {
		ResponseBean bean = null;
		int methodId = Integer.parseInt(authMethod);
		try {
			JSONArray array = new JSONArray();
			array.put("abha-login");
			array.put("search-abha");
			array.put("mobile-verify");
			JSONObject payload = new JSONObject();
			payload.put("scope", array);
			if (methodId == 1) {
				payload.put("loginHint", "mobile");
			} else if (methodId == 2) {
				payload.put("loginHint", "aadhaar");
			} else if (methodId == 3) {
				payload.put("loginHint", "abha-number");
			} else {
				payload.put("loginHint", "email");
			}
			payload.put("loginId", getPublickeyForV3(authMethodTypeId).getBody().getData());
			payload.put("otpSystem", "abdm");
			payload.put("txnId", transactionid);
			return returnSuccessOrFailure(this.restCallApiM1V3(null, loginrequest, payload, "LogIn Request V3"),
					"LogIn Request V3", "");
		} catch (Exception exception) {
			LOGGER.error("findAbhaAccount() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	public ResponseEntity<ResponseBean> requestOtpV3(String authMethod, String abhaAddress) {
		ResponseBean bean = null;
		int methodId = Integer.parseInt(authMethod);
		try {
			if (methodId == 1) {
				JSONArray array = new JSONArray();
				array.put("abha-address-login");
				array.put("mobile-verify");
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("loginHint", "abha-address");
				payload.put("loginId", getPublickeyForV3(abhaAddress).getBody().getData());
				payload.put("otpSystem", "abdm");
				return returnSuccessOrFailure(this.restCallApiM1V3(null, requestotpV3, payload, "Request OTP V3"),
						"Request OTP V3", "");
			} else {
				JSONArray array = new JSONArray();
				array.put("abha-address-login");
				array.put("aadhaar-verify");
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("loginHint", "abha-address");
				payload.put("loginId", getPublickeyForV3(abhaAddress).getBody().getData());
				payload.put("otpSystem", "aadhaar");
				return returnSuccessOrFailure(this.restCallApiM1V3(null, requestotpV3, payload, "Request OTP V3"),
						"Request OTP V3", "");
			}

		} catch (Exception exception) {
			LOGGER.error("findAbhaAccount() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	public ResponseEntity<ResponseBean> profileLoginVerify(VerifyOTP verifyOTP) {
		ResponseBean bean = null;
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));
			LOGGER.info("OTP encripted = " + getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
			// Creating Scope
			JSONArray array = new JSONArray();
			array.put("abha-login");
			array.put("mobile-verify");

			// Creating the authData JSONObject
			JSONObject authData = new JSONObject();
			authData.put("authMethods", new JSONArray().put("otp"));

			// Creating the otp JSONObject
			JSONObject otp = new JSONObject();
			otp.put("txnId", verifyOTP.getTxnId());
			otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

			// Adding otp to authData
			authData.put("otp", otp);

			// Creating the final payload JSONObject
			JSONObject payload = new JSONObject();
			payload.put("scope", array);
			payload.put("authData", authData);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, loginprofileotp, payload, "VERYFY LOGIN OTP FOR V3"),
					"VERYFY LOGIN OTP FOR V3", "");
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> loginAbhaVerify(VerifyOTP verifyOTP, String authMethod) {
		ResponseBean bean = null;
		int methodId = Integer.parseInt(authMethod);
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));
			if (methodId == 1) {
				LOGGER.info("OTP encripted = " + getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
				// Creating Scope
				JSONArray array = new JSONArray();
				array.put("abha-address-login");
				array.put("mobile-verify");

				// Creating the authData JSONObject
				JSONObject authData = new JSONObject();
				authData.put("authMethods", new JSONArray().put("otp"));

				// Creating the otp JSONObject
				JSONObject otp = new JSONObject();
				otp.put("txnId", verifyOTP.getTxnId());
				otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

				// Adding otp to authData
				authData.put("otp", otp);

				// Creating the final payload JSONObject
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("authData", authData);
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, loginabhaotpverify, payload, "LOGIN ABHA OTP VERYFY V3"),
						"LOGIN ABHA OTP VERYFY V3", "");
			} else {
				LOGGER.info("OTP encripted = " + getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
				// Creating Scope
				JSONArray array = new JSONArray();
				array.put("abha-address-login");
				array.put("aadhaar-verify");

				// Creating the authData JSONObject
				JSONObject authData = new JSONObject();
				authData.put("authMethods", new JSONArray().put("otp"));

				// Creating the otp JSONObject
				JSONObject otp = new JSONObject();
				otp.put("txnId", verifyOTP.getTxnId());
				otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

				// Adding otp to authData
				authData.put("otp", otp);

				// Creating the final payload JSONObject
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("authData", authData);
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, loginabhaotpverify, payload, "LOGIN ABHA OTP VERYFY V3"),
						"LOGIN ABHA OTP VERYFY V3", "");
			}
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> getAbhaProfileV3(CmToken cmToken) {
		ResponseBean bean = null;
		try {
			ResponseEntity<ResponseBean> entity = this.restCallApiM1V3(cmToken, getabhaprofile, null,
					"GET ABHA PROFILE");
			LOGGER.info("entity======== >" + entity);
			return this.returnSuccessOrFailure(entity, "GET ABHA PROFILE", "");
		} catch (Exception exception) {
			LOGGER.error("getAbhaProfileV3 method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> getAbhaCardV3(CmToken cmToken) {
		LOGGER.info("getAbhaCardV3() Method Started ======> " + cmToken);
		ResponseBean bean = null;
		try {
			ResponseEntity<ResponseBean> entity = this.restCallApiM1V3(cmToken, getabhacardv3, null,
					"GET ABHA CARD V3");
			LOGGER.info("entity======== >" + entity);
			return this.returnSuccessOrFailure(entity, "GET ABHA CARD V3", "");
		} catch (Exception exception) {
			LOGGER.error("getAbhaCardV3 method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> getAbhaProfileForVerifyFlowV3(CmToken cmToken) {
		LOGGER.info("getAbhaProfileForVerifyFlowV3() Method Started  ====>  " + cmToken);
		ResponseBean bean = null;
		try {
			ResponseEntity<ResponseBean> entity = this.restCallApiM1V3(cmToken, getabhaprofileforverifyflow, null,
					"GET ABHA PROFILE FOR VERIFY FLOW");
			LOGGER.info("entity======== >" + entity);
			return this.returnSuccessOrFailure(entity, "GET ABHA PROFILE FOR VERIFY FLOW", "");
		} catch (Exception exception) {
			LOGGER.error("getAbhaProfileV3 method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> getAbhaCardForVerifyFlowV3(CmToken cmToken) {
		LOGGER.info("getAbhaCardForVerifyFlowV3() Method Started ======> " + cmToken);
		ResponseBean bean = null;
		try {
			ResponseEntity<ResponseBean> entity = this.restCallApiM1V3(cmToken, getabhacardforverifyflowv3, null,
					"GET ABHA CARD FOR VERIFY FLOW V3");
			LOGGER.info("entity======== >" + entity);
			return this.returnSuccessOrFailure(entity, "GET ABHA CARD FOR VERIFY FLOW V3", "");
		} catch (Exception exception) {
			LOGGER.error("getAbhaCardForVerifyFlowV3() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotAbhaRequestToOTPV3(String authMethod, String authMethodTypeId) {
		LOGGER.error(
				"forgotAbhaRqequestToOTPV3() started: ======= > " + authMethod + " <==========> " + authMethodTypeId);
		ResponseBean bean = null;
		int methodId = Integer.parseInt(authMethod);
		try {
			if (methodId == 1) {
				JSONArray array = new JSONArray();
				array.put("abha-login");
				array.put("mobile-verify");
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("loginHint", "mobile");
				payload.put("loginId", getPublickeyForV3(this.decrypt(authMethodTypeId)).getBody().getData());
				payload.put("otpSystem", "abdm");
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, forgotabharequesttootpv3, payload, "FORGOT AHBA REQUEST TO OTP V3"),
						"FORGOT AHBA REQUEST TO OTP V3", "");
			} else {
				JSONArray array = new JSONArray();
				array.put("abha-login");
				array.put("aadhaar-verify");
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("loginHint", "aadhaar");
				payload.put("loginId", getPublickeyForV3(this.decrypt(authMethodTypeId)).getBody().getData());
				payload.put("otpSystem", "aadhaar");
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, forgotabharequesttootpv3, payload, "FORGOT AHBA REQUEST TO OTP V3"),
						"FORGOT AHBA REQUEST TO OTP V3", "");
			}
		} catch (Exception exception) {
			LOGGER.error("findAbhaAccount() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotAbhaVerifyOTPV3(VerifyOTP verifyOTP, String authMethod) {
		ResponseBean bean = null;
		int methodId = Integer.parseInt(authMethod);
		try {
			verifyOTP.setOtp(this.decrypt(verifyOTP.getOtp()));
			if (methodId == 1) {
				LOGGER.info("OTP encripted = " + getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
				// Creating Scope
				JSONArray array = new JSONArray();
				array.put("abha-login");
				array.put("mobile-verify");

				// Creating the authData JSONObject
				JSONObject authData = new JSONObject();
				authData.put("authMethods", new JSONArray().put("otp"));

				// Creating the otp JSONObject
				JSONObject otp = new JSONObject();
				otp.put("txnId", verifyOTP.getTxnId());
				otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

				// Adding otp to authData
				authData.put("otp", otp);

				// Creating the final payload JSONObject
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("authData", authData);
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, forgotabhaverifyotpv3, payload, "FORGOT ABHA OTP VERYFY V3"),
						"FORGOT ABHA OTP VERYFY V3", "");
			} else {
				LOGGER.info("OTP encripted = " + getPublickeyForV3(verifyOTP.getOtp()).getBody().getData().toString());
				// Creating Scope
				JSONArray array = new JSONArray();
				array.put("abha-login");
				array.put("aadhaar-verify");

				// Creating the authData JSONObject
				JSONObject authData = new JSONObject();
				authData.put("authMethods", new JSONArray().put("otp"));

				// Creating the otp JSONObject
				JSONObject otp = new JSONObject();
				otp.put("txnId", verifyOTP.getTxnId());
				otp.put("otpValue", getPublickeyForV3(verifyOTP.getOtp()).getBody().getData());

				// Adding otp to authData
				authData.put("otp", otp);

				// Creating the final payload JSONObject
				JSONObject payload = new JSONObject();
				payload.put("scope", array);
				payload.put("authData", authData);
				return this.returnSuccessOrFailure(
						this.restCallApiM1V3(null, forgotabhaverifyotpv3, payload, "FORGOT ABHA OTP VERYFY V3"),
						"FORGOT ABHA OTP VERYFY V3", "");
			}
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}
	public ResponseEntity<ResponseBean> verifyDemographicV3(DemographicPayload demographicPayload){
	ResponseBean bean=null;
	try{
		LOGGER.info("verifyDemographicV3() Started "+demographicPayload);
		JSONObject payload=new JSONObject();
		
		JSONObject authData = new JSONObject();
		
		authData.put("authMethods", new JSONArray().put("demo_auth"));
		
		JSONObject demoAuth = new JSONObject();
        demoAuth.put("aadhaarNumber",demographicPayload.getAadhaarNumber());
        demoAuth.put("districtCode",demographicPayload.getDistrictCode());
        demoAuth.put("stateCode", demographicPayload.getStateCode());
        demoAuth.put("dateOfBirth", demographicPayload.getDateOfBirth());
        demoAuth.put("gender", demographicPayload.getGender());
        demoAuth.put("name", demographicPayload.getFullName());
        demoAuth.put("mobile", demographicPayload.getMobileNumber());
        
        // Add demo_auth object to authData
        authData.put("demo_auth", demoAuth);
        
        // Add authData to the main payload
        payload.put("authData", authData);
        
        // Create the consent object
        JSONObject consent = new JSONObject();
        consent.put("code", "abha-enrollment");
        consent.put("version", "1.4");
        
        // Add consent to the main payload
        payload.put("consent", consent);

		return this.returnSuccessOrFailure(this.restCallApiM1V3(null,demographicv3, payload, "DEMOGRAPHIC V3"), "DEMOGRAPHIC V3", "");
		
	} catch (Exception exception) {
		LOGGER.error("verifyAadhaarOTP() method error: " + exception);
		bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
				exception.getMessage(), null);
		exception.printStackTrace();
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
	}
	}
	
	
	public ResponseEntity<ResponseBean> enrolAbhaByBiometricV3(BiometricPaylod biometricPaylod){
		ResponseBean bean=null;
		try{
			LOGGER.info("enrolAbhaByBiometricV3() Started =======> "+biometricPaylod);
			JSONObject payload=new JSONObject();
			
			JSONObject authData = new JSONObject();
			
			authData.put("authMethods", new JSONArray().put("bio"));
			
			JSONObject demoAuth = new JSONObject();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
			demoAuth.put("timeStamp",currentTimestamp);
	        demoAuth.put("aadhaar",getPublickeyForV3(biometricPaylod.getAadhaarNumber()).getBody().getData());
	        demoAuth.put("fingerPrintAuthPid", biometricPaylod.getFingerPrintAuthPidl());
	        demoAuth.put("mobile", biometricPaylod.getMobileNumber());
	        
	        // Add demo_auth object to authData
	        authData.put("bio", demoAuth);
	        
	        // Add authData to the main payload
	        payload.put("authData", authData);
	        
	        // Create the consent object
	        JSONObject consent = new JSONObject();
	        consent.put("code", "abha-enrollment");
	        consent.put("version", "1.4");
	        
	        // Add consent to the main payload
	        payload.put("consent", consent);

			return this.returnSuccessOrFailure(this.restCallApiM1V3(null,enrolabhabybiometricv3, payload, "ENROL ABHA BY BIOMETRIC V3"), "ENROL ABHA BY BIOMETRIC V3", "");
			
		} catch (Exception exception) {
			LOGGER.error("verifyAadhaarOTP() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		}
	

	public ResponseEntity<ResponseBean> confirmAadhar(ConfirmAadharDTO confirmAadharDTO) {
		LOGGER.info("confirmAadhar() method confirmAadharDTO = " + confirmAadharDTO);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("consent", consent);
			jsonObject.put("consentVersion", consentversion);
			jsonObject.put("txnId", confirmAadharDTO.getTxnId());
			return this.returnSuccessOrFailure(this.restCallApi(null, confirm, jsonObject, "CONFIRM AADHAAR OTP"),
					"CONFIRM AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("confirmAadhar() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> checkAndGenerateMobileOTP(VerifyOTP verifyOTP) {
		LOGGER.info("checkAndGenerateMobileOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mobile", verifyOTP.getMobile());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, checkandgeneratemobileotp, jsonObject, "CHECK AND GENERATE MOBILE OTP"),
					"CHECK AND GENERATE MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("checkAndGenerateMobileOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> verifymoblieOTP(VerifyOTP verifyOTP) {
		LOGGER.info("verifymoblieOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("otp", getPublickey(verifyOTP.getOtp()).getBody().getData());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(restCallApi(null, verifymobileotp, jsonObject, "VERIFY MOBILE OTP"),
					"VERIFY MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("verifymoblieOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> createHealthIdWithPreVerified(CreateHealthId createHealthId) {
		LOGGER.info("createHealthIdWithPreVerified() method confirmAadharDTO = " + createHealthId);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", createHealthId.getEmail());
			jsonObject.put("firstName", createHealthId.getFirstName());
			jsonObject.put("healthId", createHealthId.getHealthId());
			jsonObject.put("lastName", createHealthId.getLastName());
			jsonObject.put("middleName", createHealthId.getMiddleName());
			jsonObject.put("password", createHealthId.getPassword());
			jsonObject.put("profilePhoto", createHealthId.getProfilePhoto());
			jsonObject.put("txnId", createHealthId.getTxnId());
			System.out.println(jsonObject.toString());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, createhealthidwithpreverified, jsonObject, "CREATE ABHA"), "CREATE ABHA",
					"");
		} catch (Exception exception) {
			LOGGER.error("createHealthIdWithPreVerified() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> generateMobileOTP(VerifyOTP verifyOTP) {
		LOGGER.info("GenerateMobileOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mobile", verifyOTP.getMobile());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, generatemobileotp, jsonObject, "GENERATE MOBILE OTP GET TXN ID"),
					"GENERATE MOBILE OTP GET TXN ID", "");
		} catch (Exception exception) {
			LOGGER.error("generateMobileOTP() line 523 method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}
	

	public ResponseEntity<ResponseBean> getPngCard(CmToken cmToken, String healthIdNumber) {
		ResponseBean bean = new ResponseBean();
		LOGGER.info("getPngCard() method verifyOTP = " + cmToken);
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("token", cmToken.getToken());
			return this.returnSuccessOrFailure(this.restCallApi(cmToken, getpngcard, jsonObject, "GET PNG CARD"),
					"GET PNG CARD", healthIdNumber);
		} catch (Exception exception) {
			LOGGER.error("getPngCard() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> phrLinkedupdate(PhrLinked phrLinked) {
		LOGGER.info("phrLinkedupdate() = " + phrLinked);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phrAddress", phrLinked.getPhrAddress());
			jsonObject.put("preferred", phrLinked.getPreferred());
			jsonObject.put("token", phrLinked.getUpdate_token());
			CmToken cmToken = new CmToken(phrLinked.getUpdate_token());
			return this.returnSuccessOrFailure(this.restCallApi(cmToken, phrlinked, jsonObject, "PHR LINKED"),
					"PHR LINKED", "");
		} catch (Exception exception) {
			LOGGER.error("phrLinkedupdate() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> existsByHealthId(String healthId) {
		LOGGER.info("existsByHealthId() = " + healthId);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("healthId", healthId);
			return this.returnSuccessOrFailure(restCallApi(null, existsbyhealthid, jsonObject, "EXISTS BY ABHA ID"),
					"EXISTS BY ABHA ID", "");
		} catch (Exception exception) {
			LOGGER.error("existsByHealthId() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> mobileverifyotp(VerifyOTP verifyOTP) {
		LOGGER.info("mobileverifyotp() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			LOGGER.info("verifyOTP encripted = " + getPublickey(verifyOTP.getOtp()).getBody().getData().toString());
			jsonObject.put("otp", getPublickey(verifyOTP.getOtp()).getBody().getData());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			LOGGER.info("mobileverifyotp() method verifyOTP = " + verifyOTP);
			return this.returnSuccessOrFailure(restCallApi(null, mobile_verifyotp, jsonObject, "VERIFY MOBILE OTP"),
					"VERIFY MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("mobileverifyotp() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> searchByHealthId(String healthId) {
		LOGGER.info("searchByHealthId() " + healthId);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("healthId", healthId);
			LOGGER.info("response" + healthId);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, searchbyhealthid, jsonObject, "SEARCH BY HEALTH ID"), "SEARCH BY HEALTH ID",
					"");
		} catch (Exception exception) {
			LOGGER.error("search by health Id() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> authinit(String authMethod, String healthId) {
		LOGGER.info("authinit authMethod and healthId");
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("authMethod", authMethod);
			jsonObject.put("healthid", healthId);
			return this.returnSuccessOrFailure(this.restCallApi(null, init, jsonObject, "AUTH INIT"), "AUTH INIT", "");
		} catch (Exception exception) {
			LOGGER.error("authinit() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> confirmWithAadharOTP(VerifyOTP verifyOTP) {
		LOGGER.info("Confirm With Aadhar OTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			LOGGER.info("confirmWithAadharOTP encripted = "
					+ getPublickey(verifyOTP.getOtp()).getBody().getData().toString());
			jsonObject.put("otp", verifyOTP.getOtp());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, confirmwithaadharotp, jsonObject, "CONFIRM WITH AADHAAR OTP"),
					"CONFIRM WITH AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("confirmWithAadharOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> confirmWithMobileOTP(VerifyOTP verifyOTP) {
		LOGGER.info("Confirm With Mobile OTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			LOGGER.info("confirmWithMobileOTP encripted = "
					+ getPublickey(verifyOTP.getOtp()).getBody().getData().toString());
			jsonObject.put("otp", verifyOTP.getOtp());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, confirmwithmobileotp, jsonObject, "CONFIRM WITH MOBILE OTP"),
					"CONFIRM WITH MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("confirmWithMobileOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> confirmViaPassword(NdhmPassword password) {
		LOGGER.info("confirmViaPassword() NdhmPassword password" + password);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("healthId", password.getHealthId());
			jsonObject.put("password", password.getPassword());
			return this.returnSuccessOrFailure(this.restCallApi(null, authpassword, jsonObject, ""),
					"CONFIRM WITH AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("confirmViaPassword() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> accountProfileNew(CmToken cmToken) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			return this.returnSuccessOrFailure(
					this.restCallApi(cmToken, accountprofile, jsonObject, "GET ACCOUNT PROFILE"), "GET ACCOUNT PROFILE",
					"");
		} catch (Exception exception) {
			LOGGER.error("generatAadherOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> getQrCode(CmSessions cmSessions) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			CmToken token = cmgenerateToken(cmSessions);
			return this.returnSuccessOrFailure(this.restCallApi(token, qrcode, jsonObject, "GET QR CODE"),
					"GET QR CODE", "");
		} catch (Exception exception) {
			LOGGER.error("getQrCode() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgothealthidgeneratAadherOTP(String aadharnumber) {
		LOGGER.info("generatAadherOTP() method aadharnumber = " + aadharnumber);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("aadhaar", getPublickey(aadharnumber).getBody().getData());
			LOGGER.info("abdm api = " + forgotgenerateaadharotp);
			LOGGER.info("aadhar encrypted code = " + getPublickey(aadharnumber).getBody().getData());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotgenerateaadharotp, jsonObject, "FORGOT HEALTH ID GEN AADHAAR OTP"),
					"GENERATE AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("forgothealthidgeneratAadherOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotgeneratmobileOTP(String mobilenumber) {
		LOGGER.info("generatmobileOTP() method mobilenumber = " + mobilenumber);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mobile", mobilenumber);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotgeneratemobileotp, jsonObject, "FORGOT HEALTH ID GEN MOBILE OTP"),
					"GENERATE MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("forgotgeneratmobileOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotaadharverifyOTP(VerifyOTP verifyOTP) {
		LOGGER.info("verifyOTP() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			LOGGER.info("verifyOTP encripted = " + getPublickey(verifyOTP.getOtp()).getBody().getData().toString());
			jsonObject.put("otp", getPublickey(verifyOTP.getOtp()).getBody().getData());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					restCallApi(null, forgotverifyaadharotp, jsonObject, "VERIFY AADHAR OTP IF FORGOT ABHA"),
					"VERIFY AADHAR OTP IF FORGOT ABHA", "");
		} catch (Exception exception) {
			LOGGER.error("verifyOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> verifyDemographic(DemographicDetails demographicDetails) {
		LOGGER.info("authinit authMethod and healthId");
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("gender", demographicDetails.getGender());
			jsonObject.put("mobile", demographicDetails.getMobile());
			jsonObject.put("name", demographicDetails.getName());
			jsonObject.put("yearOfBirth", demographicDetails.getYearOfBirth());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotdemographic, jsonObject, "VERIFY DEMOGRAPHY DETAILS"),
					"VERIFY DEMOGRAPHY DETAILS", "");
		} catch (Exception exception) {
			LOGGER.error("search by health Id() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotverifyMobileotp(ForgotVerifyMobileOtp forgotVerifyMobileOtp) {
		LOGGER.info("forgotverifyMobileotp() :" + forgotVerifyMobileOtp);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("dayOfBirth", forgotVerifyMobileOtp.getDayOfBirth());
			jsonObject.put("firstName", forgotVerifyMobileOtp.getFirstName());
			jsonObject.put("gender", forgotVerifyMobileOtp.getGender());
			jsonObject.put("lastName", forgotVerifyMobileOtp.getLastName());
			jsonObject.put("middleName", forgotVerifyMobileOtp.getMiddleName());
			jsonObject.put("monthOfBirth", forgotVerifyMobileOtp.getMonthOfBirth());
			jsonObject.put("name", forgotVerifyMobileOtp.getName());
			jsonObject.put("otp", forgotVerifyMobileOtp.getOtp());
//			jsonObject.put("status", forgotVerifyMobileOtp.getStatus());
			jsonObject.put("txnId", forgotVerifyMobileOtp.getTxnId());
			jsonObject.put("yearOfBirth", forgotVerifyMobileOtp.getYearOfBirth());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotverifymobileotp, jsonObject, "FORGOT ABHA CONFIRM WITH MOBILE OTP"),
					"FORGOT ABHA CONFIRM WITH MOBILE OTP", "");
		} catch (Exception exception) {
			LOGGER.error("forgotverifyMobileotp() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotgenerataadharOTP(String aadharnumber) {
		LOGGER.info("forgotgenerataadharOTP() method mobilenumber = " + aadharnumber);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("aadhaar", aadharnumber);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotgenerateaadharotp2, jsonObject, "GENERATE AADHAAR OTP IF FORGOT ABHA"),
					"GENERATE AADHAAR OTP", "");
		} catch (Exception exception) {
			LOGGER.error("generatAadherOTP() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> forgotaadharverifyOTP2(VerifyOTP verifyOTP) {
		LOGGER.info("forgotaadharverifyOTP2() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("otp", verifyOTP.getOtp());
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, forgotverifyaadharotp2, jsonObject, "FORGOT ABHA VERIFY AADHAR OTP 2"),
					"FORGOT ABHA VERIFY AADHAR OTP 2", "");
		} catch (Exception exception) {
			LOGGER.error("forgotaadharverifyOTP2() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> resendAadhaarOtp(ResendAadharOtpOrMobileOtp verifyOTP) {
		LOGGER.info("resendAadhaarOtp() method  = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, resendAadhaarOtpUrl, jsonObject, "RESEND AADHAAR OTP"), "RESEND AADHAAR OTP",
					"");
		} catch (Exception exception) {
			LOGGER.error("resendAadhaarOtp() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> resendMobileOtp(ResendAadharOtpOrMobileOtp verifyOTP) {
		LOGGER.info("resendMobileOtp() method verifyOTP = " + verifyOTP);
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("txnId", verifyOTP.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, resendMobileOtpUrl, jsonObject, "RESEND MOBILE OTP"), "RESEND MOBILE OTP",
					"");
		} catch (Exception exception) {
			LOGGER.error("forgotaadharverifyOTP2() method error: " + exception);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(exception.getMessage());
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> linkProfileDetails(String authToken) {
		LOGGER.info("linkProfileDetails = " + authToken);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("authToken", authToken);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, linkProfileDetails, jsonObject, "LINK PROFILE DETAILS"),
					"LINK PROFILE DETAILS", "");
		} catch (Exception exception) {
			LOGGER.error("linkProfileDetails() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> abhaAddressSuggestions(String transactionId) {
		LOGGER.info("abhaAddressSuggestions = " + transactionId);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("transactionId", transactionId);
			return this.returnSuccessOrFailure(
					this.restCallApi(null, abhaAddressSuggestions, jsonObject, "ABHA ADDRESS SUGGESTIONS"),
					"ABHA ADDRESS SUGGESTIONS", "");
		} catch (Exception exception) {
			LOGGER.error("abhaAddressSuggestions() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> createPhrAddress(CreatePhrAddressPayload createPhr)

	{
		LOGGER.info("CreatePhrAddress = " + createPhr);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phrAddress", createPhr.getPhrAddress());
			jsonObject.put("transactionId", createPhr.getTransactionId());
			return this.returnSuccessOrFailure(
					this.restCallApi(null, createPhrAddress, jsonObject, "CREATE PHR ADDRESS"), "CREATE PHR ADDRESS",
					"");
		} catch (Exception exception) {
			LOGGER.error("CreatePhrAddress() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	private ResponseEntity<ResponseBean> restCallApi(CmToken cmToken, String url, JSONObject jsonObject, String apiType)
			throws JsonMappingException, JsonProcessingException {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		ResponseEntity<String> postForEntity = null;
		ResponseEntity<byte[]> postForEntityBytes = null;
		try {
			HttpEntity<String> entity = null;
			AccessToken convertValue = generateToken();
			HttpHeaders headers = new HttpHeaders();
			if(jsonObject!=null) {
				LOGGER.info("Calling ==========>" + url);
				LOGGER.info("With this Payload =========> "+jsonObject.toString());
			}
				
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			if (cmToken != null && "PHR LINKED".equals(apiType)) {
				headers.set("X-HIP-ID", clientId);
				headers.set("X-Token", "Bearer " + cmToken.getToken());
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			} else if (cmToken != null && "GET PNG CARD".equals(apiType)) {
				headers.set("X-Token", "Bearer " + cmToken.getToken());
				entity = new HttpEntity<>(headers);
				postForEntityBytes = template.exchange(url, HttpMethod.GET, entity, byte[].class);
				bean.setData(postForEntityBytes.getBody());
				bean.setStatus(HttpStatus.OK);
			} else if (cmToken != null && "GET ACCOUNT PROFILE".equals(apiType)) {
				headers.set("X-Token", "Bearer " + cmToken.getToken());
				entity = new HttpEntity<>(headers);
				postForEntity = template.exchange(url, HttpMethod.GET, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(HttpStatus.OK);
				return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
			} else if (cmToken != null && !"GET PNG CARD".equals(apiType) && !"PHR LINKED".equals(apiType)) {
				headers.set("X-Token", "Bearer " + cmToken.getToken());
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			} else if (cmToken == null) {
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				System.out.println(jsonObject.toString());
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			}
			LOGGER.info("restCallApi, method response " + apiType);
		} catch (HttpClientErrorException exception) {
			mapper.setSerializationInclusion(Include.NON_NULL);
			LOGGER.error("restCallApi, method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				bean.setStatus(exception.getStatusCode());
				bean.setData(exception.getResponseBodyAsString());
				bean.setMessage("UN-AUTHORIZED -> TOKEN EXPIRED");
			} else {
				ErrorLog error = mapper.readValue(exception.getResponseBodyAsString(), ErrorLog.class);
				if (error != null) {
					if (error.getDetails() != null && !error.getDetails().isEmpty()) {
						String errorString = error.getDetails().stream().map(s -> s.getMessage())
								.collect(Collectors.joining(","));
						bean.setMessage(errorString);
					} else {
						bean.setMessage(error.getMessage());
					}
				}
				bean.setStatus(exception.getStatusCode());
				bean.setData(exception.getResponseBodyAsString());
			}
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (HttpServerErrorException exception) {
//			mapper.setSerializationInclusion(Include.NON_NULL);
//			LOGGER.error("restCallApi, method error HttpServerErrorException: " + exception.getResponseBodyAsString());
//			List<ErrorLog> error = mapper.readValue(
//					exception.getMessage()
//							.substring(exception.getMessage().indexOf(":") + 1, exception.getMessage().length()).trim(),
//					new TypeReference<List<ErrorLog>>() {
//					});
//			if (error != null) {
//				bean.setMessage(error.get(0).getMessage() + " " + "From ABDM End");
//
//			}
			bean.setMessage(exception.getResponseBodyAsString());
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			bean.setData(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (Exception exception) {
			LOGGER.error("restCallApi method error: " + exception);
			List<ErrorLog> error = mapper.readValue(
					exception.getMessage()
							.substring(exception.getMessage().indexOf(":") + 1, exception.getMessage().length()).trim(),
					new TypeReference<List<ErrorLog>>() {
					});
			if (error != null) {
				bean.setMessage(error.get(0).getMessage() + " " + "From ABDM End");
			}
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			bean.setData(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	private ResponseEntity<ResponseBean> restCallApiM1V3(CmToken cmToken, String url, JSONObject jsonObject,
			String apiType) throws JsonMappingException, JsonProcessingException {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		ResponseEntity<String> postForEntity = null;
		ResponseEntity<byte[]> postForEntityBytes = null;
		try {
			HttpEntity<String> entity = null;
			AccessToken convertValue = generateToken();
			HttpHeaders headers = new HttpHeaders();
			if (jsonObject != null) {
				LOGGER.info("Calling ==========>" + url);
				LOGGER.info("With this Payload =========> " + jsonObject.toString());
			}

			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			if (cmToken == null && "ABHA ADDRESS SUGGESTIONS V3".equals(apiType)
					&& jsonObject.getString("txnId") != null) {
				headers.set("Transaction_Id", jsonObject.getString("txnId"));
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				
				entity = new HttpEntity<>(headers);
				postForEntity = template.exchange(url, HttpMethod.GET, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			} else if (cmToken == null && "IsExists AbhaAddress".equals(apiType)
					&& jsonObject.getString("abhaaddress") != null) {
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				entity = new HttpEntity<>(headers);
				String urlWithParams = url + "?abhaAddress=" + jsonObject.getString("abhaaddress");
				postForEntity = template.exchange(urlWithParams, HttpMethod.GET, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			} else if (cmToken != null
					&& ("GET ABHA PROFILE".equals(apiType) || "GET ABHA PROFILE FOR VERIFY FLOW".equals(apiType) )) {
				headers.set("X-token", "Bearer " + cmToken.getToken());
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				entity = new HttpEntity<>(headers);
				postForEntity = template.exchange(url, HttpMethod.GET, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			} else if (cmToken != null
					&& ("GET ABHA CARD FOR VERIFY FLOW V3".equals(apiType) || "GET ABHA CARD V3".equals(apiType))) {
				headers.set("X-Token", "Bearer " + cmToken.getToken());
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				entity = new HttpEntity<>(headers);
				postForEntityBytes = template.exchange(url, HttpMethod.GET, entity, byte[].class);
				bean.setData(postForEntityBytes.getBody());
				bean.setStatus(HttpStatus.OK);
			}else if(cmToken!=null && "USER-VERIFY-V3".equals(apiType)) {
				headers.set("T-Token", "Bearer " + cmToken.getToken());
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			}
			else if(cmToken == null && ("DEMOGRAPHIC V3".equals(apiType) || "ENROL ABHA BY BIOMETRIC V3".equals(apiType))) {
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				headers.set("Benefit-Name", "Govt of UP");
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());

			}
			else {
				headers.set("REQUEST-ID", UUID.randomUUID().toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String currentTimestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);
				headers.set("TIMESTAMP", currentTimestamp);
				entity = new HttpEntity<>(jsonObject.toString(), headers);
				postForEntity = template.postForEntity(url, entity, String.class);
				bean.setData(postForEntity.getBody());
				bean.setStatus(postForEntity.getStatusCode());
			}
			LOGGER.info("restCallApi, method response " + apiType);
		} catch (HttpClientErrorException exception) {
			mapper.setSerializationInclusion(Include.NON_NULL);
			LOGGER.error("restCallApi, method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				bean.setStatus(exception.getStatusCode());
				bean.setData(exception.getResponseBodyAsString());
				bean.setMessage("UN-AUTHORIZED -> TOKEN EXPIRED");
			} else {
				ErrorLog error = mapper.readValue(exception.getResponseBodyAsString(), ErrorLog.class);
				if (error != null) {
					if (error.getDetails() != null && !error.getDetails().isEmpty()) {
						String errorString = error.getDetails().stream().map(s -> s.getMessage())
								.collect(Collectors.joining(","));
						bean.setMessage(errorString);
					} else {
						bean.setMessage(error.getMessage());
					}
				}
				bean.setStatus(exception.getStatusCode());
				bean.setData(exception.getResponseBodyAsString());
			}

			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (HttpServerErrorException exception) {
			bean.setMessage(exception.getResponseBodyAsString());
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			bean.setData(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (Exception exception) {
			exception.printStackTrace();
			LOGGER.error("restCallApi method error: " + exception);
			List<ErrorLog> error = mapper.readValue(
					exception.getMessage()
							.substring(exception.getMessage().indexOf(":") + 1, exception.getMessage().length()).trim(),
					new TypeReference<List<ErrorLog>>() {
					});
			if (error != null) {
				bean.setMessage(error.get(0).getMessage() + " " + "From ABDM End");
			}
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			bean.setData(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	public ResponseEntity<ResponseBean> searchByAbhaAddress(String abhaAddress) {
		LOGGER.info("searchByAbhaAddress = " + abhaAddress);
		ResponseBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("abhaAddress", abhaAddress);
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(null, searchByAbhaAddress, jsonObject, "SEARCH-BY-ABHA-ADDRESS"), "SEARCH-BY-ABHA-ADDRESS",
					"");
		} catch (Exception exception) {
			LOGGER.error("searchByAbhaAddress() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseBean> verifyUserV3(UserVerifyDto userVerifyDto) {
		LOGGER.info("UserVerifyDto = " + userVerifyDto);
		ResponseBean bean = null;
		try {
			CmToken cmToken=new CmToken();
			cmToken.setToken(userVerifyDto.getToken());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ABHANumber", userVerifyDto.getABHANumber());
			jsonObject.put("txnId", userVerifyDto.getTxnId());
			return this.returnSuccessOrFailure(
					this.restCallApiM1V3(cmToken, verifyUserV3, jsonObject, "USER-VERIFY-V3"), "USER-VERIFY-V3",
					"");
		} catch (Exception exception) {
			LOGGER.error("searchByAbhaAddress() method error: " + exception);
			bean = new ResponseBean(HttpStatus.EXPECTATION_FAILED, ConstantUtil.SOMETHING_WENT_WRONG,
					exception.getMessage(), null);
			exception.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class CertSession {
	private String clientId;
	private String clientSecret;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Attribute {
	public String key;
	public String value;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Detail {
	public String message;
	public String code;
	public Attribute attribute;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorLog {
	public String code;
	public String message;
	public List<Detail> details;
}
