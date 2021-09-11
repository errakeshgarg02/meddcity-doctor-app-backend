/*
 * arg license
 *
 */

package com.arg.auth.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arg.auth.converter.AuthUserConverter;
import com.arg.auth.converter.UserResponseConverter;

@Component
public class MvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthUserConverter userConverter;

    @Autowired
    private UserResponseConverter userResponseConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(userConverter);
        registry.addConverter(userResponseConverter);
    }
}
