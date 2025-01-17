package com.dipl.abha.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.service.HipLinkingService;
import com.dipl.abha.v3.dto.LinkTokenGeneration;
import com.dipl.abha.v3.dto.LinkingCareContext;

@RestController
@RequestMapping("hip-initiated-linking")
public class HipLinkingController {
	@Autowired
	HipLinkingService hipLinkingService;
	@Value("${GENERATE_LINK_TOKEN_V3}")
	private String genLinkToken;

	@Value("${LINK_CARE_CONTEXT_V3}")
	private String linkCareContext;

	@PostMapping("generatelinktoken")
	private ResponseEntity<?> genLinkToken(@RequestBody LinkTokenGeneration payload, HttpServletRequest request)
			throws Exception {

		return hipLinkingService.restCallApi(payload, genLinkToken, "GENERATE-LINK-TOKEN", request);
	}

	@PostMapping("linkcarecontext")
	private ResponseEntity<?> linkCareContext(@RequestBody LinkingCareContext payload, HttpServletRequest request)
			throws Exception {

		return hipLinkingService.restCallApi(payload, linkCareContext, "LINK-CARE-CONTEXT", request);
	}
}
