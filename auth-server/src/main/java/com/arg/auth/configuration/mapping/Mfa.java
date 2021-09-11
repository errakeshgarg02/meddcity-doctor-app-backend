/*
 * arg license
 *
 */

package com.arg.auth.configuration.mapping;

import java.io.Serializable;

import lombok.Data;

@Data
public class Mfa implements Serializable {

    private static final long serialVersionUID = -9105854189700217619L;
    private int codeLength;
    private int expTimeInSec;
    private String loginOtp;
    private String forgetPwdText;
    private String subject;
}
