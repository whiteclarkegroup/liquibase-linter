package com.whiteclarkegroup.liquibaselinter.report;

import com.google.common.collect.ImmutableList;
import com.whiteclarkegroup.liquibaselinter.resolvers.AddColumnChangeParameterResolver;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AddColumnChangeParameterResolver.class)
class ConsoleReporterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ConsoleReporter consoleReporter;

    @BeforeEach
    void setUp() {
        consoleReporter = new ConsoleReporter();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldProduceReport(AddColumnChange addColumnChange) {
        addColumnChange.getChangeSet().setFilePath("src/main/resources/ddl/add-test-column.xml");
        final ReportItem error = ReportItem.error(null, addColumnChange, "test-rule", "Some rather long message about the error");
        final ReportItem ignored = ReportItem.ignored(null, addColumnChange, "another-rule", "Some other message about this rule");
        Collection<ReportItem> reportItems = ImmutableList.of(error, ignored);
        Report report = new Report();
        report.getReportItems().addAll(reportItems);
        consoleReporter.processReport(report);
        assertEquals("\n" +
            "src/main/resources/ddl/add-test-column.xml\n" +
            ConsoleReporter.RESET + ConsoleReporter.RED + "ERROR" + ConsoleReporter.RESET + "\n" +
            "\t'test-rule': Some rather long message about the error\n" +
            ConsoleReporter.RESET + ConsoleReporter.YELLOW + "IGNORED" + ConsoleReporter.RESET + "\n" +
            "\t'another-rule': Some other message about this rule\n\n", outContent.toString());
    }
}
