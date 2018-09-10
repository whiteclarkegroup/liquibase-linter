---
title: primary-key-name
---

## Why?

When [adding a primary key](http://www.liquibase.org/documentation/changes/add_primary_key.html), it's possible to omit a specific `constraintName` for the primary key. This is hazardous, because the database vendor will automatically name it for you in an unpredictable way, which makes things difficult later if you want to reference or remove it.

Also, you might already have a broad standard for object names - and be enforcing it with [the object-name rule](rules/object-name.md) - but you might also want a more specific rule concerning how primary keys are named.

This rule will fail if there is no `constraintName` given when adding a primary key, or when configured with a pattern, will fail if the given name does not match the pattern.

## Options

- `pattern` - (regex, as string) optional regular expression that the name of any added primary key must adhere to
- `dynamicValue` - (string) Spring EL expression, with the [`AddPrimaryKeyChange`](https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/change/core/AddPrimaryKeyChange.java) instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with `{{value}}`

## Example Usage

To simply ensure that a name is always given:

```json
{
    "rules": {
        "primary-key-name": true
    }
}
```

To ensure that a pattern is matched, including the table name:

```json
{
    "rules": {
        "primary-key-name": {
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "tableName",
            "errorMessage": "Primary key names must be the table name, suffixed with 'PK', e.g. FOO_PK"
        }
    }
}
```
