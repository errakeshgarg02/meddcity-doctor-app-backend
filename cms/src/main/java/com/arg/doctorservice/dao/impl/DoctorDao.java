package com.arg.doctorservice.dao.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.arg.doctorservice.dao.IDoctorDao;
import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.repository.IDoctorRepository;
import com.arg.doctorservice.request.SignupRequest;

@Service
public class DoctorDao implements IDoctorDao {
	
	@Autowired
	private IDoctorRepository doctorRepository;
	
	@Autowired
	private ConversionService conversionService;

	@Override
	public Optional<Doctor> signup(SignupRequest request) {
		Doctor doctor = conversionService.convert(request, Doctor.class);
		return Optional.of(doctorRepository.save(doctor));
	}
}
