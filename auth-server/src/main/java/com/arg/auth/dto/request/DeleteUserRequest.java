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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest implements Serializable {

    private static final long serialVersionUID = -218544109218652078L;

    private String mobileNumber;

    private String username;

}
