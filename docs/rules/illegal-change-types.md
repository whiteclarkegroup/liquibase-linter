---
title: illegal-change-types
---

## Why?

Liquibase supports [a lot of different changes](http://www.liquibase.org/documentation/changes/index.html), but there might be some you just don't want to be used in your project for one reason or another.

This rule will fail if one of the change types provided is ever used in a script.

## Options

`values` - (array of strings) list of change types that should not be used; each can be expressed as either the tag name or the full Java class name of the [change type](https://github.com/liquibase/liquibase/tree/master/liquibase-core/src/main/java/liquibase/change/core)

## Exmple Usage

To prevent `loadData` changes being used:

```json
{
    "rules": {
        "illegal-change-types": {
            "values": ["loadData"]
        }
    }
}
```

This is exactly equivalent to the above, but using the class name:

```json
{
    "rules": {
        "illegal-change-types": {
            "values": ["liquibase.change.core.LoadDataChange"]
        }
    }
}
```
