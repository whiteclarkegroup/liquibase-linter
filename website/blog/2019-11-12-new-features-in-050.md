---
title: New Features in Liquibase Linter 0.5.0
author: David Goss
authorURL: http://davidgoss.co/
---

Liquibase Linter [0.5.0 is released](https://github.com/whiteclarkegroup/liquibase-linter/releases/tag/0.5.0) and available now [in Maven Central](https://search.maven.org/artifact/com.whiteclarkegroup/liquibase-linter/0.5.0/jar)!

This release has mostly been about making it easier to integrate the linter into an existing project.

<!--truncate-->

## Config options to support retrofitting

Using Liquibase Linter in a brand new project is pretty straightforward, but more often than not you'll be retrofitting it to an existing project with a history of changes. It's likely that many of those changes would not pass the set of rules you are applying, but since changes are supposed to be immutable, fixing them retrospectively is not really an option.

The configuration file now supports an "enable-after" property that can be specified at the root level and/or on individual rules, which tells the linter to only check change logs after that one. Read more on the [Retrofitting](/liquibase-linter/docs/retrofitting) page.

There's also the new "ignore-files-pattern" option, which is regular expression that will cause linting to be skipped for change logs whose file path match against it. If you are able to segregate your historical changes into a separate directory/module when integrating Liquibase Linter, this might be a simpler way to exclude them from linting.

## Support for formatted SQL change logs

Whilst we already had support for XML, JSON and YAML formats, support for [Formatted SQL format](https://www.liquibase.org/documentation/sql_format.html) was missing until now - not that we would recommend using formatted SQL changes...

## Technical improvements

There have been some good changes under the hood as well:

- The integration tests have been refactored; it's now much easier to add a new test
- PMD is now running against the project for increased quality control
- The Travis build now runs against a range of Liquibase versions
- Several dependencies were brought up to date

## Next

We have a few key things lined up as we move towards a 1.0.0 release:

- Providing a "recommended" config that you can extend
- Reporting in JUnit format for CI tools
- Providing a JSON schema for the configuration file
- Improved documentation for running with tools other than Maven



 




