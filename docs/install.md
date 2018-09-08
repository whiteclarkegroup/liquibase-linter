---
title: Install
---

Liquibase Linter is built with [the Extensions feature in Liquibase](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview), so it works by simply being on the classpath with Liquibase.

## Maven

The simplest (and best-supported) way to actually do this is to declare it as a dependency of [the Maven plugin](http://www.liquibase.org/documentation/maven/) in your pom:

```xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
        ...
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.whiteclarkegroup</groupId>
            <artifactId>liquibase-linter</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

## Compatibility

Liquibase Linter has been tested with Liquibase versions 3.4.0 through 3.6.x, so you can confidently use it with those. From version 3.7.0, [Liquibase will be making a series of breaking API changes](https://www.liquibase.org/2018/04/liquibase-3-6-0-released.html#looking-forward-api-changes); we'll be working to keep up with those here as they come.

As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself.
