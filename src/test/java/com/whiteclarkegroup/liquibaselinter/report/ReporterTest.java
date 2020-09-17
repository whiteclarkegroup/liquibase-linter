package com.whiteclarkegroup.liquibaselinter.report;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.fusesource.jansi.HtmlAnsiOutputStream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.PASSED;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.SKIPPED;
import static org.assertj.core.api.Assertions.*;
import static org.fusesource.jansi.Ansi.ansi;

class ReporterTest {

    private static final Map<String, Supplier<? extends AbstractReporter.Builder>> REPORT_TYPES = ImmutableMap.of(
        ConsoleReporter.NAME, () -> new TestConsoleReporerBuilder(),
        TextReporter.NAME, () -> new TextReporter.Builder(),
        MarkdownReporter.NAME, () -> new MarkdownReporter.Builder());

    final List<ReportingTestConfig> tests = new ArrayList<>();

    @TestFactory
    Stream<DynamicTest> reporterTests() {
        REPORT_TYPES.forEach((reportType, builder) -> {
            addDefaultFilters(reportType, builder.get());
            addLimitedFilters(reportType, builder.get());
            addFullFilters(reportType, builder.get());
            addEmptyReport(reportType, builder.get());
        });

        ThrowingConsumer<ReportingTestConfig> testExecutor = running -> {
            String output;

            if (running.reporter instanceof ConsoleReporter) {
                output = runConsoleTest(running);
            } else {
                output = runFileReport(running);
            }
            assertThat(output).isEqualTo(running.getExpectedOutput());
        };
        return DynamicTest.stream(tests.iterator(), ReportingTestConfig::getDisplayName, testExecutor);
    }

    private String runConsoleTest(ReportingTestConfig running) {
        String output;
        PrintStream originalOut = System.out;
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(new HtmlAnsiOutputStream(outContent));
            System.setOut(out);
            running.reporter.process(running.report);
            output = outContent.toString();
        } finally {
            System.setOut(originalOut);
        }
        return output;
    }

    private String runFileReport(ReportingTestConfig running) throws IOException {
        String output;
        running.reporter.process(running.report);
        output = Files.asCharSource(new File(running.outputPath), StandardCharsets.UTF_8).read();
        return output;
    }

    private void addDefaultFilters(String reportType, AbstractReporter.Builder builder) {
        String suffix = suffix(builder.getPath());
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "defaultFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "defaultFilters." + suffix));
    }

    private void addLimitedFilters(String reportType, AbstractReporter.Builder builder) {
        builder.withFilter(ReportItem.ReportItemType.ERROR);
        String suffix = suffix(builder.getPath());
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "limitedFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "limitedFilters." + suffix));
    }

    private void addFullFilters(String reportType, AbstractReporter.Builder builder) {
        builder.withFilter(ReportItem.ReportItemType.values());
        String suffix = suffix(builder.getPath());
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "fullFilters." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildFullReport(),
            builder.getPath(), "fullFilters." + suffix));
    }

    private void addEmptyReport(String reportType, AbstractReporter.Builder builder) {
        String suffix = suffix(builder.getPath());
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildEmptyReport(),
            builder.getPath(), "emptyReport." + suffix));
        builder.withPath("target/lqlint-custom." + suffix);
        tests.add(new ReportingTestConfig(reportType, builder.build(), buildEmptyReport(),
            builder.getPath(), "emptyReport." + suffix));
    }

    private static String suffix(String path) {
        return Optional.ofNullable(path).map(Files::getFileExtension).orElse("out");
    }

    private Report buildFullReport() {
        Collection<ReportItem> items = new ArrayList<>();

        items.add(new ReportItem(null, null, "error-rule", ERROR, "No change log"));

        String dbChangeLog1 = "src/main/resources/ddl/first.xml";
        items.add(new ReportItem(dbChangeLog1, null, "skipped-rule", SKIPPED, "Skipped message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "error-rule", ERROR, "Error message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "ignored-rule", IGNORED, "Ignored message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "passed-rule", PASSED, "Passed message 1"));

        String changeSet1 = "2020010101";
        items.add(new ReportItem(dbChangeLog1, changeSet1, "skipped-rule", SKIPPED, "Skipped message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "error-rule", ERROR, "Error message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "ignored-rule", IGNORED, "Ignored message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "passed-rule", PASSED, "Passed message 2"));

        String changeSet2 = "2020010102";
        items.add(new ReportItem(dbChangeLog1, changeSet2, "skipped-rule", SKIPPED, "Skipped message 3"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.1"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.2"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "ignored-rule", IGNORED, "Ignored message 3"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "passed-rule", PASSED, "Passed message 3"));

        String dbChangeLog2 = "src/main/resources/ddl/second.xml";
        String changeSet3 = "2020010103";
        items.add(new ReportItem(dbChangeLog2, changeSet3, "error-rule", ERROR, "Error message 4\nwith newline"));

        Report report = new Report();
        report.getItems().addAll(items);
        report.setDisabledRuleCount(2);
        return report;
    }

    private Report buildEmptyReport() {
        return new Report();
    }

    private static class ReportingTestConfig {
        final String reportType;
        final Reporter reporter;
        final Report report;
        final String outputPath;
        final String expectedResourceName;

        private ReportingTestConfig(String reportType, Reporter reporter, Report report, String outputPath, String expectedResourceName) {
            this.reportType = reportType;
            this.reporter = reporter;
            this.report = report;
            this.outputPath = outputPath;
            this.expectedResourceName = expectedResourceName;
        }

        public String getDisplayName() {
            return "Should produce " + Optional.ofNullable(outputPath).orElse("output") + " for " + reportType + " matching " + expectedResourceName;
        }

        public String getExpectedOutput() throws IOException {
            final URL expectedOutputUrl = Resources.getResource("reports/" + expectedResourceName);
            return Resources.toString(expectedOutputUrl, StandardCharsets.UTF_8);
        }
    }

    private static class TestConsoleReporerBuilder extends ConsoleReporter.Builder {
        @Override
        public ConsoleReporter build() {
            return new ConsoleReporter(this) {
                @Override
                protected void installAnsi() {
                    // ansi is difficult to test with so noop installing makes it easier
                }

                @Override
                protected void uninstallAnsi() {
                    // ansi is difficult to test with so noop uninstalling makes it easier
                }
            };
        }
    }
}
