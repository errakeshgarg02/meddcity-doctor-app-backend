package com.arg.doctorservice.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.arg.common.constants.CommonConstants;
import com.arg.common.dto.CreateUserRequest;
import com.arg.common.enums.ChannelEnum;
import com.arg.common.enums.UserType;
import com.arg.doctorservice.entity.Doctor;

@Component
public class DoctorToCreateUserRequestConverter implements Converter<Doctor, CreateUserRequest> {

    @Override
    public CreateUserRequest convert(Doctor source) {
        Map<String, String> userData = new HashMap<>();
        userData.put(CommonConstants.USER_ID, String.valueOf(source.getId()));
        userData.put(CommonConstants.USER_TYPE, UserType.DOCTOR.name());

        CreateUserRequest createUserRequest = CreateUserRequest.builder().email(source.getEmail())
                .name(source.getName()).username(source.getUsername()).mfaChannel(ChannelEnum.SMS)
                .mobileNumber(source.getMobileNumber()).role(source.getRole()).userData(userData)
                .mfaEnabled(false).build();

        return createUserRequest;
    }
}
