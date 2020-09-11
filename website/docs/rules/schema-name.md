---
title: schema-name
---

## Why?

You might want to restrict which schema names changes can be made in.

The `schema-name` rule matches the regex you provide against any `schemaName` attributes on changes, and fails where it is not matched.

## Options

- `pattern` - (regex, as string) regular expression that any `schemaName` attribute should adhere to

## Example Usage

```json
{
    "rules": {
        "schema-name": {
            "enabled": true,
            "pattern": "^FOO_SCHEMA$",
            "errorMessage": "Schema name must follow pattern '%s'"
        }
    }
}
``` 
