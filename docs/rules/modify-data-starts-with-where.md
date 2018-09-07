---
title: modify-data-starts-with-where
---

## Why?

This mistake can be common with people newer to Liquibase, or where a Liquibase script is being reverse-engineered from some raw SQL:

```xml
<delete tableName="FOO">
    <where>WHERE BAR IS NOT NULL</where>
</delete>
```

If you try to run this, Liquibase will fail with a SQL syntax error. You might want to make it clear right away to the developer what they did wrong in this scenario.

This rule will fail if the content of a `<where>` tag starts with "where" (case-insensitive).

## Options

No extra options.
