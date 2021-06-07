---
title: console
---

## Format

Displays the linting results to a standard Markdown `.md` file. By default, the report
is saved at `./target/lqlint-report.md`

## Options

No extra options.

## Sample report
### `src/main/resources/ddl/first.xml`
| Change Set | Status  | Rule                | Message                                                                      |
|------------|---------|---------------------|------------------------------------------------------------------------------|
| *none*     | ERROR   | file-name-no-spaces | Changelog filenames should not contain spaces                                |
|            | IGNORED | no-preconditions    | Preconditions are not allowed in this project                                |
| 2020010101 | ERROR   | create-index-name   | Index name does not follow pattern                                           |
|            | IGNORED | no-raw-sql          | Raw sql change types are not allowed, use appropriate Liquibase change types |

### `src/main/resources/ddl/second.xml`
| Change Set | Status | Rule                | Message                            |
|------------|--------|---------------------|------------------------------------|
| 2020010101 | ERROR  | create-index-name   | Index name does not follow pattern |

### Summary
* ERROR: 3
* IGNORED: 2
* PASSED: 6
* DISABLED: 2
