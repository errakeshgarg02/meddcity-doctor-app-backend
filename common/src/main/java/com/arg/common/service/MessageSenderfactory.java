/*
 * arg license
 *
 */

package com.arg.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.arg.common.service.impl.EmailChannel;
import com.arg.common.service.impl.SmsChannel;
import com.arg.common.service.impl.WhatsappChannel;

@Component
public class MessageSenderfactory {

    @Autowired
    private SmsChannel smsChannel;

    @Autowired
    private EmailChannel emailChannel;

    @Autowired
    private WhatsappChannel whatsappChannel;

    public Channel getInstance(String key) {
        if (ObjectUtils.isEmpty(key)) {
            key = "";
        }
        switch (key) {
            case "EMAIL":
                return emailChannel;
            case "SMS":
                return smsChannel;
            case "WHATSAPP":
                return whatsappChannel;
            default:
                return emailChannel;
        }
    }
}
