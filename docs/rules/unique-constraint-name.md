---
title: unique-constraint-name
---

## Why?

When [adding a unique constraint](http://www.liquibase.org/documentation/changes/add_unique_constraint.html), it's possible to omit a specific `constraintName` for the constraint. This is hazardous, because the database vendor will automatically name it for you in an unpredictable way, which makes things difficult later if you want to reference or remove it.

Also, you might already have a broad standard for object names - and be enforcing it with [the object-name rule](rules/object-name.md) - but you might also want a more specific rule concerning how unique constraints are named.

This rule will fail if there is no `constraintName` given when adding a unique constraint, or when configured with a pattern, will fail if the given name does not match the pattern.

## Options

- `pattern` - (regex, as string) optional regular expression that the name of any added unique constraint must adhere to
- `dynamicValue` - (string) Spring EL expression, with the [`AddUniqueConstraintChange`](https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/change/core/AddUniqueConstraintChange.java) instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with `{{value}}`

## Example Usage

To simply ensure that a name is always given:

```json
{
    "rules": {
        "unique-constraint-name": true
    }
}
```

To ensure that a pattern is matched, including the table name:

```json
{
    "rules": {
        "unique-constraint-name": {
            "pattern": "^{{value}}_U\\d$",
            "dynamicValue": "tableName",
            "errorMessage": "Unique constraint names must be the table name, suffixed with 'U' and a number, e.g. FOO_U2"
        }
    }
}
```
