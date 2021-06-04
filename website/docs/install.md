---
title: Install
---

Liquibase Linter is built with [the Extensions feature in Liquibase](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview), so it works by simply being on the classpath with Liquibase.

## Maven

1. Add `liquibase-linter` as a dependency of [the Liquibase Maven plugin](http://www.liquibase.org/documentation/maven/):
2. Add `lqlint.json` to the root of your project

See this simple [example](https://github.com/whiteclarkegroup/liquibase-linter/tree/main/examples/maven) maven project to help get you started

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
            <version>0.5.1</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

## Gradle

1. Add `liquibase-linter` as a dependency of [the Liquibase Gradle plugin](https://github.com/liquibase/liquibase-gradle-plugin):
2. Add `lqlint.json` to the `lqlint` directory under the root of your project

See this simple [example](https://github.com/whiteclarkegroup/liquibase-linter/tree/main/examples/gradle) gradle project to help get you started

```groovy
dependencies {
    liquibaseRuntime 'org.liquibase:liquibase-core:3.8.1'
    liquibaseRuntime 'org.liquibase:liquibase-groovy-dsl:2.1.1'
    liquibaseRuntime 'org.hsqldb:hsqldb:2.5.0'
    liquibaseRuntime 'com.whiteclarkegroup:liquibase-linter:0.5.1'
    liquibaseRuntime files('lqlint')
}
```

## Command Line

1. Start with the latest [Liquibase release zip](https://github.com/liquibase/liquibase/releases/).
2. Download the latest Liquibase Linter jar from [maven central](https://repo1.maven.org/maven2/com/whiteclarkegroup/liquibase-linter/) and download 
the [dependencies](https://mvnrepository.com/artifact/com.whiteclarkegroup/liquibase-linter) required by Liquibase Linter, then add them to 
the `lib` directory.
3. Add your `lqlint.json` configuration file to the `lib` directory.

## Compatibility

It doesn't matter whether you use Liquibase scripts written in XML, JSON or YAML, they will be linted just the same.

Liquibase Linter has been tested with Liquibase versions 3.4.0 through to the latest version, so you can confidently use it with those. We'll be working to keep up with newer versions of Liquibase as they happen.

As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself.
