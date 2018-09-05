package com.whiteclarkegroup.liquibaselinter.resolvers;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ChangeSetParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == ChangeSet.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        final ChangeSet changeSet = new ChangeSet(databaseChangeLog);
        databaseChangeLog.addChangeSet(changeSet);
        return changeSet;
    }

}
