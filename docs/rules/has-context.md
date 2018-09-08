---
title: has-context
---

## Why?

Usages of [contexts](https://www.liquibase.org/documentation/contexts.html) vary a lot, but if you are using them heavily then you might want to make sure that all changeSets have a context, since omitting it will mean the changeSet is _always_ run.

The `has-context` rule will fail if any changeSets don't have a populated `context` attirbute.

## Options

No extra options.
