---
title: no-duplicate-includes
---

## Why?

Two `<include>`s with the same file path will cause Liquibase to fail, but in a somewhat opaque way - it will say the changeSet's `id` is duplicated, which might cause you to go hunting around the project looking for a second changeSet with the same `id`, only to realise sometime later that it was just included twice in the changelog, probably due to a bad Git merge.

Also, if you are running Liquibase with contexts that don't match those in the file you've included twice, Liquibase won't fail at all, until someone somewhere tries to run it _with_ those context(s) on.

The `no-duplicate-includes` rule will explicitly fail because of the duplicated `<include>`.

## Options

No extra options.
