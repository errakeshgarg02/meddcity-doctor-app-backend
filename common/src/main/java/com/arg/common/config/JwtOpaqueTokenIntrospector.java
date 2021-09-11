/*
 * arg license
 *
 */

package com.arg.common.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JwtOpaqueTokenIntrospector
        implements
        OpaqueTokenIntrospector {

    @Autowired
    private OAuth2ResourceServerProperties auth2ResourceServerProperties;

    private OpaqueTokenIntrospector delegate;

    @PostConstruct
    public void init() {
        delegate = new NimbusOpaqueTokenIntrospector(
                auth2ResourceServerProperties.getOpaquetoken()
                        .getIntrospectionUri(),
                auth2ResourceServerProperties.getOpaquetoken().getClientId(),
                auth2ResourceServerProperties.getOpaquetoken()
                        .getClientSecret());
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {

    	OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
    	log.debug("JwtOpaqueTokenIntrospector princlipal {}", principal);
    	return this.prepareCustomOAuth2AuthenticatedPrincipal(principal);
    	
    }

    private CustomOAuth2Principal prepareCustomOAuth2AuthenticatedPrincipal(
            OAuth2AuthenticatedPrincipal principal) {
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = ((ArrayList<String>) principal
                .getAttributes().get("authorities")).stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toSet());
        return new CustomOAuth2Principal(
                principal.getAttributes(), authorities);
    }
}
