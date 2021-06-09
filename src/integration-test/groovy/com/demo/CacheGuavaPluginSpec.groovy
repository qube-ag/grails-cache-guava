package com.demo

import grails.cache.guava.GrailsGuavaCache
import grails.testing.mixin.integration.Integration
import org.grails.plugin.cache.GrailsCacheManager
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll

@Integration
class CacheGuavaPluginSpec extends Specification {

	@Autowired
	GrailsCacheManager grailsCacheManager

	@Unroll('config for #name cache')
	void "config settings for various caches and not-defined cache"() {
		when:
		GrailsGuavaCache cache = grailsCacheManager.getCache(name) as GrailsGuavaCache

		then:
		with(cache) {
			ttl == expectedTtl
			capacity == expectedCapacity
			allowNullValues == expectedAllowNullValues
		}

		where:
		name          | expectedTtl | expectedCapacity | expectedAllowNullValues
		'cache1'      | 10          | 2000             | true
		'cache2'      | 300         | 20               | true
		'cache3'      | 300         | 2000             | false
		'not-defined' | 300         | 2000             | true
	}

}
