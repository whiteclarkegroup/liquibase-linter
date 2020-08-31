---
title: table-name
---

## Why?

You might already have a broad standard for object names - and be enforcing it with [the object-name rule](rules/object-name.md) - but you might also want a more specific rule concerning how tables are named.

This rule will fail if the given regex does not match against the name when creating or renaming a table.

## Options

- `pattern` - (regex, as string) regular expression that the name of any created or renamed table must adhere to

## Example Usage

```json
{
    "rules": {
        "table-name": {
            "pattern": "^(?!tbl).*$",
            "errorMessage": "Don't prefix table names with 'tbl'"
        }
    }
}
``` 

(The above example just ensures that the `tbl` prefix convention is not used.)
