/*
 * arg license
 *
 */

package com.arg.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.arg.auth.service.impl.EmailChannel;
import com.arg.auth.service.impl.SmsChannel;

@Component
public class MessageSenderfactory {

    @Autowired
    private SmsChannel smsChannel;

    @Autowired
    private EmailChannel emailChannel;

    public Channel getInstance(String key) {
        if (ObjectUtils.isEmpty(key)) {
            key = "";
        }
        switch (key) {
            case "EMAIL":
                return emailChannel;
            case "SMS":
                return smsChannel;
            default:
                return emailChannel;
        }
    }
}
