---
title: Implementing a Custom Reporter
---

While Liquibase Linter has some good core reporters, you may have a use case that's particular to your project and
wouldn't make sense as a core rule.

Fortunately it's trivial to implement custom reporters and apply them in your own project; you just need to write a
Java class implementing the reporting interface and do a little configuration. 

## Writing the reporter

There are a few interfaces to implement when writing a custom reporter in Java:
- [Reporter](https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/report/Reporter.java) is the actual interface that your reporter must implement
- [Reporter.Factory](https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/report/Reporter.java) is the piece which ties the reporter into the `lqlint.json` configuration.

Fortunately, there are existing abstract classes that make this easy to do. Furthermore, the `Reporter.Factory` can
exist as inner classes to the main `Reporter` implementation.

### Sample `Reporter` implementation

```java
package com.fake.fancyapp.liquibase;

import com.whiteclarkegroup.liquibaselinter.report.AbstractReporter;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReporterConfig;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import java.util.List;
import java.io.PrintWriter;

public class CustomReporter extends AbstractReporter {
    private static final String NAME = "custom-reporter";

    public CustomReporter(ReporterConfig config) {
        super(config, "ext"); // reports will have a `.ext` file extension
    }

    @Override
    protected void printReport(PrintWriter output, Report report, List<ReportItem> items) {
        // The 'items' have already been filtered.
        // All that is left to do is produce the output.
        // Alternatively, extend an existing core reporter and override methods.
    }

    public static class Factory extends AbstractReporter.Factory<CustomReporter> {
        public Factory() {
            super(NAME);
        }
    }
}
```

Some notes about how we've done this:

- Extend the `AbstractReporter` class, which saves us from creating a lot of boilerplate ourselves.
- Create `Factory`. This links the `CustomReporter` to the Liquibase Linter configuration.  

All the core reporters are implemented in this way as well, so if you're not sure how best to hook something up you
might try looking in the source at
[some existing core reporters](https://github.com/whiteclarkegroup/liquibase-linter/tree/master/src/main/java/com/whiteclarkegroup/liquibaselinter/report)
that do something similar

## Making the reporter discoverable

The class above should go into a new Maven project that depends on both `liquibase-linter` and `liquibase`.

The fact that the class exists isn't quite enough on its own; we need to tell Liquibase Linter that it's there. For this
we are using the [Service Provider Interface](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) pattern - 
this is natively supported in Java and for use cases like this is preferable to powerful-but-heavy classpath scanning
approaches like that used by Spring.

In our newly-created project, we'll create a new file at:

`src/main/resources/META-INF/services/com.whiteclarkegroup.liquibaselinter.report.Reporter.Factory`

And in the file, we'll write:

`com.fake.fancyapp.liquibase.CustomReporter.Factory`

## Configuring the reporter in Maven

In the project where our scripts live, we'll add a dependency on our rules project to `liquibase-maven-plugin`, in much
the same way that we [added a dependency for `liquibase-linter` originally](configure.md):
So for our example custom reporting project `wcg-liquibase-linter` we would have the following dependency.

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
        </dependency>
        <dependency>
            <groupId>com.fake.fancyapp</groupId>
            <artifactId>liquibase-reporters</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

Then all we need is to [configure the reporter as normal](reporting/index.md) in `lqlint.json`.

## Adding custom config to the `Reporter`

If additional configuration opens are required for the `CustomReporter` to operate, extend `ReporterConfig` and create
a new builder that extends `ReporterConfig.BaseBuilder` and change the `Factory` to extend `ReporterConfig.BaseFactory`,
adding in the `Config` class to the generic type declaration. Add `@JsonDeserialize` to the `Config` class so that the
`CustomReporter.Config` can be loaded from the Liquibase Linter configuration.

```java
package com.fake.fancyapp.liquibase;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.whiteclarkegroup.liquibaselinter.report.AbstractReporter;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReporterConfig;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import java.util.List;
import java.io.PrintWriter;

public class CustomReporter extends AbstractReporter {
    private static final String NAME = "custom-reporter";

    final String customConfigOption;

    protected CustomReporter(CustomReporter.Config config) {
        super(config);
        this.customConfigOption = config.customConfigOption;
    }

    @Override
    protected void printReport(PrintWriter output, Report report, List<ReportItem> items) {
        // The 'items' have already been filtered.
        // All that is left to do is produce the output.
        // Alternatively, extend an existing core reporter and override methods.
    }

    public static class Factory extends AbstractReporter.BaseFactory<CustomReporter, Config> {
        public Factory() {
            super(NAME);
        }
    }

    @JsonDeserialize(builder = Builder.class)
    public static class Config extends ReporterConfig {
        final String customConfigOption;
        
        public Config(Builder builder) {
            super(builder);
            customConfigOption = builder.customConfigOption;
        }
    }

    public static class Builder extends ReporterConfig.BaseBuilder<Builder> {
        String customConfigOption;

        public Builder withCustomConfigOption(String customConfigOption) {
            this.customConfigOption = customConfigOption;
            return this;
        }

        @Override
        public Config build() {
            return new Config(this);
        }
    }
}
```
