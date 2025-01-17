package com.dipl.abha.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.ErrorDTO;
import com.dipl.abha.dto.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserLinkingV3Service {

	@Autowired
	Base64Genarate base64Genarate;

	@Autowired
	private RestTemplate template;
	@Autowired
	private ObjectMapper mapper;

	public ResponseEntity<?> restCallApi(Object payload, String url, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		ResponseEntity<String> postForEntity = null;
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		bean.setMessage("SUCCESS");
		try {
			String payloadString = mapper.writeValueAsString(payload);
			AccessToken access = base64Genarate.generateToken();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + access.getAccessToken());
			headers.set("X-CM-ID", "sbx");
			headers.set("X-HIU-ID", request.getHeader("X-HIU-ID"));
			headers.set("TIMESTAMP", request.getHeader("TIMESTAMP"));
			headers.set("REQUEST-ID", request.getHeader("REQUEST-ID"));
			headers.set("X-AUTH-TOKEN", request.getHeader("X-AUTH-TOKEN"));

			HttpEntity<?> entity = new HttpEntity<>(payloadString, headers);
			postForEntity = template.postForEntity(url, entity, String.class);
			bean.setData(postForEntity.getStatusCodeValue());
			bean.setStatus(postForEntity.getStatusCode());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				bean.setStatus(HttpStatus.BAD_REQUEST);
				bean.setData(e.getResponseBodyAsString());
				bean.setMessage("Invalid Credentials");
			} else {
				ErrorDTO error = mapper.readValue(e.getResponseBodyAsString(), ErrorDTO.class);
				bean.setMessage(error.getError().getMessage());
				bean.setData(error.getError().getCode());
				bean.setStatus(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

}
