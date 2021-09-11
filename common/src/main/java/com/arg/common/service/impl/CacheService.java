/*
 * arg license
 *
 */

package com.arg.common.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arg.common.service.ICacheService;
import com.arg.common.utils.CacheUtil;

@Service
public class CacheService implements ICacheService {

    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public void add(String key, Object value) {
        cacheUtil.getCache().put(key, value);
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(cacheUtil.getCache().getIfPresent(key));
    }

    @Override
    public boolean invalidate(String key) {
        cacheUtil.getCache().invalidate(key);
        return true;
    }

    @Override
    public void invalidateAll() {
        cacheUtil.getCache().invalidateAll();
    }

    @Override
    public boolean isPresent(String key) {
        return this.get(key).isPresent();
    }

}
