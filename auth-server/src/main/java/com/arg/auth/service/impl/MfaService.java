/*
 * arg license
 *
 */

package com.arg.auth.service.impl;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arg.auth.configuration.mapping.AuthValueMapper;
import com.arg.auth.dto.request.Message;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.MessageType;
import com.arg.auth.helper.AuthHelper;
import com.arg.auth.repository.AuthUserRepository;
import com.arg.auth.service.IMfaService;
import com.arg.auth.service.MessageSenderfactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MfaService implements IMfaService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSenderfactory messageSenderfactory;

    @Autowired
    private AuthValueMapper authValueMapper;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;


    @Override
    public boolean verifyCode(String username, String verificationCode) {
        return userRepository.findByUsername(username).filter(user -> authHelper.verifyMfa(user, verificationCode))
                .map(user -> Boolean.TRUE).orElse(Boolean.FALSE);
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<Boolean> sendVerificationCode(AuthUser user, MessageType messageType) {
        log.debug("Entered sendVerificationCode method with username {}, mobile {}, message type {}",
                user.getUsername(), user.getMobileNumber(), messageType);
        String mfaCode = authHelper.generateMfaCode();
        Message message = authHelper.prepareMailMessageForOTP(fromEmailAddress, user, messageType,
                mfaCode);
        return userRepository.findByUsername(user.getUsername())
                .map(usr -> {
                    usr.setMfaCode(passwordEncoder.encode(mfaCode));
                    usr.setMfaCodeExpTime(LocalDateTime.now().plusSeconds(authValueMapper.getMfa().getExpTimeInSec()));
                    return usr;
                }).map(userRepository::save)
                .map(usr -> messageSenderfactory.getInstance(usr.getMfaChannel()).sendMessage(message))
                .map(flag -> CompletableFuture.completedFuture(flag))
                .orElseThrow(() -> new RuntimeException("Error while sending verification code"));
    }
}
