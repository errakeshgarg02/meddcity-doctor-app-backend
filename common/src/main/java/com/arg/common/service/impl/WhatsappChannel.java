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
import com.arg.common.property.WhatsAppApiPropertyReader;
import com.arg.common.service.Channel;
import com.arg.common.service.ISyncClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhatsappChannel implements Channel {

    @Autowired
    private CommonPropertyReader commonPropertyReader;

    @Autowired
    private ISyncClient syncClient;

    @SuppressWarnings("rawtypes")
	@Override
    public Boolean sendMessage(Message message) {
        try {
            MultiValueMap<String, String> headersMap = new HttpHeaders();
            UriComponents url = this.prepareUrlForSendMediaMessage(message);
            String path = url.toUriString();
            Map object = syncClient.getObject(commonPropertyReader.getWhatsappApi().getBaseUrl(), path, headersMap,
                    Map.class);
            log.debug("Whatsapp api opt response {}", object);
        } catch (Exception e) {
            log.error("Error while sending whatsapp SendMediaMessage ", e);
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Boolean optIn(String mobileNumber) {
        try {
            MultiValueMap<String, String> headersMap = new HttpHeaders();
            UriComponents url = this.prepareUrlForOptIn(mobileNumber);
            String path = url.toUriString();
            Map object = syncClient.getObject(commonPropertyReader.getWhatsappApi().getBaseUrl(), path, headersMap,
                    Map.class);
            log.debug("Whatsapp api opt response {}", object);
        } catch (Exception e) {
            log.error("Error while sending whatsapp OPT_IN message", e.getMessage());
        }
        return true;
    }

    private UriComponents prepareUrlForOptIn(String mobileNumber) {
        WhatsAppApiPropertyReader whatsappApi = commonPropertyReader.getWhatsappApi();
        return UriComponentsBuilder.fromPath(ServicePathUrlConstants.WHATAPP_API_PATH)
                .queryParam("userid", whatsappApi.getUsername())
                .queryParam("method", "OPT_IN")
                .queryParam("format", "json")
                .queryParam("password", whatsappApi.getPassword())
                .queryParam("phone_number", mobileNumber)
                .queryParam("v", whatsappApi.getVersion())
                .queryParam("auth_scheme", whatsappApi.getAuthScheme())
                .queryParam("channel", "WHATSAPP")
                .build().encode();
    }


    private UriComponents prepareUrlForSendMediaMessage(Message message) {
        WhatsAppApiPropertyReader whatsappApi = commonPropertyReader.getWhatsappApi();
        return UriComponentsBuilder.fromPath(ServicePathUrlConstants.WHATAPP_API_PATH)
                .queryParam("userid", whatsappApi.getUsername())
                .queryParam("method", "SendMediaMessage")
                .queryParam("format", "json")
                .queryParam("password", whatsappApi.getPassword())
                .queryParam("send_to", message.getMobileNumber())
                .queryParam("v", whatsappApi.getVersion())
                .queryParam("auth_scheme", whatsappApi.getAuthScheme())
                .queryParam("isHSM", "true")
                .queryParam("msg_type", "DOCUMENT")
                .queryParam("media_url", message.getFileLink())
                .queryParam("caption", message.getMessage())
                .queryParam("filename", message.getFileName())
                .build().encode();
    }
}
