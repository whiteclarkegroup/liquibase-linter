---
title: no-reserved-words
---

## Why?

Each SQL dialect has reserved words that you are prevented from using for object names because they have meaning in the language. While many are common (e.g. "SELECT") some are specific to the vendor, and when supporting multiple vendors it's possible to test a script successfully on one database that will fail on another later.

The `has-context` rule will fail if you try to use any known reserved word for an object name, regardless of the current target database.

Further reading:

- [Reserved Keywords (Transact-SQL)](https://docs.microsoft.com/en-us/sql/t-sql/language-elements/reserved-keywords-transact-sql?view=sql-server-2017)
- [Oracle Reserved Words](https://docs.oracle.com/cd/B10501_01/appdev.920/a42525/apb.htm)

## Options

No extra options.
