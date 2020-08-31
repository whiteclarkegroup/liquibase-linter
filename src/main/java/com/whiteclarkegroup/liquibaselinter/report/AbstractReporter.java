package com.whiteclarkegroup.liquibaselinter.report;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableSet;
import com.whiteclarkegroup.liquibaselinter.report.ReporterConfig.Builder;
import liquibase.exception.UnexpectedLiquibaseException;
import org.springframework.core.GenericTypeResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;

public abstract class AbstractReporter implements Reporter {
    private final ReporterConfig config;
    private final String path;
    private final Set<ReportItem.ReportItemType> filter;

    protected AbstractReporter(ReporterConfig config, String defaultPathExtension) {
        this.config = config;
        this.path = Optional.ofNullable(config.getPath()).orElse("./target/lqlint-report." + defaultPathExtension);
        this.filter = Optional.ofNullable(config.getFilter()).map(ImmutableSet::copyOf).orElse(ImmutableSet.of(ERROR, IGNORED));
    }

    @Override
    public ReporterConfig getConfiguration() {
        return config;
    }

    @Override
    public void processReport(Report report) {
        final List<ReportItem> filteredItems = report.getItems().stream()
            .filter(item -> filter.contains(item.getType()))
            .collect(Collectors.toList());
        process(report, filteredItems);
    }

    protected void process(Report report, List<ReportItem> items) {
        try (FileWriter writer = new FileWriter(path)) {
            printReport(new PrintWriter(writer), report, items);
            writer.flush();
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Unable to write text report", e);
        }
    }

    protected abstract void printReport(PrintWriter output, Report report, List<ReportItem> items);

    protected static long countDisabledRules(Report report) {
        return report.getConfig().getRules().values().stream().filter(rule -> !rule.isEnabled()).count();
    }

    public abstract static class Factory<R extends Reporter, C extends ReporterConfig> implements Reporter.Factory<R, C> {
        private final String name;
        private final Class<? extends R> reporterClass;
        private final Class<? extends C> configClass;

        protected Factory(final String name) {
            this.name = name;
            final Class<?>[] factoryTypes = GenericTypeResolver.resolveTypeArguments(getClass(), Reporter.Factory.class);
            this.reporterClass = (Class<? extends R>) factoryTypes[0];
            this.configClass = (Class<? extends C>) factoryTypes[1];
        }

        @Override
        public boolean supports(String name) {
            return this.name.equals(name);
        }

        @Override
        public Class<? extends C> getConfigClass() {
            return configClass;
        }

        @Override
        public R create(C config) {
            return createReporter(config);
        }

        @Override
        public R create(boolean enabled) {
            return createReporter(newConfigBuilder().withEnabled(enabled).build());
        }

        @Override
        public R create(String path) {
            return createReporter(newConfigBuilder().withPath(path).build());
        }

        protected R createReporter(ReporterConfig config) {
            try {
                return reporterClass.getConstructor(configClass).newInstance(config);
            } catch (ReflectiveOperationException e) {
                throw new UnexpectedLiquibaseException("Cannot instantiate reporter of type " + reporterClass, e);
            }
        }

        private Builder<? extends Builder> newConfigBuilder() {
            Class<?> builderClass = Optional.ofNullable(configClass.getAnnotation(JsonDeserialize.class))
                .map(json -> json.builder())
                .orElse(null);
            if (builderClass != null) {
                try {
                    return (Builder<? extends Builder>) builderClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new UnexpectedLiquibaseException("Cannot instantiate builder for " + configClass, e);
                }
            }
            throw new UnexpectedLiquibaseException("Cannot find builder for " + configClass);
        }
    }
}
