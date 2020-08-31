package com.whiteclarkegroup.liquibaselinter.report;

import com.google.auto.service.AutoService;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class TextReporter extends AbstractReporter {
    public static final String NAME = "text";

    public TextReporter(ReporterConfig config) {
        this(config, "txt");
    }

    protected TextReporter(ReporterConfig config, String defaultPathExtension) {
        super(config, defaultPathExtension);
    }

    @Override
    protected void printReport(PrintWriter output, Report report, List<ReportItem> items) {
        printByChangeLogFile(output, items);
        printSummary(output, report);
    }

    protected void printByChangeLogFile(PrintWriter output, List<ReportItem> items) {
        items.stream().collect(groupingBy(item -> ofNullable(item.getFilePath()).map(String::trim).orElse("")))
            .entrySet().stream().sorted(Map.Entry.comparingByKey(new EmptyLastComparator()))
            .forEach(entry -> printChangeLogFile(output, entry.getKey(), entry.getValue()));
    }

    protected void printChangeLogFile(PrintWriter output, String fileName, List<ReportItem> items) {
        printChangeLogHeader(output, fileName);
        printByChangeSet(output, items);
        output.println();
    }

    protected void printChangeLogHeader(PrintWriter output, String fileName) {
        if (isNullOrEmpty(fileName)) {
            output.println("Other");
        } else {
            output.println(fileName);
        }
    }

    protected void printByChangeSet(PrintWriter output, List<ReportItem> items) {
        items.stream().collect(groupingBy(item -> ofNullable(item.getChangeSetId()).map(String::trim).orElse("")))
            .entrySet().stream().sorted(Map.Entry.comparingByKey())
            .forEach(entry -> printChangeSet(output, entry.getKey(), entry.getValue()));
    }

    protected void printChangeSet(PrintWriter output, String changeSetId, List<ReportItem> items) {
        printChangeSetHeader(output, changeSetId);
        printByItemType(output, items);
    }

    protected void printChangeSetHeader(PrintWriter output, String changeSetId) {
        if (!changeSetId.isEmpty()) {
            output.append("changeSet '").append(changeSetId).append("'").println();
        }
    }

    protected void printByItemType(PrintWriter output, List<ReportItem> items) {
        items.stream().collect(groupingBy(ReportItem::getType))
            .entrySet().stream().sorted(Map.Entry.comparingByKey())
            .forEach(entry -> printItemType(output, entry.getKey(), entry.getValue()));
    }

    protected void printItemType(PrintWriter output, ReportItem.ReportItemType type, List<ReportItem> items) {
        printItemTypeHeader(output, type);
        items.forEach(item -> printItemDetail(output, item));
    }

    protected void printItemTypeHeader(PrintWriter output, ReportItem.ReportItemType type) {
        output.println(type.name());
    }

    protected void printItemDetail(PrintWriter output, ReportItem item) {
        output.append("\t'").append(item.getRule()).append("': ").println(indentMessage(item.getMessage()));
    }

    protected String indentMessage(String message) {
        return message.replace("\n", "\n\t\t");
    }

    protected void printSummary(PrintWriter output, Report report) {
        printSummaryHeader(output, report);
        printSummaryByItemType(output, report.getItems());
        printSummaryDisabledRules(output, report);
    }

    protected void printSummaryHeader(PrintWriter output, Report report) {
        output.println("Summary:");
    }

    protected void printSummaryByItemType(PrintWriter output, List<ReportItem> items) {
        for (ReportItem.ReportItemType type : ReportItem.ReportItemType.values()) {
            printItemTypeSummary(output, type, items.stream().filter(item -> item.getType() == type).collect(toList()));
        }
    }

    protected void printItemTypeSummary(PrintWriter output, ReportItem.ReportItemType type, List<ReportItem> items) {
        output.append('\t').append(type.name()).append(": ").println(items.size());
    }

    protected void printSummaryDisabledRules(PrintWriter output, Report report) {
        output.append("\tDISABLED: ").println(countDisabledRules(report));
    }

    protected static class EmptyLastComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (isNullOrEmpty(o1)) {
                return isNullOrEmpty(o2) ? 0 : 1;
            } else if (isNullOrEmpty(o2)) {
                return -1;
            }
            return o1.compareTo(o2);
        }
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory<TextReporter, ReporterConfig> {

        public Factory() {
            super(NAME);
        }
    }

}
