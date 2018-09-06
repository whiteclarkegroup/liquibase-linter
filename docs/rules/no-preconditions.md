---
title: no-preconditions
---

## Why?

[Preconditions in Liquibase](https://www.liquibase.org/documentation/preconditions.html) are fantastically useful in theory, but in practise they can come back to bite you. When you run the `updateSQL` method, or are using offline mode, Liquibase can't add the conditional logic to the output script, so it just has to assume it will pass, so it does.

Unless you are using `update` _all the way_ through to production (it's unusual), this means anything you guard with a precondition will eventually be run unconditionally. It's better practise to do some additional changes to fix any inconsistencies in data that are holding you up.

The `no-preconditions` rule will fail if any preconditions are used.

## Options

No extra options.
