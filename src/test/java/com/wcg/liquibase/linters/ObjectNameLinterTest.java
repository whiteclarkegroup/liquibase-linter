package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.AddColumnChangeParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AddColumnChangeParameterResolver.class, DefaultConfigParameterResolver.class})
class ObjectNameLinterTest {

    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = new ObjectNameLinter();
    }

    @DisplayName("Should only allow uppercase with _ separator")
    @Test
    void should_only_allow_uppercase_with_underscore_separator(AddColumnChange addColumnChange, Config config) throws ChangeLogParseException {
        final List<String> invalidNames = Arrays.asList("_TEST", "TEST_", "TE ST", "TeST");
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, config.getRules()));
            assertTrue(changeLogParseException.getMessage().contains("Object name '" + invalidName + "' name must be uppercase and use '_' separation"));
        }

        objectNameLinter.lintObjectName("VALID_NAME", addColumnChange, config.getRules());
    }

    @DisplayName("Should only allow names under max length")
    @Test
    void should_only_allow_names_under_max_length(AddColumnChange addColumnChange, Config config) throws ChangeLogParseException {
        String tooLong = "TEST_TEST_TEST_TEST_TEST_TEST_TEST";
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(tooLong, addColumnChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Object name '" + tooLong + "' must be less than 30 characters"));

        String notTooLong = "TEST_TEST_TEST_TEST_TEST_TEST";
        objectNameLinter.lintObjectName(notTooLong, addColumnChange, config.getRules());
    }

    @DisplayName("Should catch when name is null, when trying to lint length")
    @Test
    void should_catch_null_names_when_checking_length(AddColumnChange addColumnChange, Config config) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectNameLength(null, addColumnChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Object name is null"));
    }

    @DisplayName("Should allow uppercase with, numbers and _ separator")
    @Test
    void should_only_allow_uppercase_with_numbers_and_underscore_separator(AddColumnChange addColumnChange, Config config) throws ChangeLogParseException {
        final List<String> invalidNames = Arrays.asList("_TEST", "TEST_", "TE ST", "TeST");
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, config.getRules()));
            assertTrue(changeLogParseException.getMessage().contains("Object name '" + invalidName + "' name must be uppercase and use '_' separation"));
        }
        objectNameLinter.lintObjectName("VALID_12_3NAME_99", addColumnChange, config.getRules());
    }

    @DisplayName("Should allow uppercase with, numbers and _ separator")
    @Test
    void should_not_allow_null_object_name(AddColumnChange addColumnChange, Config config) {
        final List<String> invalidNames = Collections.singletonList(null);
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, config.getRules()));
            assertTrue(changeLogParseException.getMessage().contains("Object name is null"));
        }
    }

}
