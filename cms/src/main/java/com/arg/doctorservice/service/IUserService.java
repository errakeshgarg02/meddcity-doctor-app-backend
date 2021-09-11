package com.arg.doctorservice.service;

import java.util.Optional;

import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.request.SignupRequest;

public interface IUserService {
	
	Optional<Doctor> signupDoctor(SignupRequest request);

}
