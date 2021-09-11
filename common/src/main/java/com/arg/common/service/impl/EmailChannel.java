/*
 * arg license
 *
 */

package com.arg.common.service.impl;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.arg.common.dto.Message;
import com.arg.common.service.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class EmailChannel implements Channel {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public Boolean sendMessage(Message message) {
        log.debug("Entered sendMessage method with message receiver email {} and subject {}", message.getToAddress(),
                message.getSubject());

        MimeMessage mail = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(message.getFromAddress());
            helper.setTo(message.getToAddress());
            helper.setSubject(message.getSubject());
            helper.setText(message.getMessage(), message.isHtml());
            if (!ObjectUtils.isEmpty(message.getAttachedFile())) {
                File attachedFile = message.getAttachedFile();
                helper.addAttachment(attachedFile.getName(), new FileSystemResource(attachedFile));
            }

            if (!ObjectUtils.isEmpty(message.getAttachementByteArray())) {
                helper.addAttachment(message.getFileName(), new ByteArrayResource(message.getAttachementByteArray()));
            }

            long start = System.currentTimeMillis();
            emailSender.send(mail);
            log.info("Total time taken to send a email ::{} is :: {} ms", message.getToAddress(),
                    (System.currentTimeMillis() - start));
        } catch (MessagingException e) {
            log.error("Problem with sending email to: {}, error: {}", message.getToAddress(), e);
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean optIn(String mobileNumber) {
        throw new UnsupportedOperationException();
    }
}
