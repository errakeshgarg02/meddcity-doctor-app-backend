/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.File;
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
    private boolean html;
    private String mobileNumber;
    private File attachedFile;
    private byte[] attachementByteArray;
    private String fileName;
    private String fileLink;

}
