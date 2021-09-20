/*
 * arg license
 *
 */

package com.arg.doctorservice.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = -4861022693013063848L;

    private String role;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("mfa_token")
    private String mfaToken;

    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_description")
    private String errorDesc;
    @JsonProperty("user_description")
    private String userDesc;

    @JsonIgnore
    private Map<String, Object> additonalProperties;

    @JsonAnySetter
    public void setProperty(String key, Object value) {
        if (additonalProperties == null) {
            additonalProperties = new HashMap<>();
        }
        additonalProperties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperty() {
        if (additonalProperties == null) {
            additonalProperties = new HashMap<>();
        }
        return additonalProperties;
    }
}
