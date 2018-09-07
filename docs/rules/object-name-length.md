---
title: object-name-length
---

## Why?

Database vendors vary in what length of object (table, column, constraint etc) names they support. If your project works across multiple vendors, you might find a script could fail on some databases when it has already run successfully on some others, due to the object name length. This then requires manual intervention to fix, which is prone to further mistakes.

For example:

- **SQL Server** [supports 128 characters](https://technet.microsoft.com/en-us/library/ms172451(v=sql.110).aspx), and has done since at least 2008
- **Oracle** only [supported up to 30 characters until 12.2](https://stackoverflow.com/questions/756558/what-is-the-maximum-length-of-a-table-name-in-oracle) when it changed to 128 characters - but earlier versions are still in widespread use, especially in enterprise settings
- **MySQL** [supports up to 64 characters](https://dev.mysql.com/doc/refman/8.0/en/identifiers.html)
- **PostgreSQL** [supports up to 63 characters](https://www.postgresql.org/docs/current/static/sql-syntax-lexical.html#SQL-SYNTAX-IDENTIFIERS) - but will automatically truncate longer names and use the result, without erroring <span style="white-space:nowrap">¯\\\_(ツ)\_/¯</span>
- **H2** apparently [has no limits](http://www.h2database.com/html/advanced.html#limits_limitations) on object names

This rule will fail if the given maximum length is exceeded by the name for any new:

- Table
- Column
- View
- Primary key
- Foreign key constraint
- Unique constraint
- Index

## Options

- `maxLength` - (number) the maximum length of the name of any schema object to be created 

## Example Usage

```json
{
    "rules": {
        "object-name-length": {
            "maxLength": 63
        }
    }
}
``` 
