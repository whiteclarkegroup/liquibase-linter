package com.whiteclarkegroup.liquibaselinter.report;

import com.google.auto.service.AutoService;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintWriter;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleReporter extends TextReporter {
    public static final String NAME = "console";

    public ConsoleReporter(ReporterConfig config) {
        super(config);
    }

    @Override
    protected void process(Report report, List<ReportItem> items) {
        installAnsi();
        PrintWriter writer = new PrintWriter(System.out);
        printReport(writer, report, items);
        writer.flush();
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
        long disabled = countDisabledRules(report);

        output.append('\t');
        if (disabled > 0) {
            output.print(ansi().reset().fg(Ansi.Color.MAGENTA).a("DISABLED").reset().toString());
        } else {
            // don't draw attention with color when there are no report items
            output.print("DISABLED");
        }
        output.append(": ").println(disabled);
    }

    protected void installAnsi() {
        AnsiConsole.systemInstall();
    }

    protected void uninstallAnsi() {
        AnsiConsole.systemInstall();
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory<ConsoleReporter, ReporterConfig> {

        public Factory() {
            super(NAME);
        }
    }
}
