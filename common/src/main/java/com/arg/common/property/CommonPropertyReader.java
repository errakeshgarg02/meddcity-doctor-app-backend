/*
 * arg license
 *
 */

package com.arg.common.property;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "arg.common")
public class CommonPropertyReader {

    private int otpLength;

    private int otpExpirationInSec;

    private int passwordLength;

    private String otpSubject;

    private String otpText;

    private Map<Integer, String> textMessageMap;

    private String otpEmailText;

    private SmsApiPropertyReader smsApi;

    private WhatsAppApiPropertyReader whatsappApi;
}
