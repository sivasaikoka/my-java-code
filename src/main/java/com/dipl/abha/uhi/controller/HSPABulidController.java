package com.dipl.abha.uhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.uhi.service.HSPABulidService;

@RestController
public class HSPABulidController {

	@Autowired
	private HSPABulidService hSPABulidService;

	@GetMapping("/findHprId")
	public ResponseEntity<?> getHprDetails(@RequestParam(name = "hprId") String hprId,
			HttpServletRequest httpServletRequest) {
		return hSPABulidService.getHprDetails(hprId, httpServletRequest);

	}

}
