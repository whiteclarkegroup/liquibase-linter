package com.whiteclarkegroup.liquibaselinter.resolvers;

import liquibase.change.core.AddColumnChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class AddColumnChangeParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == AddColumnChange.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ChangeSet changeSet = new ChangeSet(new DatabaseChangeLog());
        final AddColumnChange addColumnChange = new AddColumnChange();
        addColumnChange.setChangeSet(changeSet);
        return addColumnChange;
    }

}
