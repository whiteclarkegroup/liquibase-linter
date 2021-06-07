# `src/main/resources/ddl/first.xml`
| Change Set | Status | Rule       | Message           |
|------------|--------|------------|-------------------|
| *none*     | ERROR  | error-rule | Error message 1   |
| 2020010101 | ERROR  | error-rule | Error message 2   |
| 2020010102 | ERROR  | error-rule | Error message 3.1 |
|            |        | error-rule | Error message 3.2 |

# `src/main/resources/ddl/second.xml`
| Change Set | Status | Rule       | Message         |
|------------|--------|------------|-----------------|
| 2020010103 | ERROR  | error-rule | Error message 4<br>with newline |

# *Other*
| Change Set | Status | Rule       | Message       |
|------------|--------|------------|---------------|
| *none*     | ERROR  | error-rule | No change log |

# Summary
* ERROR: 6
* IGNORED: 3
* PASSED: 3
* DISABLED: 2
