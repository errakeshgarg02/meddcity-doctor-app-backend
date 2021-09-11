/*
 * arg license
 *
 */

package com.arg.common.enums;

public enum CommunicationSourceType {

    MOBILE, EMAIL;

    public static CommunicationSourceType get(String value) {
        return CommunicationSourceType.valueOf(value);
    }

}
