/*
 * arg license
 *
 */

package com.arg.auth.dto.request;

import java.io.Serializable;

import com.arg.auth.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class PasswordRequest implements Serializable {


    private static final long serialVersionUID = 2262476127647722461L;

    private String username;

    private String password;

    private UserType userType;

    private String otp;

}
