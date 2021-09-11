/*
 * arg license
 *
 */

package com.arg.auth.configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.ObjectUtils;

import com.arg.auth.entity.AuthUser;
import com.arg.auth.utils.MapperUtil;

/**
 * Add custom user principal information to the JWT token
 *
 * @author rakesh
 *
 */
public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    private MapperUtil mapperUtil;

    public CustomTokenEnhancer(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();

        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
        prepareAdditionalInfo(user, info);
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);

        return super.enhance(customAccessToken, authentication);
    }

    private void prepareAdditionalInfo(AuthUser user, Map<String, Object> info) {
        info.put("email", user.getEmail());

        if (!ObjectUtils.isEmpty(user.getMfaChannel())) {
            info.put("mfa_channel", user.getMfaChannel());
        }

        if (!ObjectUtils.isEmpty(user.getName())) {
            info.put("name", user.getName());
        }

        if (!ObjectUtils.isEmpty(user.getMobileNumber())) {
            info.put("mobile_number", user.getMobileNumber());
        }


        if (!ObjectUtils.isEmpty(user.getData())) {
            info.put("data", mapperUtil.toObject(user.getData(), HashMap.class));
        }

        user.getAuthorities().stream()
                .filter(simpleGrantAuthority -> simpleGrantAuthority.getAuthority().startsWith("role"))
                .map(simpleGrantAuthority -> simpleGrantAuthority.getAuthority()).map(role -> {
                    info.put("role", role);
                    return role;
                }).findFirst();
    }
}
