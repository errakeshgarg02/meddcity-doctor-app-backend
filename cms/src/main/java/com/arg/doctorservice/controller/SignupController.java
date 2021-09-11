package com.arg.doctorservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.common.aop.Log;
import com.arg.common.dto.ApiResponse;
import com.arg.common.exception.ArgException;
import com.arg.doctorservice.request.SignupRequest;
import com.arg.doctorservice.service.IUserService;

@RestController
@RequestMapping("/public/v1")
public class SignupController {

	@Autowired
	private IUserService userService;

	@Log
	@PostMapping("/doctor/signup")
	public ResponseEntity<ApiResponse> signupDoctor(@Valid @RequestBody SignupRequest request) {
		return userService.signupDoctor(request)
				.map(doctor -> ApiResponse.builder().id(doctor.getId()).message("Doctor signup created successfully!")
						.build())
				.map(ResponseEntity.ok()::body).orElseThrow(() -> new ArgException("Error while signup doctor"));
	}

}
