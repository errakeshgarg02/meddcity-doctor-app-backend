/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.Serializable;

import com.arg.common.enums.LoanSchemeChannel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanSchemeResponse implements Serializable {

    private static final long serialVersionUID = -8196567433788854120L;

    private Long id;

    private String name;

    private String code;

    private LoanSchemeChannel loanSchemeChannel;

    private int graceDays;

    private double minLoanAmount;

    private double maxLoanAmount;

    private double ltvPercentage;

    private double annualInterestRate30Days;

    private double annualInterestRate60Days;

    private double annualInterestRate90Days;

    private double annualInterestRateMax;

}
