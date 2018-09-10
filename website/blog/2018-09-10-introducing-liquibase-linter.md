---
title: Introducing Liquibase Linter
author: David Goss
authorURL: http://davidgoss.co/
---

This morning, we're very pleased to announce [Liquibase Linter](https://github.com/whiteclarkegroup/liquibase-linter), an open source lint tool for Liquibase.

We've been working on it internally at White Clarke Group for a few months now, and are excited to have it out in the wild and see the feedback from the Liquibase community.

<!--truncate-->

## Background

We lean on a lot of Liquibase's feature at WCG; we have a core product but with multiple client contexts, we  support several different database vendors, and it's also integral to our release and deployment process.

While we'd had great success with lint tools and other types of testing in pushing up our *code* quality, preventing issues with database scripts had remained elusive; it's quite an error-prone activity &mdash; especially given how difficult it can sometimes be to undo changes once they have gone in &mdash; but there isn't really much tooling to leverage around what is basically some plain XML (or YAML or JSON). There didn't seem to be any kind of quality control tool out there for Liquibase (there still doesn't).

So, we started looking at [Liquibase's extensions system](https://liquibase.jira.com/wiki/spaces/CONTRIB/overview), and how we could use use it to do some parse-time checks that would prevent changes going into a database if they didn't follow the standards we set out. That worked out pretty well, and so Liquibase Linter has been built up from there.

### Configurability

Many lint tools are self-styled as being "opinionated" &mdash; they are somewhat inflexible in the style and conventions they enforce. This can be a good fit for some things (the success of [Standard](https://standardjs.com/) in the JavaScript world is one example) but we knew that making Liquibase Linter like that would be just about the worst thing we could do with it; people's needs from, and usages of, Liquibase vary hugely, and we want as many of them as possible to be able to take advantage of this tool.

So there is no "standard" config &mdash; all the rules are off by default &mdash; and for the [rules you turn on](../../../../docs/rules) you can configure them in a way that makes sense for your project, so it's up to you what you do with contexts, how you name tables and constraints, which databases you support, what types of change you want to allow, etc.

### Influence

I wanted to give a special mention here to [ESLint](https://eslint.org/). We use it on lots of products at WCG and I've been a big fan of the project for years, so it's not really surprising that it ended up influencing the majority of the design decisions we made, from things like what the config file should look like, to the idea of custom rules, to just the impressively high standard of documentation.

## Next

We've already got a lot of changes in the pipeline that might be of interest:

- Aggregating failures and producing a report [(issue)](https://github.com/whiteclarkegroup/liquibase-linter/issues/20)
- Support for defining your own rules [(milestone)](https://github.com/whiteclarkegroup/liquibase-linter/milestone/1)
- Support for YAML and JSON changelog files [(milestone)](https://github.com/whiteclarkegroup/liquibase-linter/milestone/2)
- Support for running with Gradle [(issue)](https://github.com/whiteclarkegroup/liquibase-linter/issues/23)
 
If you find any bugs, have any feature ideas, or think a new core rule should be added, please [submit an issue on GitHub](https://github.com/whiteclarkegroup/liquibase-linter/issues).
