/*
 * arg license
 *
 */

package com.arg.common.utils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class CacheUtil {

    private Cache<String, Object> cache;

    @Value("${service.cache.max_time_to_live_sec:900}")
    private Long cacheMaxTimeToLive;

    private Cache<String, Object> prepareCacheObj() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheMaxTimeToLive, TimeUnit.SECONDS)
                .build();
        return cache;
    }

    public Cache<String, Object> getCache() {
        return Optional.ofNullable(cache)
                .orElseGet(() -> this.prepareCacheObj());
    }
}
