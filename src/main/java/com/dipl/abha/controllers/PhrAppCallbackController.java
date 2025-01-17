package com.dipl.abha.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.service.ABHAM3Service;
import com.dipl.abha.v3.dto.CareContextOnConfirmv3;
import com.dipl.abha.v3.dto.CareContextOnDiscoverCallBack;
import com.dipl.abha.v3.dto.CareContextOnInitV3;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/v3/hiu/patient/care-context")
public class PhrAppCallbackController {

	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private ABHAM3Service abham3Service;

	private static final Logger log = LoggerFactory.getLogger(PhrAppCallbackController.class);

	@PostMapping("on-discover")
	public void careContextDiscover(@RequestBody String callback) {
		log.info("patient/care-context/on-discover V3 started");
		try {
			CareContextOnDiscoverCallBack ob = objMapper.readValue(callback, CareContextOnDiscoverCallBack.class);
			abham3Service.insertIntoHiuResponse(ob.getResponse().getRequestId(), ob.getTransactionId(), callback, "",
					"", 100);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in patient/care-context/on-discover V3 {}=====>", callback);
		}

	}

	@PostMapping("on-init")
	public void careContextOnInit(@RequestBody String callback) {

		log.info("patient/care-context/on-init V3 started");
		try {
			CareContextOnInitV3 ob = objMapper.readValue(callback, CareContextOnInitV3.class);
			abham3Service.insertIntoHiuResponse(ob.getResponse().getRequestId(), ob.getTransactionId(), callback, "",
					"", 101);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error in patient/care-context/on-init v3" + callback);
		}

	}

	//Reference Number is given in place of transactionId for care-context/on-confirm
	@PostMapping("on-confirm")
	public void careContextOnConfirm(@RequestBody String callback) {
		log.info("patient/care-context/on-confirm V3 started");
		try {
			CareContextOnConfirmv3 ob = objMapper.readValue(callback, CareContextOnConfirmv3.class);
			abham3Service.insertIntoHiuResponse(ob.getResponse().getRequestId(),
					ob.getPatient().get(0).getReferenceNumber(), callback, "", "", 102);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error in patient/care-context/on-confirm v3" + callback);
		}

	}

}
