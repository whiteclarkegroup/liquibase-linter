---
title: valid-context
---

## Why?

You might have a very specific set of valid [contexts](https://www.liquibase.org/documentation/contexts.html) in your Liquibase setup. If so, you might want to ensure that every specified context is in that set; otherwise, a minor typo in a context could cause the changeSet to silently never be run anywhere.

The `valid-context` rule will fail if any contexts of a changeSet don't match the provided regex.

This rule will not enforce the prescence of a context; see [has-context](has-context.md) for that.

## Options

- `pattern` - (regex, as string) regular expression that any specified context should adhere to

## Example Usage

```json
{
    "rules": {
        "valid-context": {
            "enabled": true,
            "pattern": "^.*_foo",
            "errorMessage": "Context is incorrect, should end with '_foo'"
        }
    }
}
``` 
