/*
 * arg license
 *
 */

package com.arg.auth.dto.request;

import java.io.Serializable;

import com.arg.auth.enums.ChannelEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest implements Serializable {

    private static final long serialVersionUID = -814720371816446162L;
    private String username;
    private String newRole;
    private String name;
    private String currentPassword;
    private String newPassword;
    private Boolean credentialsExpired;
    private String status;
    private Boolean mfaEnabled;
    private ChannelEnum mfaChannel;
    private String email;
    private String mobileNumber;
}
