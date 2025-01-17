package com.dipl.abha.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstantUtil {

	public static final String SUCCESS = "Success...";

	public static final String RECORDS_SUCCESS_MESSAGE = "Records Feteched Successfully";

	public static final String RECORDS_ADDED_MESSAGE = "Records added Successfully";

	public static final String RECORDS_UPDATE_MESSAGE = "Record updated successfully.!";

	public static final String NO_RECORD = "No Records found";

	public static final String SOMETHING_WENT_WRONG = "Unable to process these details now, Please try again later.!";

	public static final String PAYLOAD_WRONG = "Invalid Payload";

	public static final String RECORDS_FETCH_SUCCESS = "Records Feteched Successfully.!";

	public static final String ABHA_SEVER_ISSUE_ERROR_MESSAGE = "Looks like something is not right with ABDM's systems. Please try again after sometime";

	public static final String X_GATEWAY_AUTHORIZATION = "X-Gateway-Authorization";
	
	public static final String GET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY = "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY";
	
	public static final String GET_HPR_DOCTOR_DETAILS_BY_DOCTORNAME_SPEACIALITY = "GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME-SPEACIALITY";
	
	public static final String GET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY_DOCTORNAME_FACILITY = "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-DOCTORNAME-FACILITY";
	
	public static final String GGET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY_FACILITY = "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-FACILITY";
	
	public static final String GET_HPR_DOCTOR_DETAILS_BY_HPRID = "GET-HPR-DOCTOR-DETAILS-BY-HPRID";
	
	public static final String GET_HPR_DOCTOR_DETAILS_BY_DOCTORNAME = "GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME";
	
	public static final String AUTHORIZATION ="Authorization";
	
	public static final List<String> ABHA_NOTIFY_API_END_POINT_v05 = Stream.of("/abha/v0.5/consents/hip/notify",
			"/abha/gateway/v0.5/consents/hip/notify", "/abha/v1.0/consents/hip/notify",
			"/abha/gateway/v1.0/consents/hip/notify", "/abha/api/v3/consent/request/hip/notify")
			.collect(Collectors.toList());

	public static final List<String> ABHA_NOTIFY_API_END_POINT_v1 = Stream.of("/abha/v1.0/consents/hip/notify",
			"/abha/gateway/v1.0/consents/hip/notify", "/abha/api/v3/consent/request/hip/notify")
			.collect(Collectors.toList());

	public static final List<String> ABHA_HIU_NOTIFY_API_END_POINT_v1 = Stream
			.of("/abha/v1.0/consents/hiu/notify", "/abha/gateway/v1.0/consents/hiu/notify",
					"/abha/v0.5/consents/hiu/notify", "/abha/gateway/v0.5/consents/hiu/notify")
			.collect(Collectors.toList());

	public static final List<String> ABHA_REQUEST_API_END_POINT_v05 = Stream
			.of("/abha/v0.5/health-information/hip/request", "/abha/gateway/v0.5/health-information/hip/request",
					"/abha/v1.0/health-information/hip/request", "/abha/gateway/v1.0/health-information/hip/request",
					"/abha/api/v3/hip/health-information/request")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DISCOVER_API_END_POINT_v1 = Stream
			.of("/abha/v1.0/care-contexts/discover", "/abha/gateway/v1.0/care-contexts/discover")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DISCOVER_API_END_POINT_v05 = Stream
			.of("/abha/v0.5/care-contexts/discover", "/abha/gateway/v0.5/care-contexts/discover")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DATA_PUSH_API_END_POINT_v05 = Stream
			.of("/abha/v0.5/health-information/transfer", "/abha/gateway/v0.5/health-information/transfer",
					"/abha/v1.0/health-information/transfer", "/abha/gateway/v1.0/health-information/transfer")
			.collect(Collectors.toList());

	public static final List<String> ABHA_URLS = Stream.of("/abha/v0.5/users/auth/on-fetch-modes",
			"/abha/v1.0/users/auth/on-fetch-modes", "/abha/v0.5/users/auth/on-init", "/abha/v1.0/users/auth/on-init",
			"/abha/v0.5/users/auth/on-confirm", "/abha/v1.0/users/auth/on-confirm",
			"/abha/v0.5/links/link/on-add-contexts", "/abha/v1.0/links/link/on-add-contexts",
			"/abha/v0.5/links/context/on-notify", "/abha/v1.0/links/context/on-notify",
			"/abha/v0.5/patients/sms/on-notify", "/abha/v1.0/patients/sms/on-notify", "/abha/v0.5/patients/on-find",
			"/abha/v1.0/patients/on-find", "/abha/v0.5/consent-requests/on-init", "/abha/v1.0/consent-requests/on-init",
			"/abha/v0.5/consents/on-fetch", "/abha/v1.0/consents/on-fetch",
			"/abha/v0.5/health-information/hiu/on-request", "/abha/v1.0/health-information/hiu/on-request",
			"/abha/api/v3/links/link/on-add-contexts", "/abha/gateway/v0.5/users/auth/on-fetch-modes",
			"/abha/gateway/v1.0/users/auth/on-fetch-modes", "/abha/gateway/v0.5/users/auth/on-init",
			"/abha/gateway/v1.0/users/auth/on-init", "/abha/gateway/v0.5/users/auth/on-confirm",
			"/abha/gateway/v1.0/users/auth/on-confirm", "/abha/gateway/v0.5/links/link/on-add-contexts",
			"/abha/gateway/v1.0/links/link/on-add-contexts", "/abha/gateway/v0.5/links/context/on-notify",
			"/abha/gateway/v1.0/links/context/on-notify", "/abha/gateway/v0.5/patients/sms/on-notify",
			"/abha/gateway/v1.0/patients/sms/on-notify", "/abha/gateway/v0.5/patients/on-find",
			"/abha/gateway/v1.0/patients/on-find", "/abha/gateway/v0.5/consent-requests/on-init",
			"/abha/gateway/v1.0/consent-requests/on-init", "/abha/gateway/v0.5/consents/on-fetch",
			"/abha/gateway/v1.0/consents/on-fetch", "/abha/gateway/v0.5/health-information/hiu/on-request",
			"/abha/gateway/v1.0/health-information/hiu/on-request", "/abha/gateway/api/v3/links/link/on-add-contexts")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DISCOVER_URLS = Stream
			.of("/abha/v0.5/care-contexts/discover", "/abha/v1.0/care-contexts/discover",
					"/abha/gateway/v0.5/care-contexts/discover", "/abha/gateway/v1.0/care-contexts/discover")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DISCOVER_LINK_INIT = Stream.of("/abha/v0.5/links/link/init",
			"/abha/v1.0/links/link/init", "/abha/gateway/v0.5/links/link/init", "/abha/gateway/v1.0/links/link/init")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DISCOVER_LINK_CONFIRM = Stream.of("/abha/gateway/v1.0/links/link/confirm",
			"/abha/gateway/v0.5/links/link/confirm", "/abha/v0.5/links/link/confirm", "/abha/v1.0/links/link/confirm")
			.collect(Collectors.toList());

	public static final List<String> ABHA_DIRECT_AUTH_URL = Stream
			.of("/abha/v0.5/users/auth/notify", "/abha/gateway/v0.5/users/auth/notify", "/abha/v1.0/users/auth/notify",
					"/abha/gateway/v1.0/users/auth/notify")
			.collect(Collectors.toList());

	public static final List<String> ABHA_SHARE_PROFILE = Stream
			.of("/abha/v1.0/patients/profile/share", "/abha/v0.5/patients/profile/share",
					"/abha/gateway/v1.0/patients/profile/share", "/abha/gateway/v0.5/patients/profile/share")
			.collect(Collectors.toList());

	public static final List<String> ABHA_V3_ON_GENERATE_TOKEN = Stream
			.of("/abha/api/v3/hip/token/on-generate-token", "/abha/gateway/api/v3/hip/token/on-generate-token",
					"/abha/api/v3/hiecm/user-initiated-linking/patient/care-context/discover ")
			.collect(Collectors.toList());

	public static final List<String> ABHA_V3_DISCOVER_APIS = Stream.of("/abha/api/v3/hip/patient/care-context/discover")
			.collect(Collectors.toList());

	public static final List<String> PHR_V3_DISCOVER_APIS = Stream
			.of("/abha/api/v3/hiu/patient/care-context/on-discover").collect(Collectors.toList());

	public static final List<String> ABHA_UHI_URLS = Stream.of("/abha/search", "/abha/hspa/select", "/abha/hspa/init", "/abha/hspa/confirm", "/abha/hspa/status", "/abha/hspa/cancel", "/abha/hspa/on_select", "/abha/hspa/on_init", "/abha/hspa/on_confirm", "/abha/hspa/on_status", "/abha/hspa/on_cancel",
			"/abha/eua/on_select", "/abha/eua/on_init", "/abha/eua/on_confirm", "/abha/eua/on_status", "/abha/eua/on_cancel").collect(Collectors.toList());
	
	

	public static String utcTimeStamp() {
		Instant now = Instant.now();
		ZonedDateTime utcTime = now.atZone(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return utcTime.format(formatter);

	}

	public static String indianTimeStamp() {
		Instant now = Instant.now();
		Instant istInstant = now.plus(Duration.ofHours(5).plusMinutes(30));
		ZonedDateTime istTime = istInstant.atZone(ZoneOffset.UTC); // Keeping it at UTC, but adjusted for IST
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return istTime.format(formatter);
	}
	
	public static String indianTimeStampWith5mins() {
		Instant now = Instant.now();
		Instant istInstant = now.plus(Duration.ofHours(5).plusMinutes(35));
		ZonedDateTime istTime = istInstant.atZone(ZoneOffset.UTC); // Keeping it at UTC, but adjusted for IST
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return istTime.format(formatter);
	}

	public static String convertToOffsetDateTimeWithMillis(String dateTimeStr) {
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
		LocalDateTime dateTimeWithMillis = dateTime.withNano(0);
		OffsetDateTime offsetDateTime = dateTimeWithMillis.atOffset(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		return offsetDateTime.format(formatter);
	}

}
