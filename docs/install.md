---
title: Install
---

Liquibase Linter is built with [the Extensions feature in Liquibase](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview), so it works by simply being on the classpath with Liquibase.

## Maven

Liquibase Linter is available from Maven Central:
https://search.maven.org/search?q=a:liquibase-linter

The easiest way to use it is as a dependency of [the Liquibase Maven plugin](http://www.liquibase.org/documentation/maven/):

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
            <version>0.2.1</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

## Compatibility

Liquibase Linter has been tested with Liquibase versions 3.4.0 through 3.6.x, so you can confidently use it with those. From version 3.7.0, [Liquibase will be making a series of breaking API changes](https://www.liquibase.org/2018/04/liquibase-3-6-0-released.html#looking-forward-api-changes); we'll be working to keep up with those here as they happen.

As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself.
