/*
 * arg license
 *
 */

package com.arg.auth.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.arg.auth.dto.request.CreateUserRequest;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.utils.MapperUtil;

@Component
public class AuthUserConverter implements Converter<CreateUserRequest, AuthUser> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public AuthUser convert(CreateUserRequest source) {
        AuthUser user = new AuthUser();
        BeanUtils.copyProperties(source, user);
        user.setEnabled(true);

        if (!ObjectUtils.isEmpty(source.getMfaChannel())) {
            user.setMfaChannel(source.getMfaChannel().name());
        }

        user.setPassword(passwordEncoder.encode(source.getPassword()));

        if (!ObjectUtils.isEmpty(source.getUserData())) {
            user.setData(mapperUtil.toJson(source.getUserData()));
        }

        if (!ObjectUtils.isEmpty(source.getMfaCode())) {
            user.setMfaCode(passwordEncoder.encode(source.getMfaCode()));
        }

        if (!ObjectUtils.isEmpty(source.getCredentialsExpired())) {
            user.setCredentialsNonExpired(source.getCredentialsExpired());
        }
        return user;
    }

}
