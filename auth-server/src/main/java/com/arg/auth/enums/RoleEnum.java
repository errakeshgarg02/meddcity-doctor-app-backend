/*
 * arg license
 *
 */

package com.arg.auth.enums;

import lombok.Getter;

public enum RoleEnum {

    ROLE_ADMIN("role_admin", 1), ROLE_ADMINISTRATOR("role_administrator", 2), ROLE_STATE_REP("role_state_rep",
            5), ROLE_DISTT_REP("role_distt_rep", 6), ROLE_MEDICAL_REP("role_medical_rep", 7), ROLE_PHARMACIST(
                    "role_pharmacist", 8), ROLE_LAB_ATTENDENT("role_lab_attendent", 8), ROLE_HEALTH_WORKER(
                            "role_health_worker", 9), ROLE_DOCTOR("role_doctor", 9), ROLE_PATIENT("role_patient", 9);

    @Getter
    private String value;

    @Getter
    private Integer order;

    RoleEnum(String value, Integer order) {
        this.value = value;
        this.order = order;
    }

    public static RoleEnum getRole(String value) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getValue().equals(value)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("Please provide a valid role");
    }

    public static boolean validateRole(String role) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getValue().equals(role)) {
                return true;
            }
        }
        throw new IllegalArgumentException("Please provide a valid role");
    }
}
