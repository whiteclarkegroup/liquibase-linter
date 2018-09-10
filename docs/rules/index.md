---
title: Using Rules
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

All rules also support two standard options (other than `enabled`):

- `errorMessage` - (string) override the default error message for this rule, which is output when the rule fails on a change. This can be useful if you are using a rule in a very targeted way and want to make it clear to the developer why it has failed. Most rules make the invalid value they found available to be interpolated with `%s`.
- `condition` - (string) - [Spring EL expression](https://www.baeldung.com/spring-expression-language) that should resolve to a boolean, which if provided will decide whether the rule should be applied or not. The expression scope is the [`Change`](https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/change/Change.java) object.

Individual rules also support their own options; you can find these documented with those rules.

## Failure

Once a rule is switched on, it will be run against each of your scripts right after Liquibase parses them from their source format (e.g. XML). If a rule fails (that is, a script broke the rule) then Liquibase will exit with a `ChangeLogParseException` containing details of which change failed and why, and nothing will be run into the target database.
