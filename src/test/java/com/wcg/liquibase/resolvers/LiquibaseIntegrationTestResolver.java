package com.wcg.liquibase.resolvers;

import com.google.common.collect.ImmutableSet;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static com.wcg.liquibase.config.ConfigLoader.LQLLINT_CONFIG;

public class LiquibaseIntegrationTestResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Liquibase.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            Optional<Method> method = extensionContext.getTestMethod();
            if (method.isPresent()) {
                LiquibaseIntegrationTest testConfig = method.get().getAnnotation(LiquibaseIntegrationTest.class);
                DatabaseConnection conn = new OfflineConnection("offline:h2");
                ResourceAccessor resourceAccessor = new ConfigAwareFileSystemResourceAccessor("/integration/" + testConfig.configFile());
                return new Liquibase("src/test/resources/integration/" + testConfig.changeLogFile(), resourceAccessor, conn);
            } else {
                throw new ParameterResolutionException("Failed to create liquibase parameter");
            }

        } catch (LiquibaseException e) {
            throw new ParameterResolutionException("Failed to create liquibase parameter");
        }
    }

    public static class ConfigAwareFileSystemResourceAccessor extends FileSystemResourceAccessor {

        private final String configPath;

        private ConfigAwareFileSystemResourceAccessor(String configPath) {
            this.configPath = configPath;
        }

        @Override
        public Set<InputStream> getResourcesAsStream(String path) throws IOException {
            if (LQLLINT_CONFIG.equals(path)) {
                return ImmutableSet.of(getClass().getResourceAsStream(configPath));
            }
            return super.getResourcesAsStream(path);
        }

    }

}


