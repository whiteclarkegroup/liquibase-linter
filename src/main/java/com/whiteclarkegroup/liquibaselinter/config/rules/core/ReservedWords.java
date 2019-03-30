package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import java.util.Arrays;
import java.util.List;

class ReservedWords {
    private ReservedWords() {
    }

    static final List<String> SQL_SERVER = Arrays.asList(
        "SESSION_USER"
    );
}
