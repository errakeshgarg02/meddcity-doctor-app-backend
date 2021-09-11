package com.arg.doctorservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.common.config.CustomOAuth2Principal;
import com.arg.doctorservice.feign.AuthServerFeignClient;

@RestController
public class TestController {
	
	@Autowired
	private AuthServerFeignClient authServerFeignClient;

	@GetMapping("/test")
	public String test(@AuthenticationPrincipal CustomOAuth2Principal principal) {
		System.out.println(principal.getRole()+ " " + principal.getName());
		String uuid = UUID.randomUUID().toString();
		
		Boolean userExist = authServerFeignClient.isUserExist("9876414556");
		System.out.println("User exists "+ userExist);
		
		System.out.println(uuid);
		return uuid;
	}
	
	@PostMapping("/post/{name}")
	public String post(@AuthenticationPrincipal CustomOAuth2Principal principal, @PathVariable String name) {
		System.out.println(principal.getRole()+ " " + principal.getName() +"==="+ name);
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		return uuid;
	}
	
	@GetMapping("/public/test")
	public String testq() {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		Boolean userExist = authServerFeignClient.isUserExist("9876414556");
		System.out.println("User exists "+ userExist);
		return uuid;
	}
}
