package grails.cache.guava

import grails.plugins.*

class GrailsCacheGuavaGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.5 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Cache Guava" // Headline display name of the plugin
    def author = "Moritz Kobel"
    def authorEmail = "moritz@kobelnet.ch"
    def description = '''\
The guava cache provides a simple in memory cache with maximal capacity and TTL.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-cache-guava"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "IT & Design Solutions GmbH", url: "https://www.itds.ch/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GitHub", url: "https://github.com/itds-ch/grails-cache-guava/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/itds-ch/grails-cache-guava" ]

    def loadAfter = ['cache']

    def dependsOn = [cache: "4.0.0 > *"]

    private boolean isCachingEnabled() {
        config.getProperty('grails.cache.enabled', Boolean, true)
    }

    Closure doWithSpring() {
        { ->
            if (!cachingEnabled) {
                log.warn 'Cache plugin is disabled'
                return
            }

            guavaCacheConfiguration(CacheGuavaPluginConfiguration)

            grailsCacheManager(GrailsGuavaCacheManager) {
                configuration = ref('guavaCacheConfiguration')
            }
        }
    }

}
