package com.arg.doctorservice.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SignupRequest implements Serializable {

	private static final long serialVersionUID = -1703882314171346482L;

	private String email;
	
	@NotNull
	private String mobileNumber;
	
	private String name;
	
	private String password;
}
