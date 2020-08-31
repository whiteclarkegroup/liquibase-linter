---
title: isolate-ddl-changes
---

## Why?

DDL changes (that is, changes that alter the database structure, rather than modifying data), should be separated from all other changes, such that they have a changeSet all to themselves.

Unlike DML (insert, update, delete) changes, DDL changes cannot be rolled back if there is a failure. This means that if a changeSet contains several changes including DDL changes, and one change fails, the database could be left in a part-done state in respect of that changeSet, and require manual intervention to fix.

The `isolate-ddl-changes` rule will fail if any DDL change is not the only change in its changeSet.

## Options

No extra options.
