package com.arg.doctorservice.service.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arg.common.constants.CommonConstants;
import com.arg.common.exception.ArgException;
import com.arg.common.utils.MapperUtil;
import com.arg.doctorservice.dao.IDoctorDao;
import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.integration.AuthServerIntegration;
import com.arg.doctorservice.request.SignupRequest;
import com.arg.doctorservice.response.LoginResponse;
import com.arg.doctorservice.service.IUserService;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements IUserService {

	@Autowired
	private IDoctorDao doctorDao;

	@Autowired
	private AuthServerIntegration authServerIntegration;

	@Transactional
	@Override
	public Optional<Doctor> signupDoctor(SignupRequest request) {

		if (authServerIntegration.isUserExist(request.getMobileNumber())) {
			throw new ArgException(String.format("User alread exits with mobile number %s", request.getMobileNumber()),
					HttpStatus.BAD_REQUEST);
		}
		request.setPassword(CommonConstants.STATIC_PWD);
		return doctorDao.signup(request)
				.flatMap(doctor -> authServerIntegration.register(doctor, request.getPassword()));
	}

	@Override
	public Optional<LoginResponse> loginWithMfa(String username) {
		try {
			Map<String, Object> login = authServerIntegration.login(username, CommonConstants.STATIC_PWD);
			return Optional.ofNullable(MapperUtil.convertObject(login, LoginResponse.class));
		} catch (Exception e) {
			if (e instanceof FeignException) {
				FeignException feignException = (FeignException) e;
				String a = new String(feignException.responseBody().get().array());
				return Optional.ofNullable(MapperUtil.toObject(a, LoginResponse.class));
			}
			log.error("Error while login {}", e);
			throw new ArgException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Optional<Map<String, String>> login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<LoginResponse> refreshToken(String refreshToken) {
		try {
			Map<String, Object> login = authServerIntegration.refreshToken(refreshToken);
			return Optional.ofNullable(MapperUtil.convertObject(login, LoginResponse.class));
		} catch (Exception e) {
			if (e instanceof FeignException) {
				FeignException feignException = (FeignException) e;
				String a = new String(feignException.responseBody().get().array());
				return Optional.ofNullable(MapperUtil.toObject(a, LoginResponse.class));
			}
			log.error("Error while refreshToken {}", e);
			throw new ArgException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Optional<LoginResponse> verifyMfa(String mfa, String otp) {
		try {
			Map<String, Object> login = authServerIntegration.verifyMfa(mfa, otp);
			return Optional.ofNullable(MapperUtil.convertObject(login, LoginResponse.class));
		} catch (Exception e) {
			if (e instanceof FeignException) {
				FeignException feignException = (FeignException) e;
				String a = new String(feignException.responseBody().get().array());
				return Optional.ofNullable(MapperUtil.toObject(a, LoginResponse.class));
			}
			log.error("Error while verify mfa {}", e);
			throw new ArgException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
