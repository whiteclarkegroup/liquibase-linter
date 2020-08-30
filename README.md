# Liquibase Linter

*Quality control for your Liquibase scripts*

[![Build Status](https://travis-ci.org/whiteclarkegroup/liquibase-linter.svg?branch=master)](https://travis-ci.org/whiteclarkegroup/liquibase-linter) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/320a8a4be4fd44feb9d6102ccdc7e240)](https://www.codacy.com/project/whiteclarkegroup/liquibase-linter/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=whiteclarkegroup/liquibase-linter&amp;utm_campaign=Badge_Grade_Dashboard) ![Maven Central](https://img.shields.io/maven-central/v/com.whiteclarkegroup/liquibase-linter.svg) [![Gitter](https://badges.gitter.im/liquibase-linter/community.svg)](https://gitter.im/liquibase-linter/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

## Get Started

**1** Add `liquibase-linter` to your pom as a dependency of `liquibase-maven-plugin`:

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

**2** Add the [config file](website/examples/lqlint.json) to your project root, and start turning on rules.

Try the [full documentation](https://whiteclarkegroup.github.io/liquibase-linter) for details of config and rules.
