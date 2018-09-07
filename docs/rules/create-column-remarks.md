---
title: create-column-remarks
---

## Why?

When [creating a table](https://www.liquibase.org/documentation/changes/create_table.html) or [adding a column](https://www.liquibase.org/documentation/changes/add_column.html), if you provide a `remarks` attribute on a column it will be added to the schema as a column comment. This can be very valuable information to developers and database administrators in the future, and can also enable generation of information-rich database documentation with some tools. 

This rule will fail if `remarks` are not provided for each column when creating a table, or adding new column(s) to an existing table.

## Options

No extra options.
