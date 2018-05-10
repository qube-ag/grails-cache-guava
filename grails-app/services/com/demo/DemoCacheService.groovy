package com.demo

import grails.plugin.cache.Cacheable

class DemoCacheService {

    @Cacheable(value = 'demo', key = { name })
    def cachedMethod(String name, String value) {
        return value

    }
}
