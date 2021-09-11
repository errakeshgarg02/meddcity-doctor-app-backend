/*
 * arg license
 *
 */

package com.arg.common.property;

import java.io.Serializable;

import lombok.Data;

@Data
public class WhatsAppApiPropertyReader implements Serializable {

    private static final long serialVersionUID = 7827654858356338188L;

    private String baseUrl;
    private String username;
    private String password;
    private String authScheme;
    private String version;
    private String encryptionKey;



}
