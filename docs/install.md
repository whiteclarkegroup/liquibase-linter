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
            <version>0.5.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

## Command Line

Starting with the latest [Liquibase release zip](https://github.com/liquibase/liquibase/releases/).
 
Download the latest Liquibase Linter jar from [maven central](https://repo1.maven.org/maven2/com/whiteclarkegroup/liquibase-linter/) and download 
the [dependencies](https://mvnrepository.com/artifact/com.whiteclarkegroup/liquibase-linter) required by Liquibase Linter, then add them to 
the `lib` directory.

Finally add your `lqlint.json` configuration file to the `lib` directory.

## Compatibility

It doesn't matter whether you use Liquibase scripts written in XML, JSON or YAML, they will be linted just the same.

Liquibase Linter has been tested with Liquibase versions 3.4.0 through 3.7.x, so you can confidently use it with those. We'll be working to keep up with newer versions of Liquibase as they happen.

As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself.
