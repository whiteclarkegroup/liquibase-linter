---
title: Using Rules
slug: /rules/
---

Liquibase Linter has several dozen rules that you can turn on and configure to suit your project.

## Turning on a rule

No rules are turned on by default, but most can be turned on simply by adding a key with a `true` value to the `rules` object in the config file:

```json
{
    "rules": {
        "no-duplicate-includes": true
    }
}
```

The value can also be an options object:

```json
{
    "rules": {
        "no-duplicate-includes": {
            "enabled": true
        }
    }
}
```

## Options

All rules also support these standard options (other than `enabled`):

- `errorMessage` - (string) override the default error message for this rule, which is output when the rule fails on a change. This can be useful if you are using a rule in a very targeted way and want to make it clear to the developer why it has failed. Most rules make the invalid value they found available to be interpolated with `%s`.
- `condition` - (string) - [Spring EL expression](https://www.baeldung.com/spring-expression-language) that should resolve to a boolean, which if provided will decide whether the rule should be applied or not. The expression scope is as follows - 
     - [`DatabaseChangeLog`](https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/DatabaseChangeLog.java) object available as `changeLog`
     - [`ChangeSet`](https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/ChangeSet.java) object available as `changeSet`
     - [`Change`](https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/change/Change.java) object available as `change`
     - `matchesContext` helper function which can be used like `matchesContext('foo', 'bar')`. This function just delegates to the liquibase context matching method so the same logic applies.
- `enableAfter` - (string) allows you to specify a change log file name _after_ which this rule should be enabled. See [Retrofitting](../retrofitting.md) for more detail.

Individual rules also support their own options; you can find these documented with those rules.

## Multiple Configs

Though you might not need it often, you can specify multiple configs - with different options - for the same rule. You can do this by providing an array of rule config objects rather than just one, as in this example:

```json
{
    "rules": {
        "object-name": [
            {
                "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",
                "errorMessage": "Object name '%s' name must be uppercase and use '_' separation"
            },
            {
                "pattern": "^POWER.*$",
                "errorMessage": "Object name '%s' name must begin with 'POWER'"
            }
        ]
    }
}
```

If you provide multiple configs, each applicable change/changeset/changelog will be checked with all of the configs in turn. A failure on any of the configs will be treated as a failure - in other words, your scripts have to pass against all the configs, so the logic is "AND" rather than "OR".

## Failure

Once a rule is switched on, it will be run against each of your scripts right after Liquibase parses them from their source format (e.g. XML). If a rule fails (that is, a script broke the rule) then Liquibase will exit with a `ChangeLogParseException` containing details of which change failed and why, and nothing will be run into the target database.
