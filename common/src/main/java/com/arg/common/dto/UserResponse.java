/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.Serializable;

import com.arg.common.enums.ChannelEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class UserResponse implements Serializable {

    private static final long serialVersionUID = -6372899026224059596L;

    private String name;
    private String username;
    private String message;
    private String email;
    private String role;
    private Boolean mfaEnabled;
    private ChannelEnum mfaChannel;
    private Boolean enabled;

}
