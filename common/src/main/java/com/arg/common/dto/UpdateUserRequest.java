/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.Serializable;

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
    private String status;
    private String email;
    private String mobileNumber;
}
