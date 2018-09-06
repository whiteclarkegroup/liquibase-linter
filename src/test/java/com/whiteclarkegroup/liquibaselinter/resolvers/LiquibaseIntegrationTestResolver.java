package com.whiteclarkegroup.liquibaselinter.resolvers;

import com.google.common.collect.ImmutableSet;
import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
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

public class LiquibaseIntegrationTestResolver implements ParameterResolver {

    public static Liquibase buildLiquibase(String changeLogFile, String configFile) throws LiquibaseException {
        ResourceAccessor resourceAccessor = new LiquibaseIntegrationTestResolver.ConfigAwareFileSystemResourceAccessor("/integration/" + configFile);
        DatabaseConnection conn = new OfflineConnection("offline:h2", resourceAccessor);
        return new Liquibase("src/test/resources/integration/" + changeLogFile, resourceAccessor, conn);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Liquibase.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            Optional<Method> method = extensionContext.getTestMethod();
            if (method.isPresent()) {
                LiquibaseLinterIntegrationTest testConfig = method.get().getAnnotation(LiquibaseLinterIntegrationTest.class);
                return buildLiquibase(testConfig.changeLogFile(), testConfig.configFile());
            } else {
                throw new ParameterResolutionException("Failed to create liquibase parameter");
            }
        } catch (LiquibaseException e) {
            throw new ParameterResolutionException("Failed to create liquibase parameter");
        }
    }

    private static class ConfigAwareFileSystemResourceAccessor extends FileSystemResourceAccessor {

        private final String configPath;

        private ConfigAwareFileSystemResourceAccessor(String configPath) {
            this.configPath = configPath;
        }

        @Override
        public Set<InputStream> getResourcesAsStream(String path) throws IOException {
            if (ConfigLoader.LQLLINT_CONFIG.equals(path)) {
                return ImmutableSet.of(getClass().getResourceAsStream(configPath));
            }
            return super.getResourcesAsStream(path);
        }

    }

}


