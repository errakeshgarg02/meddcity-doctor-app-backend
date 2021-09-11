/*
 * arg license
 *
 */

package com.arg.auth.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 8142644164384664730L;

    private String message;
    private String subject;
    private String[] toAddress;
    private String fromAddress;
    private String replyTo;
    private String[] cc;
    private String[] bcc;
    private String mobileNumber;
    private boolean html;

}
