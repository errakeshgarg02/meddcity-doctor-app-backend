package com.arg.doctorservice.dao;

import java.util.Optional;

import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.request.SignupRequest;

public interface IDoctorDao {
	
	Optional<Doctor> signup(SignupRequest request);

}
