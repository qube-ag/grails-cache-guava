package grails.cache.guava

import groovy.transform.CompileStatic
import org.springframework.boot.context.properties.ConfigurationProperties

@CompileStatic
@ConfigurationProperties(value = 'grails.cache.guava', ignoreInvalidFields = true)
class CacheGuavaPluginConfiguration {

    Boolean clearAtStartup = false
    Map<String, CacheGruavaConfig> caches = [:]
    Integer defaultTtl

    static class CacheGruavaConfig {
        Integer maxCapacity
        Integer ttl
    }
}