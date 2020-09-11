---
title: modify-data-enforce-where
---

## Why?

When updating or deleting data in a table, it's possible to do an unqualified modification; that is, not provided a `<where>` condition so that all rows are updated/deleted.

Whilst this can be a valid usage, it is prone to mistakes, and there might be some tables on which an oversight could be particularly harmful, like those with multi-tenant (or otherwise scoped) data.

Additionally, even qualified updates on certain tables can be problematic if they are not differentiating on a particular column or columns, so you can get more specific about this by providing a regular expression that any `<where>` condition must adhere to.

This rule will fail if an update or delete change on any of the given tables does not include a `<where>` condition, or if a `<where>` condition is included but does not match a specified pattern.

## Options

- `values` - (array of regex strings) list of table name patterns that cannot have unqualified modifications
- `pattern` - (regex, as string) optional regular expression that `<where>` conditions must adhere to

## Example Usage

A basic usage:

```json
{
    "rules": {
        "modify-data-enforce-where": {
            "values": ["SETTINGS"],
            "errorMessage": "Updates and deletes to settings table must have a where condition"
        }
    }
}
```

With the addition of a pattern:

```json
{
    "rules": {
        "modify-data-enforce-where": {
            "values": ["SETTINGS"],
            "pattern": "^.*GROUP =.*$",
            "errorMessage": "Updates and deletes to settings table must have a where condition that references group column"
        }
    }
}
```
