package com.arg.doctorservice.service.impl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arg.common.exception.ArgException;
import com.arg.doctorservice.dao.IDoctorDao;
import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.integration.AuthServerIntegration;
import com.arg.doctorservice.request.SignupRequest;
import com.arg.doctorservice.service.IUserService;

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

		return doctorDao.signup(request)
				.flatMap(doctor -> authServerIntegration.register(doctor, request.getPassword()));
	}
}
