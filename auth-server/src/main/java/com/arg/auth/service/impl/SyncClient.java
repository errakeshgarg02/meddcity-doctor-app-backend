/*
 * arg license
 *
 */

package com.arg.auth.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arg.auth.exception.ArgException;
import com.arg.auth.service.ISyncClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncClient implements ISyncClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T, R> T postObjectRestTemplate(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            R object, Class<T> type) throws ArgException {
        log.debug("postObjectRestTemplate :: ===hostUrl [{}], ===path [{}], ===headers [{}], ===request object [{}]",
                hostUrl, path, headersMap, object);
        try {
            return postRestTemplate(headersMap, object, type, URI.create(hostUrl + path));
        } catch (Exception error) {
            log.error("Error in postObjectRestTemplate {}", error);
            throw new ArgException(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private <R, T> T postRestTemplate(MultiValueMap<String, String> headersMap, R object, Class<T> type, URI url) {
        HttpEntity<R> httpEntity = new HttpEntity<R>(object, headersMap);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, type);
        log.debug("postObjectRestTemplate Response ::{}", exchange.getBody());
        return exchange.getBody();

    }

    @Override
    public <T> T getObject(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            MultiValueMap<String, String> queryParams, Class<T> type) throws ArgException {
        URI uri = URI.create(hostUrl + path);
        log.debug("getObject method with url {}", uri);
        try {
            T response = restTemplate.getForObject(uri, type);
            log.debug("getObject Response ::{}", response);
            return response;
        } catch (Exception error) {
            log.error("Error in postObjectRestTemplate {}", error);
            throw new ArgException(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
