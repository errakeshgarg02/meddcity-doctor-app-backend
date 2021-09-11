/*
 * arg license
 *
 */

package com.arg.auth.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 8296554722275110134L;
    private boolean exist;
    private String message;
}
