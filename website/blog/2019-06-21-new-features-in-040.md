---
title: New Features in Liquibase Linter 0.4.0
author: David Goss
authorURL: http://davidgoss.co/
---

Liquibase Linter [0.4.0 is released](https://github.com/whiteclarkegroup/liquibase-linter/releases/tag/0.4.0) and available now [in Maven Central](https://search.maven.org/artifact/com.whiteclarkegroup/liquibase-linter/0.4.0/jar)!

This release has mostly been about improving the flexibility of existing features and rules.

<!--truncate-->

## New/updated core rules

- The [new `file-not-included` rule](../../../../docs/rules/file-not-included) can be used to flag up when a new script has been created and dropped into the project, but not included in the main changelog (and therefore never run). This is a very easy mistake to make, and would often otherwise rely on code review to catch it.
- The [existing `modify-data-enforce-where` rule](../../../../docs/rules/modify-data-enforce-where) has been given some more power via support for a `pattern` - so now as well as enforcing that updates/deletes on certain tables always have a `<where>` condition, you can also provide a regular expression that describes what that should look like.

## Multiple configs support

Until now, as with most rules-based quality tools, each rule you used in Liquibase Linter would be configured once. Our attempts to make better use of the `modify-data-enforce-where` rule mentioned above showed this up as a limitation; sometimes you want to use a rule in different ways for different use cases. For example, you may have several tables that should never have unqualified updates, but have very different requirements in terms of what the `<where>` condition should look like.

Multiple configs can also be desirable even when you aren't handling different use cases; for any of the rules concerned with naming, you might find that a single regular expression is a little unwieldy, or that having more granular failure messaging would better serve your developers.

So, where normally you would provide a single configuration object for any rule in your config file, you can now [provide an array of them](../../../../docs/rules/index#multiple-configs) if you like, and they will be applied with "AND" logic - so they all have to pass for any change(set|log).




