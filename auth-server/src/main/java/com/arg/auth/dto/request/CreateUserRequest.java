/*
 * arg license
 *
 */

package com.arg.auth.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.arg.auth.enums.ChannelEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Serializable {

    private static final long serialVersionUID = -4799198288594871808L;

    // @Pattern(regexp = "^(.+)@(.+)$")
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String name;

    @NotBlank
    private String mobileNumber;
    @NotBlank
    private String role;
    private Map<String, String> userData;
    private ChannelEnum mfaChannel;
    private boolean mfaEnabled;
    private String mfaCode;
    private LocalDateTime mfaCodeExpTime;
    private Boolean credentialsExpired;
}
