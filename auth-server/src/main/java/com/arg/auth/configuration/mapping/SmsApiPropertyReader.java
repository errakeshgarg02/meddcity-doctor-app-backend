/*
 * arg license
 *
 */

package com.arg.auth.configuration.mapping;

import java.io.Serializable;

import lombok.Data;

@Data
public class SmsApiPropertyReader implements Serializable {

    private static final long serialVersionUID = -449728677444990242L;

    private String baseUrl;
    private String ukey;
    private String language;
    private int credittype;
    private String senderid;
    private Long templateid;
    private int filetype;


}
