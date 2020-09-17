package com.whiteclarkegroup.liquibaselinter.report;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.Console;
import java.io.PrintWriter;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

@JsonDeserialize(builder = ConsoleReporter.Builder.class)
public class ConsoleReporter extends TextReporter {
    public static final String NAME = "console";

    protected ConsoleReporter(Reporter.Config config) {
        super(config);
    }

    @Override
    protected void process(Report report, List<ReportItem> items) {
        installAnsi();
        printReport(new PrintWriter(System.out), report, items);
        uninstallAnsi();
    }

    @Override
    protected void printItemTypeHeader(PrintWriter output, ReportItem.ReportItemType type) {
        printItemTypeName(output, type);
        output.println();
    }

    @Override
    protected void printItemTypeSummary(PrintWriter output, ReportItem.ReportItemType type, List<ReportItem> items) {
        output.append('\t');
        if (items.isEmpty()) {
            // don't draw attention with color when there are no report items
            output.print(type.name());
        } else {
            printItemTypeName(output, type);
        }
        output.append(": ").println(items.size());
    }

    private void printItemTypeName(PrintWriter output, ReportItem.ReportItemType type) {
        switch (type) {
            case ERROR:
                printColoured(output, Ansi.Color.RED, type.name());
                break;
            case IGNORED:
                printColoured(output, Ansi.Color.YELLOW, type.name());
                break;
            case SKIPPED:
                printColoured(output, Ansi.Color.CYAN, type.name());
                break;
            case PASSED:
                printColoured(output, Ansi.Color.GREEN, type.name());
                break;
            default:
                super.printItemTypeHeader(output, type);
                break;
        }
    }

    private void printColoured(PrintWriter output, Ansi.Color colour, String line) {
        output.print(ansi().reset().fg(colour).a(line).reset().toString());
    }

    @Override
    protected void printSummaryDisabledRules(PrintWriter output, Report report) {
        output.append('\t');
        if (report.getDisabledRuleCount() > 0) {
            output.print(ansi().reset().fg(Ansi.Color.MAGENTA).a("DISABLED").reset().toString());
        } else {
            // don't draw attention with color when there are no report items
            output.print("DISABLED");
        }
        output.append(": ").println(report.getDisabledRuleCount());
    }

    protected void installAnsi() {
        AnsiConsole.systemInstall();
    }

    protected void uninstallAnsi() {
        AnsiConsole.systemInstall();
    }

    public static class Builder extends AbstractReporter.Builder<Builder> {

        @Override
        public ConsoleReporter build() {
            return new ConsoleReporter(this);
        }
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory {

        public Factory() {
            super(NAME, ConsoleReporter.class, new Builder());
        }
    }
}
