/*
 * arg license
 *
 */

package com.arg.auth.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

@Component
public class CustomerTokenService {

    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Autowired
    private TokenStore tokenStore;

    public boolean invalidateToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);

        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            String accessToken = token.substring("Bearer".length() + 1);
            OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(accessToken);
            if (existingAccessToken != null) {
                tokenStore.removeAccessToken(existingAccessToken);
                tokenStore.removeRefreshToken(existingAccessToken.getRefreshToken());
            }
        }

        return true;
    }
}
