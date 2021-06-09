package grails.cache.guava

import groovy.transform.CompileStatic
import org.springframework.boot.context.properties.ConfigurationProperties

@CompileStatic
@ConfigurationProperties(value = 'grails.cache.guava', ignoreInvalidFields = true)
class CacheGuavaPluginConfiguration {

    Map<String, CacheGuavaConfig> caches = [:]
    Integer defaultTtl
    Integer defaultMaxCapacity
    Boolean defaultAllowNullValues

    static class CacheGuavaConfig {
        Integer maxCapacity
        Integer ttl
        Boolean allowNullValues
    }
}
