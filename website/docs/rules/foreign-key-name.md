---
title: foreign-key-name
---

## Why?

You might already have a broad standard for object names - and be enforcing it with [the object-name rule](rules/object-name.md) - but you might also want a more specific rule concerning how foreign key constraints are named.

This rule will fail if the given name for a new foreign key constraint does not match the configured regex.

Note that unlike some other constraint rules, the linter does not enforce a foreign key constraint being explicitly named, because unlike those other constraint rules, Liquibase itself actually requires you to name it.

## Options

- `pattern` - (regex, as string) regular expression that the name of any added foreign key constraint must adhere to
- `dynamicValue` - (string) Spring EL expression, with the [`AddForeignKeyConstraintChange`](https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/change/core/AddForeignKeyConstraintChange.java) instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with `{{value}}`

## Example Usage

To ensure that a pattern is matched, including the base table name:

```json
{
    "rules": {
        "foreign-key-name": {
            "pattern": "^{{value}}_FK\\d$",
            "dynamicValue": "baseTableName",
            "errorMessage": "Foreign key constraint names must be the table name, suffixed with 'FK' and a number, e.g. FOO_FK1"
        }
    }
}
```

To ensure that a pattern is matched, including both table names - unless it would make it too long:

```json
{
    "rules": {
        "foreign-key-name": {
            "pattern": "^{{value}}_FK$",
            "dynamicValue": "(baseTableName + '_' + referencedTableName).length() <= 27 ? baseTableName + '_' + referencedTableName : '[A-Z_]+'",
            "errorMessage": "Foreign key constraint '%s' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits"
        }
    }
}
```
