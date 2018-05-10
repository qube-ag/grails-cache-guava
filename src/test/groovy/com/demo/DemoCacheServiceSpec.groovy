package com.demo

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class DemoCacheServiceSpec extends Specification implements ServiceUnitTest<DemoCacheService>{

    def setup() {
    }

    def cleanup() {
    }

    void "test cachedMethod"() {
        when:
            def name = 'my key'
            def value = 'my val'
            def ret = service.cachedMethod(name, value)
        then:
            ret == value
    }
}
