---
title: file-not-included
---

## Why?

Although generally uncommon, sometimes including a change log in the deltas change log is forgotten.

The `file-not-included` rule will fail if any file in the specified target directories were not included.
The specified directory will not be searched recursively, so only files directly under the path will be checked.

## Options

`values` - (array of strings) list of relative paths to include

## Exmple Usage

```json
{
    "rules": {
        "file-not-included": {
            "values": ["src/main/resources/ddl", "src/main/resources/dml"]
        }
    }
}
```
