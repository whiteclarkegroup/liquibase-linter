package com.whiteclarkegroup.liquibaselinter.report;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import org.fusesource.jansi.HtmlAnsiOutputStream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.PASSED;
import static org.assertj.core.api.Assertions.assertThat;

class ReporterTest {

    private static final Map<String, String> REPORT_TYPES = ImmutableMap.of(
        ConsoleReporter.NAME, "out",
        TextReporter.NAME, "txt",
        MarkdownReporter.NAME, "md"
    );

    final List<ReportingTestConfig> tests = new ArrayList<>();

    @TestFactory
    Stream<DynamicTest> reporterTests() {
        REPORT_TYPES.forEach((reportType, suffix) -> {
            addDefaultFilters(reportType, suffix);
            addLimitedFilters(reportType, suffix);
            addFullFilters(reportType, suffix);
            addEmptyReport(reportType, suffix);
        });

        ThrowingConsumer<ReportingTestConfig> testExecutor = running -> {
            String output;

            if (running.reportType.equalsIgnoreCase(ConsoleReporter.NAME)) {
                output = runConsoleTest(running);
            } else {
                output = runFileReport(running);
            }
            assertThat(output).isEqualTo(running.getExpectedOutput());
        };
        return DynamicTest.stream(tests.iterator(), ReportingTestConfig::getDisplayName, testExecutor);
    }

    private String runConsoleTest(ReportingTestConfig running) {
        PrintStream originalOut = System.out;
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(new HtmlAnsiOutputStream(outContent));
            System.setOut(out);
            new TestConsoleReporter(running.config).processReport(running.report);
            return outContent.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    private String runFileReport(ReportingTestConfig running) throws IOException {
        for (Reporter.Factory factory : ServiceLoader.load(Reporter.Factory.class)) {
            if (factory.supports(running.reportType)) {
                factory.create(running.config).processReport(running.report);
            }
        }
        String output;
        output = Files.asCharSource(new File(running.outputPath), StandardCharsets.UTF_8).read();
        return output;
    }

    private void addDefaultFilters(String reportType, String suffix) {
        ReporterConfig.Builder builder = new ReporterConfig.Builder();
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-report." + suffix, "defaultFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-custom." + suffix, "defaultFilters." + suffix));
    }

    private void addLimitedFilters(String reportType, String suffix) {
        ReporterConfig.Builder builder = new ReporterConfig.Builder();
        builder.withFilter(ERROR);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-report." + suffix, "limitedFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-custom." + suffix, "limitedFilters." + suffix));
    }

    private void addFullFilters(String reportType, String suffix) {
        ReporterConfig.Builder builder = new ReporterConfig.Builder();
        builder.withFilter(ReportItem.ReportItemType.values());
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-report." + suffix, "fullFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            "target/lqlint-custom." + suffix, "fullFilters." + suffix));
    }

    private void addEmptyReport(String reportType, String suffix) {
        ReporterConfig.Builder builder = new ReporterConfig.Builder();
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildEmptyReport(),
            "target/lqlint-report." + suffix, "emptyReport." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildEmptyReport(),
            "target/lqlint-custom." + suffix, "emptyReport." + suffix));
    }

    private Report buildFullReport() {
        List<ReportItem> items = new ArrayList<>();

        items.add(new ReportItem(null, null, "error-rule", ERROR, "No change log"));

        String dbChangeLog1 = "src/main/resources/ddl/first.xml";
        items.add(new ReportItem(dbChangeLog1, null, "error-rule", ERROR, "Error message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "ignored-rule", IGNORED, "Ignored message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "passed-rule", PASSED, "Passed message 1"));

        String changeSet1 = "2020010101";
        items.add(new ReportItem(dbChangeLog1, changeSet1, "error-rule", ERROR, "Error message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "ignored-rule", IGNORED, "Ignored message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "passed-rule", PASSED, "Passed message 2"));

        String changeSet2 = "2020010102";
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.1"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.2"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "ignored-rule", IGNORED, "Ignored message 3"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "passed-rule", PASSED, "Passed message 3"));

        String dbChangeLog2 = "src/main/resources/ddl/second.xml";
        String changeSet3 = "2020010103";
        items.add(new ReportItem(dbChangeLog2, changeSet3, "error-rule", ERROR, "Error message 4\nwith newline"));

        Config config = new Config.Builder().withRules(ImmutableListMultimap.<String, RuleConfig>builder()
            .put("a", RuleConfig.builder().withEnabled(false).build())
            .put("a", RuleConfig.builder().withEnabled(true).build())
            .put("b", RuleConfig.builder().withEnabled(false).build())
            .build()
        ).build();

        return new Report(config, items);
    }

    private Report buildEmptyReport() {
        return new Report(new Config.Builder().withRules(ImmutableListMultimap.of()).build(), ImmutableList.of());
    }

    private static class ReportingTestConfig {
        final String reportType;
        final ReporterConfig config;
        final Report report;
        final String outputPath;
        final String expectedResourceName;

        private ReportingTestConfig(String reportType, ReporterConfig config, Report report, String outputPath, String expectedResourceName) {
            this.reportType = reportType;
            this.config = config;
            this.report = report;
            this.outputPath = outputPath;
            this.expectedResourceName = expectedResourceName;
        }

        public String getDisplayName() {
            return "Should produce " + outputPath + " for " + reportType + " matching " + expectedResourceName;
        }

        public String getExpectedOutput() throws IOException {
            final URL expectedOutputUrl = Resources.getResource("reports/" + expectedResourceName);
            return Resources.toString(expectedOutputUrl, StandardCharsets.UTF_8);
        }
    }

    private static class TestConsoleReporter extends ConsoleReporter {

        TestConsoleReporter(ReporterConfig config) {
            super(config);
        }

        @Override
        protected void installAnsi() {
            // ansi is difficult to test with so noop installing makes it easier
        }

        @Override
        protected void uninstallAnsi() {
            // ansi is difficult to test with so noop uninstalling makes it easier
        }
    }
}
