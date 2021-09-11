/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.Serializable;

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
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 2901155772244696427L;
    private String uuid;
    private String message;
    private String name;
    private Long id;
    private String leadId;
    private String applicationId;
    private String status;
    private String fileName;

    private String loanAccountNumber;
    private String customerId;

}
