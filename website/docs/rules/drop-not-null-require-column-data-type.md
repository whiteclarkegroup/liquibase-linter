---
title: drop-not-null-require-column-data-type
---

## Why?

When [dropping a not-null constraint](http://www.liquibase.org/documentation/changes/drop_not_null_constraint.html), some database vendors will fail if the `columnDataType` is not specified, while others will work fine. This can lead to problems if not caught soon enough.

This rule will fail if a `dropNotNullConstaint` is used without a `columnDataType`. 

## Options

No extra options.
