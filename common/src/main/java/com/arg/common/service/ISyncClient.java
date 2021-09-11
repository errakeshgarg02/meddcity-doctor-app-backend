/*
 * arg license
 *
 */

package com.arg.common.service;

import org.springframework.util.MultiValueMap;

import com.arg.common.exception.SyncClientException;

public interface ISyncClient {

    public <T> T getObject(String hostUrl, String path, MultiValueMap<String, String> headersMap,
            Class<T> type) throws SyncClientException;
}
