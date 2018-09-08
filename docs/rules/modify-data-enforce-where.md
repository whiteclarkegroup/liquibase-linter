---
title: modify-data-enforce-where
---

## Why?

When updating or deleting data in a table, it's possible to do an unqualified modification; that is, not provided a `<where>` condition so that all rows are updated/deleted.

Whilst this can be a valid usage, it is prone to mistakes, and there might be some tables on which an oversight could be particularly harmful, like those with multi-tenant (or otherwise scoped) data.

This rule will fail if an update or delete change on any of the given tables does not include a `<where>` condition.

## Options

`values` - (array of strings) list of table names that cannot have unqualified modifications

## Exmple Usage

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
