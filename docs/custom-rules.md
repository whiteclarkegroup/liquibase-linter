---
title: Adding custom rules
---

Adding custom rules to liquibase linter is very easy due to the use of [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html)

## Creating a rule

Starting with a project that depends on both `liquibase-linter` and `liquibase`.

Rules can be created at three different levels
 - Change Log
 - Change Set
 - Change

Simply implement one of the following interfaces and add to the corresponding `META-INF\services` file

| Level  | Interface  | Services file  |
|---|---|---|
| Change Log | ChangeLogRule | com.whiteclarkegroup.liquibaselinter.config.rules.ChangeLogRule |
| Change Set | ChangeSetRule | com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule |
| Change | ChangeRule | com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule |

## Configuring the rule

Add your project as a dependency to the `liquibase-maven-plugin` in the same way you added a dependency for `liquibase-linter`.
So for our example custom rules project `wcg-liquibase-linter` we would have the following dependency.
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
        <dependency>
            <groupId>com.whiteclarkegroup</groupId>
            <artifactId>wcg-liquibase-linter</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

Then configure your rule as normal in your `lqlint.json` file
