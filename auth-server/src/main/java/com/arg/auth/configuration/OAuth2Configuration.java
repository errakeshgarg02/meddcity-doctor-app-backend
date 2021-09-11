/*
 * arg license
 *
 */

package com.arg.auth.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.arg.auth.configuration.granter.AdminAsCustomerTokenGranter;
import com.arg.auth.configuration.granter.LogOffViewAsClientTokenGranter;
import com.arg.auth.configuration.granter.MfaTokenGranter;
import com.arg.auth.configuration.granter.PasswordTokenGranter;
import com.arg.auth.configuration.granter.ViewAsClientTokenGranter;
import com.arg.auth.configuration.mapping.AuthValueMapper;
import com.arg.auth.helper.AuthHelper;
import com.arg.auth.service.IMfaService;
import com.arg.auth.utils.MapperUtil;

@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthValueMapper authValueMapper;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private IMfaService mfaService;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private MapperUtil mapperUtil;

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(
                clientDetailsService, userDetailsService, tokenStore());
        requestFactory.setCheckUserScopes(true);
        return requestFactory;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    @Bean
    public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
        return new TokenEndpointAuthenticationFilter(authenticationManager,
                requestFactory());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        MyDefaultTokenServices tokenServices = new MyDefaultTokenServices();
        tokenServices.setAuthenticationManager(authenticationManager);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        tokenServices.setTokenStore(tokenStore());

        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.tokenStore(tokenStore())
                .tokenServices(tokenServices())
                .tokenGranter(tokenGranter(endpoints))
                .tokenEnhancer(jwtAccessTokenConverter())
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService)
                .setClientDetailsService(clientDetailsService);
        if (authValueMapper.isCheckUserScope())
            endpoints.requestFactory(requestFactory());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new CustomTokenEnhancer(mapperUtil);
        converter.setSigningKey(authValueMapper.getPrivateKey());
        converter.setVerifierKey(authValueMapper.getPublicKey());
        return converter;
    }

    private TokenGranter tokenGranter(
            final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>(
                Arrays.asList(endpoints.getTokenGranter()));
        granters.add(new PasswordTokenGranter(endpoints, authenticationManager,
                mfaService));
        granters.add(new MfaTokenGranter(endpoints, authenticationManager,
                mfaService));

        granters.add(new AdminAsCustomerTokenGranter(endpoints,
                authenticationManager, userDetailsService, authHelper));

        granters.add(new ViewAsClientTokenGranter(endpoints,
                authenticationManager, userDetailsService, authHelper));

        granters.add(new LogOffViewAsClientTokenGranter(endpoints, authenticationManager));
        return new CompositeTokenGranter(granters);
    }
}
