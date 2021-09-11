/*
 * arg license
 *
 */

package com.arg.auth.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.arg.auth.dto.response.UserResponse;
import com.arg.auth.entity.AuthUser;

@Component
public class UserResponseConverter implements Converter<AuthUser, UserResponse> {

    @Override
    public UserResponse convert(AuthUser source) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(source, response);
        source.getAuthorities().stream()
                .filter(simpleGrantAuthority -> simpleGrantAuthority.getAuthority().startsWith("role"))
                .map(simpleGrantAuthority -> simpleGrantAuthority.getAuthority()).map(role -> {
                    response.setRole(role);
                    return role;
                }).findFirst();

        return response;
    }

}
