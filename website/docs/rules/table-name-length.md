---
title: table-name-length
---

## Why?

You might already be employing [the object-name-length rule](rules/object-name-length.md) to prevent names that are too long across all types of schema objects. If you also have conventions around constraints and indexes that involve using the table name as a prefix, you might want to override the maximum length for a table's name, to ensure you can always meet your other naming standards.

For example, let's say you have your object names limited to 30 characters, and then create a table named `APP_FAILED_PROPOSAL_VALIDATION`, which just fits. Now you want to add a primary key, and your convention is to suffix the table name with `_PK`, meaning the correct constraint name would be `APP_FAILED_PROPOSAL_VALIDATION_PK` - which is 33 characters long. So, you could limit your table names to 27 characters to guard against this without having to drop the naming standard.

This rule will fail if the given maximum length is exceeded by the name when creating or renaming a table.

## Options

- `maxLength` - (number) the maximum length of the name of any created or renamed table

## Example Usage

```json
{
    "rules": {
        "table-name-length": {
            "maxLength": 60
        }
    }
}
``` 
