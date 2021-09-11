/*
 * arg license
 *
 */

package com.arg.auth.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.arg.auth.configuration.mapping.AuthValueMapper;
import com.arg.auth.configuration.mapping.SmsApiPropertyReader;
import com.arg.auth.constants.UrlConstants;
import com.arg.auth.dto.request.Message;
import com.arg.auth.service.Channel;
import com.arg.auth.service.ISyncClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsChannel implements Channel {

    @Autowired
    private AuthValueMapper mapper;

    @Autowired
    private ISyncClient syncClient;

    @Override
    public boolean sendMessage(Message message) {
        try {
            log.debug("Sending message to mobile number {}", message.getMobileNumber());
            MultiValueMap<String, String> headersMap = new HttpHeaders();
            UriComponents url = this.prepareUrl(message.getMobileNumber(), message.getMessage());
            String path = url.toUriString();
            Map object = syncClient.getObject(mapper.getSmsapi().getBaseUrl(),
                    path, headersMap, headersMap,
                    Map.class);
            log.debug("Sms api response {}", object);
        } catch (Exception e) {
            log.error("Error while sending sms", e.getMessage());
        }
        return true;
    }

    private UriComponents prepareUrl(String mobileNumber, String message) {
        SmsApiPropertyReader smsapi = mapper.getSmsapi();
        return UriComponentsBuilder.fromPath(UrlConstants.SMS_API_PATH).queryParam("ukey", smsapi.getUkey())
                .queryParam("language", smsapi.getLanguage()).queryParam("credittype", smsapi.getCredittype())
                .queryParam("senderid", smsapi.getSenderid()).queryParam("templateid", smsapi.getTemplateid())
                .queryParam("filetype", smsapi.getFiletype()).queryParam("message", message)
                .queryParam("msisdn", mobileNumber).build().encode();
    }
}
