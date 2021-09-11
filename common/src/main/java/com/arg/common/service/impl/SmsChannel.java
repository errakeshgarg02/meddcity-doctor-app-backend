/*
 * arg license
 *
 */

package com.arg.common.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.arg.common.constants.ServicePathUrlConstants;
import com.arg.common.dto.Message;
import com.arg.common.property.CommonPropertyReader;
import com.arg.common.property.SmsApiPropertyReader;
import com.arg.common.service.Channel;
import com.arg.common.service.ISyncClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsChannel implements Channel {

    @Autowired
    private CommonPropertyReader commonPropertyReader;

    @Autowired
    private ISyncClient syncClient;

    @SuppressWarnings("rawtypes")
	@Override
    public Boolean sendMessage(Message message) {
        try {
            MultiValueMap<String, String> headersMap = new HttpHeaders();
            UriComponents url = this.prepareUrl(message.getMobileNumber(), message.getMessage());
            String path = url.toUriString();
            Map object = syncClient.getObject(commonPropertyReader.getSmsApi().getBaseUrl(),
                    path, headersMap, Map.class);
            log.debug("Sms api response {}", object);
        } catch (Exception e) {
            log.error("Error while sending sms", e.getMessage());
        }
        return true;
    }

    private UriComponents prepareUrl(String mobileNumber, String message) {
        SmsApiPropertyReader smsApiPropertyReader = commonPropertyReader.getSmsApi();
        return UriComponentsBuilder.fromPath(ServicePathUrlConstants.SMS_API_PATH)
                .queryParam("ukey", smsApiPropertyReader.getUkey())
                .queryParam("language", smsApiPropertyReader.getLanguage())
                .queryParam("credittype", smsApiPropertyReader.getCredittype())
                .queryParam("senderid", smsApiPropertyReader.getSenderid())
                .queryParam("templateid", smsApiPropertyReader.getTemplateid())
                .queryParam("filetype", smsApiPropertyReader.getFiletype()).queryParam("message", message)
                .queryParam("msisdn", mobileNumber).build().encode();
    }

    @Override
    public Boolean optIn(String mobileNumber) {
        throw new UnsupportedOperationException();
    }
}
