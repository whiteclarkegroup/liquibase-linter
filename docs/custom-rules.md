---
title: Implementing a Custom Rule
---

Whilst Liquibase Linter has lots of good core rules, you may have a use case that's particular to your project and wouldn't make sense as a core rule.

Fortunately it's trivial to implement custom rules and apply them in your own project; you just need to write a Java class implementing one of our rule interfaces, and do a little configuration. 

## Example use case

Let's say for argument's sake that we have a Liquibase project for an app. We ship customised implementations of this app to different clients, so we have a `core` context and then client-specific contexts like `client_jane` and `client_john`.

In the database for our app, we have a table called `FORM_LAYOUT` that holds configuration for how forms are laid out, which is always different for each client. One day, somebody makes a mistake and does a Liquibase change script to update `FORM_LAYOUT` for Jane, but accidentally uses the `core` context. It slips through code review and torpedoes all other client's form layout configs.

We want to stop this happening again at the source, so let's see if we can do it with a lint rule.

## Writing the rule

There are three interfaces you could implement when writing a custom rule in Java; which you use depends on what level you want to work at.

- [ChangeRule](https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeRule.java) for linting each individual change, useful when you want to prevent issues with the content of the change itself
- [ChangeSetRule](https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeSetRule.java) for linting each changeSet, useful when you want to check things like comments and contexts, or the overall content of a changeSet e.g. when you want ensure certain changes happen together, or in isolation
- [ChangeLogRule](https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeLogRule.java) for linting each changeLog file, useful when you want to check the overall content at changeLog level - rarely used in practise

The `ChangeRule` interface uses generics, so you can target a specific change type e.g. `implements ChangeRule<InsertDataChange>` for inserts, or just do `implements ChangeRule<Change>` to catch all changes. You can also use this to lint [custom changes](http://www.liquibase.org/documentation/changes/custom_change.html), if you have any of those in your project.

It's worth noting that the three levels are not isolated from one another - a `Change` has access to the `ChangeSet` it belongs to, which in turn can access the `ChangeLog` it belongs to, and vice versa, so you can freely traverse to get the information you need to decide if your rule is failed.

For our `FORM_LAYOUT` use case, it makes the most sense to lint at changeSet level. Here's the code to implement it:

```java
package com.fake.fancyapp.liquibase;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;

public class FormLayoutContextRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "form-layout-context";
    private static final String MESSAGE = "FORM_LAYOUT should only ever be updated in a client-specific context!";

    public FormLayoutContextRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return isForFormLayout(changeSet) && isCoreContext(changeSet);
    }

    private boolean isForFormLayout(ChangeSet changeSet) {
        return changeSet.getChanges().stream()
            .anyMatch(change -> change instanceof UpdateDataChange && "FORM_LAYOUT".equals(((UpdateDataChange) change).getTableName()));
    }

    private boolean isCoreContext(ChangeSet changeSet) {
        return changeSet.getContexts().getContexts().stream().anyMatch("core"::equals);
    }
}
```

Some notes about how we've done this:

- We've extended the `AbstractLintRule` class, which saves us fussing about a lot of boilerplate ourselves. We just need to pass our rule name (i.e. the key uses in the [rules config](rules)) and the failure message
- The key method we need to implement ourselves is `invalid` - this accepts a [Liquibase `ChangeSet`](https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/changelog/ChangeSet.java) and should return true if the rule has _failed_ - in this case it will do so if any of the changes are an update on the `FORM_LAYOUT` table _and_ the `core` context is used

All the core rules are implemented in this way as well, so if you're not sure how best to hook something up you might try looking in the source at [some existing core rules](https://github.com/whiteclarkegroup/liquibase-linter/tree/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/core) that do something similar

## Making the rule discoverable

Now, our class above should go into a new Maven project that depends on both `liquibase-linter` and `liquibase`.

The class existing isn't quite enough on its own; we need to tell Liquibase Linter that it's there. For this we are using the [Service Provider Interface](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) pattern - this is natively supported in Java and for use cases like this is preferable to powerful-but-heavy classpath scanning approaches like the one used by Spring.

In our newly-created project, we'll create a new file at:

`src/main/resources/META-INF/services/com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule`

And in the file, we'll write:

`com.fake.fancyapp.liquibase.FormLayoutContextRuleImpl`

## Configuring the rule

In the project where our scripts live, we'll add a dependency on our rules project to `liquibase-maven-plugin`, in much the same way that we [added a dependency for `liquibase-linter` originally](configure):
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
        </dependency>
        <dependency>
            <groupId>com.fake.fancyapp</groupId>
            <artifactId>liquibase-linter-rules</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
```

Then all we need is to [configure the rule as normal](rules) in `lqlint.json`.
