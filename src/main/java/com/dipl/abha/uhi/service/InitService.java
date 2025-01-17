package com.dipl.abha.uhi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.uhi.dto.Breakup;
import com.dipl.abha.uhi.dto.InitPayload;
import com.dipl.abha.uhi.dto.Order;
import com.dipl.abha.uhi.dto.Params;
import com.dipl.abha.uhi.dto.Payment;
import com.dipl.abha.uhi.dto.Price;
import com.dipl.abha.uhi.dto.Quote;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InitService {

	@Autowired
	private UHIService service;

	@Autowired
	private UHIService uhiService;

	@Autowired
	private ObjectMapper objectMapper;

	public ResponseEntity<?> onInit(InitPayload onInit) throws JsonMappingException, JsonProcessingException {

		ResponseEntity<?> restData = null;
		ResponseBean bean = new ResponseBean();
		try {
			RequestDto requestDto = new RequestDto();
			requestDto.setContext(onInit.getContext());
			requestDto.setMessage(onInit.getMessage());
			requestDto.getContext().setAction("on_init");
			String request = objectMapper.writeValueAsString(requestDto).replace(" ", "");
			log.info("================request========================" + request);
			log.info("==============rest api calling=========================");
			restData = uhiService.checkRestCallSuccessOrNot(
					service.restCallApi(null, request, onInit.getContext().getConsumer_uri() + "/on_init"));
			log.info("restData============>" + restData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Hspa init method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return restData;
	}

	public String generateOrderId() {
		String randomNumber = null;
		try {
			Random random = new Random();

			String part1 = String.format("%04d", random.nextInt(10000));
			String part2 = String.format("%06d", random.nextInt(1000000));
			String part3 = String.format("%04d", random.nextInt(10000));

			randomNumber = part1 + "-" + part2 + "-" + part3;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return randomNumber;
	}

	public InitPayload bulidOnInit(InitPayload onInit) {
		Quote quote = null;
		Params params = null;
		Order order = null;
		Price price = null;
		try {
			log.info("================BULIDING INIT PAYLOAD========================");
			String orderId = generateOrderId();
			order = new Order();
			order.setId(orderId);
			order.setProvider(onInit.getMessage().getOrder().getProvider());
			order.setItem(onInit.getMessage().getOrder().getItem());
			order.setFulfillment(onInit.getMessage().getOrder().getFulfillment());
			order.setBilling(onInit.getMessage().getOrder().getBilling());
			order.setCustomer(onInit.getMessage().getOrder().getCustomer());
			quote = new Quote();
			quote.setPrice(onInit.getMessage().getOrder().getItem().getPrice());
			List<Breakup> breakups = this.bulidBeakUps(onInit.getMessage().getOrder().getItem().getPrice());

			log.info("==========breakups=======" + breakups);

			double total = 0.0;
			for (Breakup breakup : breakups) {
				log.info("==========breakup=======" + breakup);
				total = total + Double.valueOf(breakup.getPrice().getValue());
				log.info("===========Total========" + total);
			}
			log.info("===========Total========" + total);
			
			Payment payment = new Payment();
			payment.setUri("https://api.bpp.com/pay?amt="+total+"&txn_id=ksh87yriuro34iyr3p4&mode=upi&vpa=doctor@upi");
			payment.setType("on-order");
			payment.setStatus("Not paid");
			params = new Params();
			params.setAmount(String.valueOf(total));
			params.setMode("UPI");
			params.setVpa("sana.bhatt@upi");
			payment.setParams(params);
			order.setPayment(payment);
			quote = new Quote();
			quote.setBreakup(breakups);
			price = new Price();
			price.setCurrency("INR");
			price.setValue(String.valueOf(total));
			order.setQuote(quote);
			log.info("order=============>{}",order);
			onInit.getMessage().setOrder(order);
			log.info("onInit");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return onInit;
		// TODO Auto-generated method stub

	}

	private List<Breakup> bulidBeakUps(Price price) {
		Breakup breakup = null;
		Price consPrice = null;
		Breakup breakupCgst = null;
		Price cgstPrice = null;
		Breakup breakupSgst = null;
		Price sgstPrice = null;
		Breakup breakupRegi = null;
		Price RegiPrice = null;
		List<Breakup> breakups = new ArrayList<>();
		Float doublePrice = Float.parseFloat(price.getValue());
		try {
			breakup = new Breakup();
			breakup.setTitle("consulation");
			consPrice = new Price();
			consPrice.setCurrency("INR");
			consPrice.setValue(price.getValue());
			breakup.setPrice(consPrice);
			breakups.add(breakup);
			log.info("============breakup===========" + breakup);

			breakupCgst = new Breakup();
			breakupCgst.setTitle("CGST @  5%");
			double cgstAmount = doublePrice * (0.5 / 100);
			cgstPrice = new Price();
			cgstPrice.setCurrency("INR");
			cgstPrice.setValue(String.valueOf(cgstAmount));
			breakupCgst.setPrice(cgstPrice);
			breakups.add(breakupCgst);
			log.info("============breakupCgst===========" + breakupCgst);

			breakupSgst = new Breakup();
			breakupSgst.setTitle("SGST @  5%");
			double sgstAmount = doublePrice * (0.5 / 100);
			sgstPrice = new Price();
			sgstPrice.setCurrency("INR");
			sgstPrice.setValue(String.valueOf(sgstAmount));
			breakupSgst.setPrice(cgstPrice);
			breakups.add(breakupSgst);
			log.info("============breakupCgst===========" + breakupCgst);

			breakupRegi = new Breakup();
			breakupRegi.setTitle("Registration");
			RegiPrice = new Price();
			RegiPrice.setCurrency("INR");
			RegiPrice.setValue("0.0");
			breakupRegi.setPrice(RegiPrice);
			breakups.add(breakupRegi);
			log.info("============breakupRegi===========" + breakupRegi);
		} catch (Exception e) {
}
		return breakups;
	}

}
