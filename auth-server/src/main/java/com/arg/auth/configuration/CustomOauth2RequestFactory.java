/*
 * arg license
 *
 */

package com.arg.auth.configuration;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 *
 * @author rakesh garg
 *
 */
public class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {

    public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService,
            UserDetailsService userDetailsService, TokenStore tokenStore) {
        super(clientDetailsService);
    }

    @Override
    public TokenRequest createTokenRequest(
            Map<String, String> requestParameters,
            ClientDetails authenticatedClient) {
        if (requestParameters.get("grant_type").equals("refresh_token")) {
            authenticatedClient.getAuthorities()
                    .addAll(authenticatedClient.getScope().stream()
                            .map(scope -> new SimpleGrantedAuthority(scope))
                            .collect(Collectors.toSet()));
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            authenticatedClient.getClientId(), null,
                            authenticatedClient.getAuthorities()));
        } else {
            authenticatedClient.getAuthorities()
                    .addAll(authenticatedClient.getScope().stream()
                            .map(scope -> new SimpleGrantedAuthority(scope))
                            .collect(Collectors.toSet()));
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            authenticatedClient.getClientId(), null,
                            authenticatedClient.getAuthorities()));
        }
        return super.createTokenRequest(requestParameters, authenticatedClient);
    }
}
