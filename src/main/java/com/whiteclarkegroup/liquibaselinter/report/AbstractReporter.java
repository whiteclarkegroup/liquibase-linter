package com.whiteclarkegroup.liquibaselinter.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import liquibase.exception.UnexpectedLiquibaseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;

public abstract class AbstractReporter implements Reporter {
    protected final boolean enabled;
    protected final String path;
    protected final Set<ReportItem.ReportItemType> filter;

    protected AbstractReporter(Reporter.Config config) {
        this.enabled = config.isEnabled();
        this.path = config.getPath();
        this.filter = Optional.ofNullable(config.getFilter()).map(ImmutableSet::copyOf).orElse(ImmutableSet.of(ERROR, IGNORED));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void process(Report report) {
        final List<ReportItem> filteredItems = report.getItems().stream()
            .filter(item -> filter.contains(item.getType()))
            .collect(Collectors.toList());
        process(report, filteredItems);
    }

    protected void process(Report report, List<ReportItem> items) {
        try (FileWriter writer = new FileWriter(path)) {
            printReport(new PrintWriter(writer), report, items);
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Unable to write text report", e);
        }
    }

    protected abstract void printReport(PrintWriter output, Report report, List<ReportItem> items);

    public abstract static class Builder<B extends Builder<B>> implements Reporter.Config {
        protected boolean enabled = true;
        protected Set<ReportItem.ReportItemType> filter = new LinkedHashSet<>(Arrays.asList(ERROR, IGNORED));
        protected String path;

        public B withEnabled(boolean enabled) {
            this.enabled = enabled;
            return (B) this;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public B withFilter(ReportItem.ReportItemType... filter) {
            this.filter = ImmutableSet.copyOf(filter);
            return (B) this;
        }

        @Override
        public Set<ReportItem.ReportItemType> getFilter() {
            return filter;
        }

        public B withPath(String path) {
            this.path = path;
            return (B) this;
        }

        @Override
        public String getPath() {
            return path;
        }

        public abstract Reporter build();
    }

    public abstract static class Factory implements Reporter.Factory {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final String name;
        private final Class<? extends Reporter> reporterClass;
        private final Builder builder;

        protected Factory(final String name, Class<? extends Reporter> reporterClass, final Builder builder) {
            this.name = name;
            this.reporterClass = reporterClass;
            this.builder = builder;
        }

        @Override
        public boolean supports(String name) {
            return this.name.equals(name);
        }

        @Override
        public Reporter create(Object config) {
            return OBJECT_MAPPER.convertValue(config, reporterClass);
        }

        @Override
        public Reporter create(boolean enabled) {
            return builder.withEnabled(enabled).build();
        }

        @Override
        public Reporter create(String path) {
            return builder.withPath(path).build();
        }
    }
}
