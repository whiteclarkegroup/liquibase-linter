---
title: file-not-included
---

## Why?

Although generally uncommon, sometimes including a change log in the root deltas change log is forgotten.

The `file-not-included` rule will fail if any file in the specified target directories were not included.
The specified directory is _searched recursively_. The check is also only performed against files which have
the same file extension as the root deltas change log. For example, if you are using `xml` change logs, a `*.json`
file would be ignored.

## Options

`values` - (array of strings) list of paths relative to the classpath of the maven module Liquibase is being run from that should be checked for not-included files


## Example Usage

```json
{
    "rules": {
        "file-not-included": {
            "values": ["src/main/resources/ddl", "src/main/resources/dml"]
        }
    }
}
```
