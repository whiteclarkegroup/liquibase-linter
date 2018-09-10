---
title: no-duplicate-includes
---

## Why?

Two `<include>`s with the same file path will cause Liquibase to fail, but in a somewhat opaque way - it will say the changeSet's `id` is duplicated, which might cause you to go hunting around the project looking for a second changeSet with the same `id`, only to realise sometime later that it was just included twice in the changelog, probably due to a bad Git merge.

Also, if you are running Liquibase with contexts that don't match those in the file you've included twice, Liquibase won't fail at all, until someone somewhere tries to run it _with_ those context(s) on.

The `no-duplicate-includes` rule will explicitly fail because of the duplicated `<include>`.

There is an exception to this rule: the linter will *not* fail on duplicated includes where the included changelog doesn't contain any changeSets. This is to allow files that just contain properties to be harmlessly included more than once in situations where the overall changelog is composed of others that might all have the same basic setup included.

## Options

No extra options.
