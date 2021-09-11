/*
 * arg license
 *
 */

package com.arg.auth.configuration.mapping;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
public class AuthValueMapper {

    private String clientId;

    private String clientSecret;

    private String privateKey;

    private String publicKey;

    private boolean checkUserScope;

    private String resourceId;

    private Mfa mfa;

    private SmsApiPropertyReader smsapi;
}
