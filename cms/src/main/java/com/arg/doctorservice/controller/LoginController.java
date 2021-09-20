package com.arg.doctorservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.arg.common.exception.ArgException;
import com.arg.doctorservice.response.LoginResponse;
import com.arg.doctorservice.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/oauth/token")
public class LoginController {

	@Autowired
	private IUserService userService;

	@PostMapping(value = "/loginWithOtp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> loginWithOtp(@RequestPart String username) {
		return userService.loginWithMfa(username).map(ResponseEntity.ok()::body)
				.orElseThrow(() -> new ArgException("Error while login with otp"));
	}

	@PostMapping(value = "/verifyMfa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> verifyMfa(@RequestPart String mfa, @RequestPart String otp) {
		return userService.verifyMfa(mfa, otp).map(ResponseEntity.ok()::body)
				.orElseThrow(() -> new ArgException("Error while verifying mfa token otp"));
	}
	
	@PostMapping(value = "/refreshToken", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> refreshToken(@RequestPart String refreshToken) {
		return userService.refreshToken(refreshToken).map(ResponseEntity.ok()::body)
				.orElseThrow(() -> new ArgException("Error while refreshing token"));
	}
}
