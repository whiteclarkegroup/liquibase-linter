package com.whiteclarkegroup.liquibaselinter.resolvers;

import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.exception.UnexpectedLiquibaseException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;

public class DefaultConfigParameterResolver implements ParameterResolver {

    private final Config config;

    public DefaultConfigParameterResolver() {
        try (InputStream inputStream = getClass().getResourceAsStream("/lqllint.test.json")) {
            this.config = Config.fromInputStream(inputStream);
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load test lq lint default config file", e);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Config.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return config;
    }

}
