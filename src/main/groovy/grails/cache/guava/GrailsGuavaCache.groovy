/* Copyright 2012-2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.cache.guava

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import grails.plugin.cache.GrailsCache
import grails.plugin.cache.GrailsValueWrapper
import groovy.transform.CompileStatic
import org.springframework.cache.support.SimpleValueWrapper

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * @author Moritz Kobel
 * based on Jakob Drangmeister GrailsConcurrentLinkedMapCache
 */
@CompileStatic
class GrailsGuavaCache implements GrailsCache {

    private static final Object NULL_HOLDER = new NullHolder()
    private String name
    private int capacity
    private int ttl
    private final Cache<Object, Object> store
    private final boolean allowNullValues

    /**
     * Create a new Guava Cache with the specified name
     * and capacity
     * @param name the name of the cache
     * @param capacity of the cache
     * @param ttl of the cache
     * @param allowNullValues null values (default is true)
     */
    GrailsGuavaCache(String name, int capacity, int ttl, boolean allowNullValues = true) {
        this.name = name
        this.capacity = capacity
        this.ttl = ttl
        this.allowNullValues = allowNullValues
        this.store = CacheBuilder.newBuilder()
                .maximumSize(capacity)
                .expireAfterWrite(ttl, TimeUnit.SECONDS)
                .build()
    }

    @Override
    final String getName() {
        return this.name
    }

    @Override
    final Cache<Object, Object> getNativeCache() {
        return this.store
    }

    @Override
    GrailsValueWrapper get(Object key) {
        Object value = getNativeCache().getIfPresent(key)
        return value == null ? null : new GrailsValueWrapper(fromStoreValue(value), null)
    }

    @Override
    <T> T get(Object key, Class<T> type) {
        Object value = getNativeCache().getIfPresent(key)
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value)
        }
        return (T) value
    }

    @Override
    <T> T get(Object key, Callable<T> valueLoader) {
        try {
            return (T)this.store.get(key, valueLoader)
        } catch (ExecutionException ignore) {
            return null
        }
    }


    @Override
    void put(Object key, Object value) {
        this.store.put(key, toStoreValue(value))
    }

    @Override
    ValueWrapper putIfAbsent(Object key, Object value) {
        Object existing = get(key)
        if ( existing == null ) {
            put(key,value)
            existing = get(key)
        }
        return toWrapper(existing)
    }


    @Override
    void evict(Object key) {
        this.store.invalidate(key)
    }

    @Override
    void clear() {
        this.store.invalidateAll()
    }

    /**
     * Convert the given value from the internal store to a user value
     * returned from the get method (adapting {@code null}).
     * @param storeValue the store value
     * @return the value to return to the user
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue == NULL_HOLDER) {
            return null
        }
        return storeValue
    }

    /**
     * Convert the given user value, as passed into the put method,
     * to a value in the internal store (adapting {@code null}).
     * @param userValue the given user value
     * @return the value to store
     */
    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER
        }
        return userValue
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null)
    }

    @Override
    Collection<Object> getAllKeys() {
        return this.store.asMap().keySet()
    }

    int getCapacity() {
        return capacity
    }

    boolean isAllowNullValues() {
        return allowNullValues
    }

    long getSize() {
        return this.store.size()
    }

    private static class NullHolder implements Serializable { }
}
