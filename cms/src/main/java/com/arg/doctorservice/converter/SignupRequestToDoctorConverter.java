package com.arg.doctorservice.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.arg.common.enums.RoleEnum;
import com.arg.doctorservice.entity.Doctor;
import com.arg.doctorservice.request.SignupRequest;

@Component
public class SignupRequestToDoctorConverter implements Converter<SignupRequest, Doctor > {

	@Override
	public Doctor convert(SignupRequest source) {
		Doctor doctor = new Doctor();
		BeanUtils.copyProperties(source, doctor);
		doctor.setUsername(source.getMobileNumber());
		doctor.setRole(RoleEnum.ROLE_DOCTOR.getValue());
		return doctor;
	}

}
