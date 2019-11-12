---
title: Retrofitting
---

Using Liquibase Linter in a brand new project is pretty straightforward, but more often than not you'll be retrofitting it to an existing project with a history of changes. It's likely that many of those changes would not pass the set of rules you are applying, but since changes are supposed to be immutable, fixing them retrospectively is not really an option.

Liquibase Linter provides some extra configuration options to help with this.

## `enable-after` at project level

This config option allows you to specify a point in time (a change log file) _after_ which you want lint rules to be run. This would typically be the last change log before you add Liquibase Linter and turn on the rules.

Take this example configuration and change log:

```json
{
    "enable-after": "src/main/resources/example-1.xml",
    "rules": {}
}
```

```xml
<!-- root change log file -->
<databaseChangeLog
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.3.xsd">

    <include relativeToChangelogFile="true" file="example-1.xml"/>
    <include relativeToChangelogFile="true" file="example-2.xml"/>
    <include relativeToChangelogFile="true" file="example-3.xml"/>

</databaseChangeLog>
```

Since we've called out `example-1.xml` as our `enable-after` change log, the linter will start checking from `example-2.xml`.

## `enableAfter` at rule level

Over time you'll probably want to add new rules to your project -- but again there may be historical changes that would fail if you just drop them in. For this, you can specify `enableAfter` at rule level.

It works the same way as the root-level one; the value is a change log file name which marks the point in time after which you want the rule to be checked. For example:

```json
{
    "rules": {
        "has-context": {
            "enableAfter": "last-changeset-before-contexts-became-mandatory.xml"        
        }   
    }
}
```
