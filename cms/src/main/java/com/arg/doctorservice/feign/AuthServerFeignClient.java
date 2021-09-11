package com.arg.doctorservice.feign;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.arg.common.dto.CreateUserRequest;
import com.arg.common.dto.UserResponse;

@FeignClient(name = "auth", configuration = FeignClientConfiguration.class)
public interface AuthServerFeignClient {
	
	@PostMapping(value = "/oauth/token", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> login(@RequestPart("username") String username, @RequestPart("password") String password,
			@RequestPart("grant_type") String grantType);

	@GetMapping("/v1/oauth/user/isUserExist")
	Boolean isUserExist(@RequestParam("input") String input);

	@PostMapping(value = "/v1/oauth/user/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	UserResponse registerUser(@RequestHeader Map<String, String> headers, @Valid @RequestBody CreateUserRequest request);
	
}
