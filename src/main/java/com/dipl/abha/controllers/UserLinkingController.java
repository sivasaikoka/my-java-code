package com.dipl.abha.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.service.UserLinkingV3Service;
import com.dipl.abha.v3.dto.CareContextDiscoverV3;

@RestController
@RequestMapping("user-initiated-linking")
public class UserLinkingController {

	@Autowired
	private UserLinkingV3Service userLinkingService;

	@Value("${CARE_CONTEXT_DISCOVER_V3}")
	private String careContextDiscover;

	@PostMapping("carecontextdiscover")
	private ResponseEntity<?> careContextDiscover(@RequestBody CareContextDiscoverV3 payload, HttpServletRequest request)
			throws Exception {

		return userLinkingService.restCallApi(payload, careContextDiscover, request);
	}
}
