package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.MergeColumnChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class MergeColumnChangeLinterTest {

    private MergeColumnChangeLinter mergeColumnChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        mergeColumnChangeLinter = new MergeColumnChangeLinter() {
            public ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        MergeColumnChange mergeColumnChange = new MergeColumnChange();
        mergeColumnChange.setChangeSet(changeSet);
        mergeColumnChange.setFinalColumnName("TEST_TEST");
        changeSet.addChange(mergeColumnChange);
        mergeColumnChangeLinter.lint(mergeColumnChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectName("TEST_TEST", mergeColumnChange, ruleRunner);
    }
}
