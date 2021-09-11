/*
 * arg license
 *
 */

package com.arg.auth.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.arg.auth.dto.request.Message;
import com.arg.auth.enums.ChannelEnum;
import com.arg.auth.service.MessageSenderfactory;

@Component
public class AuthEmailHelper {

    @Autowired
    private TemplateEngine emailTemplateEngine;

    @Autowired
    private MessageSenderfactory messageSenderfactory;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    public boolean sendMailOnResetPassword(String name,
            String email) {
        final Context ctx = new Context();
        ctx.setVariable("name", name.split(" ")[0]);
        ctx.setVariable("email", email);

        final String htmlContent = this.emailTemplateEngine
                .process("html/reset-password-success-mail-to-customer", ctx);

        Message messge = Message.builder().fromAddress(fromEmailAddress)
                .replyTo(fromEmailAddress).toAddress(new String[] {email})
                .message(htmlContent).html(true).subject("Password Reset Done")
                .build();
        return messageSenderfactory.getInstance(ChannelEnum.EMAIL.name())
                .sendMessage(messge);
    }

}
