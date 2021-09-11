/*
 * arg license
 *
 */

package com.arg.common.service;

import java.util.Optional;

public interface ICacheService {

    public void add(String key, Object value);

    public Optional<Object> get(String key);

    public boolean invalidate(String key);

    public void invalidateAll();

    public boolean isPresent(String key);

}
