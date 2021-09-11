/*
 * arg license
 *
 */

package com.arg.common.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arg.common.dto.Message;
import com.arg.common.enums.ChannelEnum;
import com.arg.common.service.MessageSenderfactory;

@Component
public class CommonMessageHelper {

    @Autowired
    private MessageSenderfactory messageSenderfactory;

    public boolean sendSmsMessage(String mobileNumber, String text) {
        Message message = this.prepareSmsMessage(mobileNumber, text);
        return this.sendSmsMessage(message);
    }

    private boolean sendSmsMessage(Message message) {
        return messageSenderfactory.getInstance(ChannelEnum.SMS.name()).sendMessage(message);
    }

    private Message prepareSmsMessage(String mobileNumber, String message) {
        return Message.builder().mobileNumber(mobileNumber).message(message).build();
    }

    public boolean sendEmailMessage(Message message) {
        return messageSenderfactory.getInstance(ChannelEnum.EMAIL.name()).sendMessage(message);
    }

    public boolean sendWhatsAppMessage(Message message) {
        return messageSenderfactory.getInstance(ChannelEnum.WHATSAPP.name()).sendMessage(message);
    }

}
