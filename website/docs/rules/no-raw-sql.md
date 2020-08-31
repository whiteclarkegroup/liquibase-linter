---
title: no-raw-sql
---

## Why?

Liquibase supports [a lot of different changes](http://www.liquibase.org/documentation/changes/index.html), and raw sql change types are only useful for edge cases or complex cases.
By default changes should be done using Liquibase’s automated refactoring tags and not raw sql.

This rule will fail if either `<sql>` or `<sqlFile>` change types are used.

## Options

No extra options.

## Example Usage

```json
{
    "rules": {
        "no-raw-sql": true
    }
}
```
