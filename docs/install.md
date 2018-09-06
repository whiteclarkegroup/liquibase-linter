---
title: Install
---

Liquibase Linter is built with [the Extensions feature in Liquibase](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview), so it works by simply being on the classpath with Liquibase. The simplest (and best-supported) way to actually do this is to declare it as a dependency of [the Maven plugin](http://www.liquibase.org/documentation/maven/) in your pom:

```xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
        ...
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.whiteclarkegroup.liquibase</groupId>
            <artifactId>liquibase-linter</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

