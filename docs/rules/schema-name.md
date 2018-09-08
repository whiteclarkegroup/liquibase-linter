---
title: schema-name
---

## Why?

You might want to restrict which schema names changes can be made in, or ensure that that are parameterised rather than hardcoded.

The `schema-name` rule matches the regex you provide against any `schemaName` attributes on changes, and fails where it is not matched.

This rule is run _before_ any parameters are replaced, so it can help you ensure that schema names are properly parameterised, if that's important to you. 

## Options

- `pattern` - (regex, as string) regular expression that any `schemaName` attribute should adhere to

## Example Usage

```json
{
    "rules": {
        "schema-name": {
            "enabled": true,
            "pattern": "^\\$\\{[a-z_]+\\}$",
            "errorMessage": "Must use schema name token, not %s"
        }
    }
}
``` 
