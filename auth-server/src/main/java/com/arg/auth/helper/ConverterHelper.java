/*
 * arg license
 *
 */

package com.arg.auth.helper;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.arg.auth.dto.request.UpdateUserRequest;
import com.arg.auth.dto.response.UserResponse;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.UserStatusEnum;

@Component
public class ConverterHelper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConversionService conversionService;

    public AuthUser updateUserData(AuthUser user, UpdateUserRequest source) {

        if (source.getName() != null) {
            user.setName(source.getName());
        }

        if (source.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(source.getNewPassword()));
        }

        if (!ObjectUtils.isEmpty(source.getCredentialsExpired())) {
            user.setCredentialsNonExpired(source.getCredentialsExpired());
        }

        if (!ObjectUtils.isEmpty(source.getStatus())) {
            user.setEnabled(this.isEnabled(UserStatusEnum.valueOf(source.getStatus())));
        }

        if (!ObjectUtils.isEmpty(source.getMfaEnabled())) {
            user.setMfaEnabled(source.getMfaEnabled());
            user.setMfaChannel(source.getMfaChannel().name());
        }
        if (!ObjectUtils.isEmpty(source.getMobileNumber())) {
            user.setMobileNumber(source.getMobileNumber());
        }
        if (!ObjectUtils.isEmpty(source.getEmail())) {
            user.setEmail(source.getEmail());
        }

        return user;
    }

    private boolean isEnabled(UserStatusEnum status) {
        if (Arrays.asList(UserStatusEnum.ACTIVE, UserStatusEnum.PENDING, UserStatusEnum.REJECTED)
                .contains(status)) {
            return true;
        }
        return false;
    }

    public UserResponse convert(AuthUser user) {
        return conversionService.convert(user, UserResponse.class);
    }
}
