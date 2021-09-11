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
    
    private static String grantType = "password";
	
	@Autowired
	private AuthServerFeignClient authServerFeignClient;
	
	@Autowired
	private ConversionService conversionService;
	
	public boolean isUserExist(String input) {
		return authServerFeignClient.isUserExist(input);
	}
	
	public String login() {
		Map<String, String> login = authServerFeignClient.login("admin", "password", grantType);
		return "Bearer " + login.get("access_token");
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
