/*
 * arg license
 *
 */

package com.arg.auth.helper;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.arg.auth.configuration.mapping.AuthValueMapper;
import com.arg.auth.dto.request.Message;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.MessageType;
import com.arg.auth.enums.RoleEnum;

@Component
public class AuthHelper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthValueMapper authValueMapper;

    public boolean verifyMfa(AuthUser user, String mfaCode) {

        if (passwordEncoder.matches(mfaCode, user.getMfaCode())
                && LocalDateTime.now().isBefore(user.getMfaCodeExpTime())) {
            return true;
        }
        throw new InvalidRequestException("mfa code is wrong");
    }

    public String generateMfaCode() {
        // String numbers = "0123456789";
        // Random rndm_method = new Random();
        // char[] otp = new char[authValueMapper.getMfa().getCodeLength()];
        // for (int i = 0; i < authValueMapper.getMfa().getCodeLength(); i++) {
        // otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        // }
        // return String.valueOf(otp);
        return "1234";// TODO for testing
    }

    public Message prepareMailMessageForOTP(String fromEmailAddress,
            AuthUser user, MessageType messageType, String otp) {

        Message message = Message.builder().fromAddress(fromEmailAddress)
                .toAddress(new String[] {user.getEmail()})
                .mobileNumber(user.getMobileNumber())
                .build();
        String textMesage = null;
        String name = user.getName();
        if (!ObjectUtils.isEmpty(name)) {
            name = name.split(" ")[0];
        }
        switch (messageType) {
            case MFA:
                textMesage = String.format(authValueMapper.getMfa().getLoginOtp(), name, otp);
                message.setMessage(textMesage);
                message.setSubject(authValueMapper.getMfa().getSubject());
                break;
            case FORGET_PWD:
                textMesage = String.format(authValueMapper.getMfa().getForgetPwdText(), name, otp);
                message.setMessage(textMesage);
                message.setSubject(authValueMapper.getMfa().getSubject());
                break;
            default:
                break;
        }

        return message;
    }

    public boolean isRoleAdmin(String role) {
        return Arrays
                .asList(RoleEnum.ROLE_ADMIN.getValue())
                .contains(role);
    }

    public boolean isEligibleToLoginAsClient(String role) {
        return Arrays
                .asList(RoleEnum.ROLE_ADMIN.getValue())
                .contains(role);
    }
}
