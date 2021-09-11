/*
 * arg license
 *
 */

package com.arg.common.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.arg.common.constants.CommonConstants;

import lombok.Getter;

public class CustomOAuth2Principal implements OAuth2AuthenticatedPrincipal, Serializable {

    private static final long serialVersionUID = -4493086170518645912L;
    private final Map<String, Object> attributes;
    private final Collection<GrantedAuthority> authorities;

    private String name;

    @Getter
    private String email;

    @Getter
    private String mobileNumber;

    @Getter
    private String mfaChannel;

    @Getter
    private String role;

    @Getter
    private Long userId;

    @SuppressWarnings("unchecked")
	public CustomOAuth2Principal(Map<String, Object> attributes,
            Collection<GrantedAuthority> authorities) {

        Assert.notEmpty(attributes, "attributes cannot be empty");
        this.attributes = Collections.unmodifiableMap(attributes);
        this.authorities =
                authorities == null ? AuthorityUtils.NO_AUTHORITIES : Collections.unmodifiableCollection(authorities);

        if (attributes.get("email") != null) {
            email = attributes.get("email").toString();
        }
        Object objData = attributes.get(CommonConstants.DATA);
        if (!ObjectUtils.isEmpty(objData)) {
            Map<String, String> data = (Map<String, String>) objData;
            userId = Long.valueOf(data.get(CommonConstants.USER_ID));
        }

        if (attributes.get("mobile_number") != null) {
            mobileNumber = attributes.get("mobile_number").toString();
        }

        if (attributes.get("mfa_channel") != null) {
            mfaChannel = attributes.get("mfa_channel").toString();
        }

        if (attributes.get("name") != null) {
            name = attributes.get("name").toString();
        }

        authorities.stream().filter(simpleGrantAuthority -> simpleGrantAuthority.getAuthority().startsWith("role"))
                .map(simpleGrantAuthority -> simpleGrantAuthority.getAuthority()).map(role -> {
                    this.role = role;
                    return role;
                }).findFirst();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return name;
    }
}
