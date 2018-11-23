package com.whiteclarkegroup.liquibaselinter.report;

import com.google.common.collect.ImmutableList;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import static org.fusesource.jansi.Ansi.ansi;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsoleReporterTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private ConsoleReporter consoleReporter;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        System.setOut(out);
        consoleReporter = new ConsoleReporter() {
            @Override
            protected void installAnsi() {

            }

            @Override
            protected void uninstallAnsi() {
            }
        };
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.out.println(outContent.toString());
    }

    @DisplayName("Should produce report without change set id if not present")
    @Test
    void shouldProduceReportWithoutChangeSet() {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        when(databaseChangeLog.getFilePath()).thenReturn("src/main/resources/ddl/add-test-column.xml");
        final ReportItem error = ReportItem.error(databaseChangeLog, null, "test-rule", "Some rather long message about the error");
        final ReportItem ignored = ReportItem.ignored(databaseChangeLog, null, "another-rule", "Some other message about this rule");
        Collection<ReportItem> reportItems = ImmutableList.of(error, ignored);
        Report report = new Report();
        report.getReportItems().addAll(reportItems);
        consoleReporter.processReport(report);
        String output = outContent.toString();
        System.out.println(output);
        assertTrue(output.contains("src/main/resources/ddl/add-test-column.xml\n" +
            ansi().reset().fg(Ansi.Color.RED).a("ERROR").reset().toString() + "\n" +
            "\t'test-rule': Some rather long message about the error\n" +
            ansi().reset().fg(Ansi.Color.YELLOW).a("IGNORED").reset().toString() + "\n" +
            "\t'another-rule': Some other message about this rule"));
    }

    @DisplayName("Should produce report with change set id if present, grouping by change set then type")
    @Test
    void shouldProduceReportWithChangeSetId() {
        ChangeSet changeSet1 = mockChangeSet("2018010101");
        final ReportItem error1 = ReportItem.error(null, changeSet1, "test-rule", "Some rather long message about the error");
        final ReportItem ignored1 = ReportItem.ignored(null, changeSet1, "another-rule", "Some other message about this rule");

        ChangeSet changeSet2 = mockChangeSet("2018010199");
        final ReportItem error2 = ReportItem.error(null, changeSet2, "test-rule", "Some rather long message about the error");
        final ReportItem error3 = ReportItem.error(null, changeSet2, "test-rule", "Some rather long message about the error 3");
        final ReportItem ignored2 = ReportItem.ignored(null, changeSet2, "another-rule", "Some other message about this rule");

        Collection<ReportItem> reportItems = ImmutableList.of(error1, ignored1, error2, ignored2, error3);
        Report report = new Report();
        report.getReportItems().addAll(reportItems);
        consoleReporter.processReport(report);

        assertTrue(outContent.toString().contains("src/main/resources/ddl/add-test-column.xml\n" +
            "changeSet '2018010101'\n" +
            ansi().reset().fg(Ansi.Color.RED).a("ERROR").reset().toString() + "\n" +
            "\t'test-rule': Some rather long message about the error\n" +
            ansi().reset().fg(Ansi.Color.YELLOW).a("IGNORED").reset().toString() + "\n" +
            "\t'another-rule': Some other message about this rule\n" +
            "changeSet '2018010199'\n" +
            ansi().reset().fg(Ansi.Color.RED).a("ERROR").reset().toString() + "\n" +
            "\t'test-rule': Some rather long message about the error\n" +
            "\t'test-rule': Some rather long message about the error 3\n" +
            ansi().reset().fg(Ansi.Color.YELLOW).a("IGNORED").reset().toString() + "\n" +
            "\t'another-rule': Some other message about this rule\n"));

    }

    private ChangeSet mockChangeSet(String changeSetId) {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getFilePath()).thenReturn("src/main/resources/ddl/add-test-column.xml");
        when(changeSet.getId()).thenReturn(changeSetId);
        return changeSet;
    }
}
