---
title: create-table-remarks
---

## Why?

When [creating a table](https://www.liquibase.org/documentation/changes/create_table.html), if you provide a `remarks` attribute it will be added to the schema as a table comment. This can be very valuable information to developers and database administrators in the future, and can also enable generation of information-rich database documentation with some tools. 

This rule will fail if `remarks` are not provided when creating a table.

## Options

No extra options.
