/*
 * arg license
 *
 */

package com.arg.auth.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = 4043180358724603499L;

    private List<String> token;

}
