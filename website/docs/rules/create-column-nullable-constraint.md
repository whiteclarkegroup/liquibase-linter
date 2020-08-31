---
title: create-column-nullable-constraint
---

## Why?

When defining a [column](https://www.liquibase.org/documentation/column.html), you are free to omit the `nullable` attribute on the `<constraints>` tag (or just omit the tag altogether), and the implicit default behaviour is that the column is added as nullable. This is fine and works consistently across vendors, but you might want to make the developer have to explicitly state whether the column is nullanble or not, to ensure they have not overlooked this detail in their design.

This rule will fail if a new column is specified without a `<constraints>` tag with a `nullable` attribute.

## Options

No extra options.
