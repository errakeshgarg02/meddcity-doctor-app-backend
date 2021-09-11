/*
 * arg license
 *
 */

package com.arg.auth.service.impl;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.arg.auth.dto.request.Message;
import com.arg.auth.service.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class EmailChannel implements Channel {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public boolean sendMessage(Message message) {
        log.debug("Entered sendMessage method with message {}", message);
        try {
            MimeMessage mail = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(message.getFromAddress());
            helper.setTo(message.getToAddress());
            helper.setSubject(message.getSubject());
            helper.setText(message.getMessage(), message.isHtml());
            long start = System.currentTimeMillis();
            emailSender.send(mail);
            log.info("Total time taken to send a email ::{} is :: {} ms", message.getToAddress(),
                    (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("Problem with sending email to: {}, error: {}", message.getToAddress(), e);
        }
        return true;
    }
}
