package com.whiteclarkegroup.liquibaselinter.report;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class ConsoleReporter implements Reporter {

    static final String RESET = "\033[0m";
    static final String RED = "\033[0;31m";
    static final String YELLOW = "\033[0;33m";
    private static final String NEW_LINE = "\n";

    @Override
    public void processReport(Report report) {
        StringBuilder output = new StringBuilder();
        report.getByFileName().forEach((fileName, items) -> {
            printFileName(output, fileName);
            final Map<String, List<ReportItem>> groupedByChangeSet = items.stream().collect(groupingBy(ReportItem::getChangeSetId));
            printByChangeSet(output, groupedByChangeSet);
        });
        printToConsole(output);
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
                return coloured(RED, type.name());
            case IGNORED:
                return coloured(YELLOW, type.name());
            default:
                return type.name();
        }
    }

    private String coloured(String colour, String value) {
        return RESET + colour + value + RESET;
    }

}
