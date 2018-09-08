---
title: object-name
---

## Why?

You might already have some standards for how things are named in your databases schemas, to ensure consistency. Schema objects are a pain to rename once they're in, so it's good to be able to enforce this standard at the source (assuming you can express your standard as a regex).

This rule will fail if the given regex does not match against the name for any new:

- Table
- Column
- View
- Primary key
- Foreign key constraint
- Unique constraint
- Index

## Options

- `pattern` - (regex, as string) regular expression that the name of any new schema object should adhere to

## Example Usage

```json
{
    "rules": {
        "object-name": {
            "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",
            "errorMessage": "Object name '%s' name must be uppercase and use '_' separation"
        }
    }
}
``` 

(The above example ensures objects are named like `NAME_OF_THING`.)
