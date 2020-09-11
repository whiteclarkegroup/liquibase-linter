---
title: no-schema-name
---

## Why?

Adding a schema name can harm portability of scripts and databases if schema name is not used consistently across all instances. The schema name should not really be needed at the script level, as when running Liquibase the user should have the correct schema access and/or default schema anyway.

The `no-schema-name` rule will fail for any changes that have a populated `schemaName` attribute.

## Options

No extra options.
