---
title: create-column-no-define-primary-key
---

## Why?

When defining a [column](https://www.liquibase.org/documentation/column.html), it's possible to indicate that it's the primary key, like this:

```xml
<addColumn tableName="FOO">
    <column name="BAR" type="${nvarchar}(50)">
        <constraints primaryKey="true"/>    
    </column>
</addColumn>
```

This is convenient but can be hazardous, because if there's a problem with the creation of the constraint, you are left with a part-done change for that database and will need to manually roll it back. You might prefer to create the primary key constraint in a [separate change](http://www.liquibase.org/documentation/changes/add_primary_key.html).

This rule will fail if a `primaryKey` attribute is found on a `<constraints>` tag when adding a column.

## Options

No extra options.
