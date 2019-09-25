package com.whiteclarkegroup.liquibaselinter.report.console;

import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleReporter implements Reporter {

    private static final String NEW_LINE = "\n";

    @Override
    public void processReport(Report report) {
        installAnsi();
        StringBuilder output = new StringBuilder();
        report.getByFileName().forEach((fileName, items) -> {
            printFileName(output, fileName);
            final Map<String, List<ReportItem>> groupedByChangeSet = items.stream().collect(groupingBy(ReportItem::getChangeSetId));
            printByChangeSet(output, groupedByChangeSet);
        });
        printToConsole(output);
        uninstallAnsi();
    }

    private void printByChangeSet(StringBuilder output, Map<String, List<ReportItem>> groupedByChangeSet) {
        groupedByChangeSet.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                printChangeSetLine(output, entry.getKey());
                final Map<ReportItem.ReportItemType, List<ReportItem>> groupedByType = entry.getValue().stream().collect(groupingBy(ReportItem::getType));
                printRulesByType(output, groupedByType);
            });
    }

    private void printRulesByType(StringBuilder output, final Map<ReportItem.ReportItemType, List<ReportItem>> groupedByType) {
        groupedByType.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                printTypeLine(output, entry.getKey());
                entry.getValue().forEach(itemByType -> printRuleLine(output, itemByType));
            });
    }

    private void printFileName(StringBuilder output, String fileName) {
        output.append(NEW_LINE).append(fileName).append(NEW_LINE);
    }

    private void printToConsole(StringBuilder output) {
        System.out.println(output.toString());
    }

    private void printChangeSetLine(StringBuilder output, String changeSetId) {
        if (!changeSetId.isEmpty()) {
            output.append("changeSet '").append(changeSetId).append("'");
            output.append(NEW_LINE);
        }
    }

    private void printTypeLine(StringBuilder output, ReportItem.ReportItemType type) {
        output.append(getType(type));
        output.append(NEW_LINE);
    }

    private void printRuleLine(StringBuilder output, ReportItem item) {
        output.append("\t'").append(item.getRule()).append("': ").append(item.getMessage()).append(NEW_LINE);
    }

    private String getType(ReportItem.ReportItemType type) {
        switch (type) {
            case ERROR:
                return coloured(Ansi.Color.RED, type.name());
            case IGNORED:
                return coloured(Ansi.Color.YELLOW, type.name());
            default:
                return type.name();
        }
    }

    private String coloured(Ansi.Color colour, String value) {
        return ansi().reset().fg(colour).a(value).reset().toString();
    }

    protected void installAnsi() {
        AnsiConsole.systemInstall();
    }

    protected void uninstallAnsi() {
        AnsiConsole.systemInstall();
    }

}
