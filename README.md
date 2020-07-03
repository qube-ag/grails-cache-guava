# grails-cache-guava

This Grails plugin extends the grails-cache plugin.

The guava cache provides a simple in memory cache with maximal capacity and TTL.


## Usage

### Dependency

```groovy
dependencies {
    compile 'org.grails.plugins:cache:4.0.0'
    compile 'org.grails.plugins:cache-guava:1.0.0'
}
```

### Configuration

```yaml
grails:
  cache:
    guava:            
      defaultTtl: 3600
      caches:
        message:
          maxCapacity: 5000
          ttl: 60
        maps:
          maxCapacity: 6000
          ttl: 30
        countries:
          maxCapacity: 1000
```

The GrailsGuavaCacheManager is automatically configured by the plugin.

### Annoations and Services

Just use grails-cache's annotations and services as described in 
its [documentation](http://grails-plugins.github.io/grails-cache/snapshot/guide/index.html)
