/*
 * arg license
 *
 */

package com.arg.auth.configuration.granter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.arg.auth.enums.RoleEnum;

public class LogOffViewAsClientTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "log_off_view_as_client";
    private static final String ACCESS_TOKEN = "access_token";

    public static final GrantedAuthority ROLE_ADMIN_AS_CUSTOMER = new SimpleGrantedAuthority(
            RoleEnum.ROLE_ADMIN.getValue());

    private final TokenStore tokenStore;
    private final ClientDetailsService clientDetailsService;
    private final AuthenticationManager authenticationManager;

    public LogOffViewAsClientTokenGranter(
            AuthorizationServerEndpointsConfigurer endpointsConfigurer,
            AuthenticationManager authenticationManager) {
        super(endpointsConfigurer.getTokenServices(),
                endpointsConfigurer.getClientDetailsService(),
                endpointsConfigurer.getOAuth2RequestFactory(), GRANT_TYPE);
        this.tokenStore = endpointsConfigurer.getTokenStore();
        this.clientDetailsService = endpointsConfigurer
                .getClientDetailsService();
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
            TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(
                tokenRequest.getRequestParameters());
        final String accessToken = parameters.get(ACCESS_TOKEN);
        if (accessToken != null) {
            OAuth2Authentication authentication = this
                    .loadAuthentication(accessToken);
            return getAuthentication(tokenRequest, authentication);
        } else {
            throw new InvalidRequestException("Missing access token");
        }
    }

    private OAuth2Authentication loadAuthentication(String accessTokenValue) {
        OAuth2AccessToken accessToken = this.tokenStore
                .readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException(
                    "Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            this.tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException(
                    "Access token expired: " + accessTokenValue);
        } else {
            OAuth2Authentication result = this.tokenStore
                    .readAuthentication(accessToken);
            if (result == null) {
                throw new InvalidTokenException(
                        "Invalid access token: " + accessTokenValue);
            }
            return result;
        }
    }

    private OAuth2Authentication getAuthentication(TokenRequest tokenRequest,
            OAuth2Authentication authentication) {

        Authentication authentication2 = authenticationManager
                .authenticate(authentication.getUserAuthentication());

        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId != null && clientId.equals(tokenRequest.getClientId())) {
            if (this.clientDetailsService != null) {
                try {
                    ClientDetails clientDetails = this.clientDetailsService
                            .loadClientByClientId(clientId);

                    OAuth2Request storedOAuth2Request = this.getRequestFactory()
                            .createOAuth2Request(clientDetails, tokenRequest);

                    OAuth2AccessToken existingAccessToken = tokenStore
                            .getAccessToken(authentication);
                    tokenStore.removeAccessToken(existingAccessToken);
                    tokenStore.removeRefreshToken(
                            existingAccessToken.getRefreshToken());

                    authentication2 = new PreAuthenticatedAuthenticationToken(
                            authentication2.getPrincipal(), "",
                            authentication2.getAuthorities());

                    authentication = new OAuth2Authentication(
                            storedOAuth2Request, authentication2);

                    authentication.setDetails(null);

                } catch (ClientRegistrationException e) {
                    throw new InvalidTokenException(
                            "Client not valid: " + clientId, e);
                }
            }
            return refreshAuthentication(authentication, tokenRequest);
        } else {
            throw new InvalidGrantException(
                    "Client is missing or does not correspond to the MFA token");
        }
    }

    private OAuth2Authentication refreshAuthentication(
            OAuth2Authentication authentication, TokenRequest request) {
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request()
                .refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException(
                        "Unable to narrow the scope of the client authentication to "
                                + scope + ".",
                        originalScope);
            }

            clientAuth = clientAuth.narrowScope(scope);
        }
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(
                clientAuth, authentication.getUserAuthentication());
        oAuth2Authentication.setDetails(authentication.getDetails());
        return oAuth2Authentication;
    }
}
