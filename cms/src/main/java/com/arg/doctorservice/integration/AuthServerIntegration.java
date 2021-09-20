package com.arg.doctorservice.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import com.arg.common.dto.CreateUserRequest;
import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.feign.AuthServerFeignClient;

@Component
public class AuthServerIntegration {
	
    @Value("${service.administrator.username}")
    private String username;

    @Value("${service.administrator.password}")
    private String password;
    
    private static String GRANT_TYPE_PASSWORD = "password";
    
    private static String GRANT_TYPE_MFA = "mfa";
    
    private static String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	
	@Autowired
	private AuthServerFeignClient authServerFeignClient;
	
	@Autowired
	private ConversionService conversionService;
	
	public boolean isUserExist(String input) {
		return authServerFeignClient.isUserExist(input);
	}
	
	public String login() {
		Map<String, Object> login = authServerFeignClient.login(username, password, GRANT_TYPE_PASSWORD);
		return "Bearer " + login.get("access_token");
	}
	
	public Map<String, Object> login(String username, String password) {
		return authServerFeignClient.login(username, password, GRANT_TYPE_PASSWORD);
	}
	
	public Map<String, Object> verifyMfa(String mfaToken, String mfaCode) {
		return authServerFeignClient.verifyMfa(mfaToken, mfaCode, GRANT_TYPE_MFA);
	}
	
	public Map<String, Object> refreshToken(String regreshToken) {
		return authServerFeignClient.refreshToken(regreshToken, GRANT_TYPE_REFRESH_TOKEN);
	}
	
	public Optional<Doctor> register(Doctor doctor, String password) {
		CreateUserRequest createUserRequest = conversionService.convert(doctor, CreateUserRequest.class);
		createUserRequest.setPassword(password);
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", this.login());
		
		return Optional.of(authServerFeignClient.registerUser(headers, createUserRequest))
				.map(userResponse -> doctor);
	}
	

}
