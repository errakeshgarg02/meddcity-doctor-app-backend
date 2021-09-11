/*
 * arg license
 *
 */

package com.arg.auth.service;

import java.util.concurrent.CompletableFuture;

import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.MessageType;

public interface IMfaService {

    public boolean verifyCode(String username, String verificationCode);

    public CompletableFuture<Boolean> sendVerificationCode(AuthUser user, MessageType messageType);


}
