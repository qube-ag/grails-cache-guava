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

import groovy.transform.CompileStatic
import org.grails.plugin.cache.GrailsCacheManager
import org.springframework.cache.Cache

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Based on com.google.guava.Cache
 *
 * @author Moritz Kobel
 * based on Jakob Drangmeister's GrailsConcurrentLinkedMapCacheManager
 */
@CompileStatic
class GrailsGuavaCacheManager implements GrailsCacheManager {
	final static int DEFAULT_TTL = 3600
	final static int DEFAULT_MAX_CAPACITY = 10_000
	final static boolean DEFAULT_ALLOW_NULL_VALUES = true

	protected final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>()

	CacheGuavaPluginConfiguration configuration

	Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(cacheMap.keySet())
	}

	Cache getCache(String name) {
		return createCache(name)
	}

	boolean cacheExists(String name) {
		getCacheNames().contains(name)
	}

	boolean destroyCache(String name) {
		cacheMap.remove(name) != null
	}

	protected Cache createCache(String name) {
		Cache cache = cacheMap.get(name)
		if (cache == null) {
			CacheGuavaPluginConfiguration.CacheGuavaConfig config = configuration.caches[name]
			int maxCapacity = config?.maxCapacity ?: configuration.defaultMaxCapacity ?: DEFAULT_MAX_CAPACITY
			int ttl = config?.ttl ?: configuration.defaultTtl ?: DEFAULT_TTL
			boolean allowNullValues = config?.allowNullValues != null ? config.allowNullValues : (configuration.defaultAllowNullValues != null ? configuration.defaultAllowNullValues : DEFAULT_ALLOW_NULL_VALUES)
			cache = createGuavaCache(name, maxCapacity, ttl, allowNullValues)
			Cache existing = cacheMap.putIfAbsent(name, cache)
			if (existing != null) {
				cache = existing
			}
		}
		return cache
	}

	protected static GrailsGuavaCache createGuavaCache(String name, int capacity, int ttl, boolean allowNullValues = DEFAULT_ALLOW_NULL_VALUES) {
		return new GrailsGuavaCache(name, capacity, ttl, allowNullValues)
	}
}
