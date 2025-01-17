package com.dipl.abha.service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dipl.abha.controllers.NDHMM2CallBackController.ValidateOtpPayload;
import com.dipl.abha.dto.ResponseBean;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OtpService {

	private static final Integer EXPIRE_MINS = 5;

	private LoadingCache<String, Integer> otpCache;

	public OtpService() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public Integer generateOTP(String key) {
		Random random = new Random();
		Integer otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		return otp;
	}

	public ResponseBean validateOtp(ValidateOtpPayload validate) throws ExecutionException {
		ResponseBean bean = new ResponseBean();
		Integer cacheOTP = otpCache.get(validate.getNumber());
		if (cacheOTP != null && cacheOTP.equals(validate.getOTP())) {
			clearOTP(validate.getNumber());
			bean.setData(true);
			bean.setMessage("Validated OTP Succesfully");
			bean.setStatus(HttpStatus.OK);
		} else {
			bean.setData(false);
			bean.setMessage("Inavlid OTP");
			bean.setStatus(HttpStatus.BAD_REQUEST);

		}
		return bean;
	}

	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
}
