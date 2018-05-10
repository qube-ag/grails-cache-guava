package com.demo

class DemoCacheController {

    DemoCacheService demoCacheService

    def index() {
        render result:demoCacheService.cachedMethod(params.key, params.value)
    }
}
