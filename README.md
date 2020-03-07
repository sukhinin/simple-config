# Yet another simple Kotlin configuration library

[![Build Status](https://travis-ci.com/sukhinin/simple-config.svg?branch=master)](https://travis-ci.com/sukhinin/simple-config)
[![codebeat badge](https://codebeat.co/badges/359401ff-434b-4ea1-be22-c49fd0662657)](https://codebeat.co/projects/github-com-sukhinin-simple-config-master)
[ ![Download](https://api.bintray.com/packages/sukhinin/maven/simple-config/images/download.svg) ](https://bintray.com/sukhinin/maven/simple-config/_latestVersion)

Minimalistic Kotlin configuration library designed with simplicity in mind.

If you're interested in some advanced stuff like POJO binding 
or auto reloading you should definitely take a look at other libraries 
such as [Lihgtbend Config](https://github.com/lightbend/config) or [cfg4j](https://github.com/cfg4j/cfg4j).

## Features
- Implemented in Kotlin with no dependencies except standard library
- Designed with simplicity in mind, zero magic included
- Loads configuration from system properties, classpath resources or files
- Supports recursive variable substitution in config values 
- Thread-safe

## Requirements
Requires at least Java 1.8 and Kotlin 1.3.61, should also work with more recent versions.

## Setting up dependency
Releases are published to Bintray jcenter repository. You can also find and download all released 
artifacts from the [library's page](https://bintray.com/sukhinin/maven/simple-config) on Bintray itself.

### Gradle
#### Add jcenter repository
```groovy
repositories {  
    jcenter()  
}
```
#### Import dependency
```groovy
dependencies {
    implementation 'com.github.sukhinin:simple-config:1.3.0' 
}
```

### Maven
#### Add jcenter repository
```xml
<repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com</url>
</repository>
```
#### Import dependency
```xml
<dependency>
    <groupId>com.github.sukhinin</groupId>
    <artifactId>simple-config</artifactId>
    <version>1.3.0</version>
</dependency>
```

## Using the library

### Quickstart
This snippet shows basic usage of `simple-config`.
```kotlin
import com.github.sukhinin.simpleconfig.ConfigLoader
import com.github.sukhinin.simpleconfig.resolved
import com.github.sukhinin.simpleconfig.withFallback

fun main() {
    val systemPropertiesConfig = ConfigLoader.getConfigFromSystemProperties("app")
    val applicationConfig = ConfigLoader.getConfigFromPath("application.properties")
    val referenceConfig = ConfigLoader.getConfigFromSystemResource("reference.properties")

    val config = systemPropertiesConfig
        .withFallback(applicationConfig)
        .withFallback(referenceConfig)
        .resolved()

    println(config.get("my.config.property"))
}
```

It loads configuration from resource `reference.properties` on the system classpath, file `application.properties` 
in the current working directory of the application, and system properties prefixed with `app.` (prefix will be 
stripped from configuration key name).

Config instance is assembled in such a way that `reference.properties` contains default values that can be 
overridden by `application.properties` and by system properties. Finally variable references in config values
are `resolved()`.

### Working with configuration objects
`Config` interface exposes `get()` method that either returns string value for the given key or throws an exception
when no such key exists. `getOrDefault()` can be used to return the provided default value instead of throwing. 
There are also a number of typed convenience methods `getXxx()` with their respective `getXxxOrDefault()` counterparts.

One can use `keys` property to retrieve a set of all keys and `contains()` method to test if the given key is present
in configuration. 

`size` property is equal to the number of entries in config, and `isEmpty` is true when config does not contain
any entries at all.

### Using scoped configurations
`ScopedConfig` is a wrapper class that allows a configuration to be scoped to only a subset of keys starting with
a given prefix. Prefix always ends with an implicit dot and is removed from the scoped keys. The wrapper itself 
does not hold any config state so any changes to the underlying config have an immediate effect on the scoped values.
`scoped()` extension method provides a fluent syntax for wrapping given config with the `ScopedConfig`.

For example running the following snippet
```kotlin
val config = MapConfig(mapOf("modA.key1" to "v1", "modA.key2" to "v2", "modB.key3" to "v3"))
val scoped = config.scoped("modA")
println(scoped.dump())
```
will print config values scoped to `modA`:
```
key1=v1
key2=v2
```

The `labels` property returns a set of key substrings before the first dot, i.e. with keys `modA.key1`, `modA.key2`, 
`modB.key3` reading `labels` returns a set of `modA` and `modB`. One can use helper method `scopedByLabel()` to
build a map of labels to configs scoped by each label. It is intended to support named configurations:
```kotlin
val config = MapConfig(mapOf("modA.key1" to "v1", "modA.key2" to "v2", "modB.key3" to "v3"))
config.scopedByLabel().forEach { (name, cfg) ->
    // Process named configurations modA (key1=v1, key2=v2) and modB (key3=v3)
}
```

### Resolving variable references
`ResovedConfig` is a wrapper class that allows variable references in config values to be substituted with values
from system properties, environment variables and the same configuration object (in this order). The wrapper does not 
cache resolved values. Resolution is performed every time a configuration value is read. `resolved()` extension method 
provides a fluent syntax for creating `ResovedConfig`.

Wrap a variable name with `${...}` to reference it. Escape a reference and prevent substitution by doubling the dollar
sign: `$${...}`. The optional default value can be specified after the variable name with a `:-` separator. Both 
variable name and default value can contain references to other variables.

Let's take a look at the following snippet:
```kotlin
val config = MapConfig(mapOf("sel" to "2", "key1" to "\${key\${sel}:-v1}", "key2" to "v2"))
val resolvedConfig = config.resolved()
println(resolvedConfig.get("key1")) // Prints "v2"
```

Config key `key1` is mapped to `${key${sel}}` value. First `${sel}` is substituted with value `2`, then the resulting
reference `${key2:-v1}` is substituted with final value `v2`. If there was no key `key2` in configuration, the
default value `v1` would have been used instead.

### Providing custom configuration implementations
Any class implementing `Config` interface can be used as a configuration object. The library provides 
`PropertiesConfig` and `MapConfig` that wrap `Properties` and `Map<String, String>` instances and expose them 
as a configuration objects. Factory methods of `ConfigLoader` class are designed to simplify loading configuration 
from different sources.
