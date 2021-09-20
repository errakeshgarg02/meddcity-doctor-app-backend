package com.arg.doctorservice.service;

import java.util.Map;
import java.util.Optional;

import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.request.SignupRequest;
import com.arg.doctorservice.response.LoginResponse;

public interface IUserService {
	
	Optional<Doctor> signupDoctor(SignupRequest request);
	
	Optional<LoginResponse> loginWithMfa(String username);

	Optional<Map<String, String>> login(String username, String password);

	Optional<LoginResponse> refreshToken(String refreshToken);

	Optional<LoginResponse> verifyMfa(String mfa, String otp);

}
