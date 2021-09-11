/*
 * arg license
 *
 */

package com.arg.common.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arg.common.exception.SyncClientException;
import com.arg.common.service.ISyncClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncClient implements ISyncClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T> T getObject(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            Class<T> type) throws SyncClientException {
        URI uri = URI.create(hostUrl + path);
        log.debug("getObject method with url {}", uri);
        try {
            T response = restTemplate.getForObject(uri, type);
            log.debug("getObject Response ::{}", response);
            return response;
        } catch (Exception error) {
            log.error("Error in postObjectRestTemplate {}", error);
            throw new SyncClientException(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
