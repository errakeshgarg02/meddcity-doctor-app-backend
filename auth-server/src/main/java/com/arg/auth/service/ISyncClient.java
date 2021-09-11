/*
 * arg license
 *
 */

package com.arg.auth.service;

import org.springframework.util.MultiValueMap;

import com.arg.auth.exception.ArgException;

public interface ISyncClient {

    public <T, R> T postObjectRestTemplate(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            R object, Class<T> type) throws ArgException;

    public <T> T getObject(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            MultiValueMap<String, String> queryParams, Class<T> type) throws ArgException;

}
