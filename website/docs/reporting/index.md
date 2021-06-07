---
title: Using Reporters
slug: /reporting/
---

Liquibase Linter has several types of reporters that you can turn on and configure to suit your needs.

## Turning on a reporter
By default, only the [`console`](console.md) reporter is enabled. 

The simmplest way to use a reporter is to simply enable it:
```json
{
    "reporting": {
        "markdown": true
    }
}
```

By default, the report is written to the `./target` directory. It this example,
the file would be written to `./target/lqlint-report.md`. An alternate location
can be configured by simply specifying it as such:
```json
{
    "reporting": {
        "markdown": "custom/path/report.md"
    }
}
```

The value can also be an options object:
```json
{
    "reporting": {
        "markdown": {
            "enabled": true,
            "path": "custom/path/report.md",
            "filter": [ "ERROR", "IGNORED", "PASSED" ]
        }
    }
}
```

## Options

All reporters support these standard options:
- `enabled` - (boolean) enable or disable a reporter
- `path` - (string) specifies the location where the report will be written
- `filter` - (string array) specifies what type of linting results will be included.
  Typically only `ERROR` and `IGNORED` violations are reported.
    - `ERROR` (string) rule violations that were not ignored or skipped
    - `IGNORED` (string) rule violations that were ignored
    - `PASSED` (string) rule evaluations that did not produce a violation

Individual reporters can also support their own options; you can find these documented with those reporters.

## Multiple Configs

Though you might not need it often, you can specify multiple configs - with
different options - for the same reporter. In this way you could produce
multiple reports of the same type with different options. For example, you could
produce multiple `markdown` reports, each containing different `filter` results.

This is configured by providing an array of reporting config objects rather than just one, as in this example:

```json
{
    "reporting": {
        "markdown": [
            {
                "path": "./target/lqlint-full-report.md",
                "filter": ["ERROR", "IGNORED", "PASSED"]
            },
            {
                "path": "./target/lqlint-errors-report.md",
                "filter": ["ERROR"]
            }
        ]
    }
}
```
