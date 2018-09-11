package com.whiteclarkegroup.liquibaselinter.report;

public class ConsoleReporter implements Reporter {

    static final String RESET = "\033[0m";
    static final String RED = "\033[0;31m";
    static final String YELLOW = "\033[0;33m";
    private static final String NEW_LINE = "\n";

    @Override
    public void processReport(Report report) {
        StringBuilder output = new StringBuilder();
        report.getByFileName().forEach((key, value) -> {
            output.append(NEW_LINE).append(key).append(NEW_LINE);
            value.forEach(item -> {
                printTypeChangeSetLine(output, item);
                printRuleLine(output, item);
            });
        });
        printToConsole(output);
    }

    private void printToConsole(StringBuilder output) {
        System.out.println(output.toString());
    }

    private void printTypeChangeSetLine(StringBuilder output, ReportItem item) {
        output.append(getType(item));
        if (item.getChangeSetId() != null) {
            output.append(" changeSet '").append(item.getChangeSetId()).append("'");
        }
        output.append(NEW_LINE);
    }

    private void printRuleLine(StringBuilder output, ReportItem item) {
        output.append("\t'").append(item.getRule()).append("': ").append(item.getMessage()).append(NEW_LINE);
    }

    private String getType(ReportItem item) {
        switch (item.getType()) {
            case ERROR:
                return RESET + RED + item.getType().name() + RESET;
            case IGNORED:
                return RESET + YELLOW + item.getType().name() + RESET;
            default:
                return item.getType().name();
        }
    }

}
