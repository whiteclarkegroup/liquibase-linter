---
title: separate-ddl-context
---

## Why?

You might want to make sure that DDL changes are kept in a separate context or set of contexts. This can be useful when you need to be able to scaffold the database structure without any of the data, in order to run tests for instance.

The `separate-ddl-context` rule will fail if the context of a changeSet with DDL changes is not matched by the provided regex.

This rule would typically be used in conjunction with [isolate-ddl-changes](isolate-ddl-changes.md).

## Options

- `pattern` - (regex, as string) regular expression that any specified context for DDL changes should adhere to

## Example Usage

```json
{
    "rules": {
        "separate-ddl-context": {
            "enabled": true,
            "pattern": "^ddl$",
            "errorMessage": "DDL changes should only use 'ddl' context"
        }
    }
}
``` 
