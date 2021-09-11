/*
 * arg license
 *
 */

package com.arg.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class MfaRequiredException extends OAuth2Exception {

    private static final long serialVersionUID = -5912811120342493157L;

    public MfaRequiredException(String mfaToken) {
        super("Multi-factor authentication required");
        this.addAdditionalInformation("mfa_token", mfaToken);
    }

    public String getOAuth2ErrorCode() {
        return "mfa_required";
    }

    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }
}
