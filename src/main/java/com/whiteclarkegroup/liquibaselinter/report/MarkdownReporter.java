package com.whiteclarkegroup.liquibaselinter.report;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.service.AutoService;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static liquibase.util.StringUtils.isEmpty;
import static liquibase.util.StringUtils.pad;
import static liquibase.util.StringUtils.repeat;
import static liquibase.util.StringUtils.trimToEmpty;
import static java.lang.Math.max;

@JsonDeserialize(builder = MarkdownReporter.Builder.class)
public class MarkdownReporter extends TextReporter {
    public static final String NAME = "markdown";

    private static final String[] HEADERS = {"Change Set", "Status", "Rule", "Message"};
    private static final int COL_CHANGE_SET = 0;
    private static final int COL_STATUS = 1;
    private static final int COL_RULE = 2;
    private static final int COL_MESSAGE = 3;

    protected MarkdownReporter(Reporter.Config config) {
        super(config);
    }

    @Override
    protected void printChangeLogHeader(PrintWriter output, String fileName) {
        output.append("# ");
        if (isEmpty(fileName)) {
            output.println("*Other*");
        } else {
            output.append('`').append(fileName).println('`');
        }
    }

    @Override
    protected void printByChangeSet(PrintWriter output, List<ReportItem> items) {
        final String[][] table = new String[items.size()][HEADERS.length];
        final int[] maxWidth = Arrays.stream(HEADERS).mapToInt(String::length).toArray();
        int row = 0;

        SortedMap<String, List<ReportItem>> itemsByChangeSet = items.stream()
            .collect(groupingBy(item -> trimToEmpty(item.getChangeSetId()), () -> new TreeMap<>(), toList()));

        for (Map.Entry<String, List<ReportItem>> changeSetEntry : itemsByChangeSet.entrySet()) {
            String changeSet = tableCellFormat(isEmpty(changeSetEntry.getKey()) ? "*none*" : changeSetEntry.getKey());
            maxWidth[COL_CHANGE_SET] = max(maxWidth[COL_CHANGE_SET], tableCellWidth(changeSet));

            final SortedMap<ReportItem.ReportItemType, List<ReportItem>> itemsByType = changeSetEntry.getValue().stream()
                .collect(groupingBy(ReportItem::getType, () -> new TreeMap<>(), toList()));

            for (Map.Entry<ReportItem.ReportItemType, List<ReportItem>> typedEntry : itemsByType.entrySet()) {
                String status = tableCellFormat(typedEntry.getKey().name());
                maxWidth[COL_STATUS] = max(maxWidth[COL_STATUS], tableCellWidth(status));

                for (ReportItem item : typedEntry.getValue()) {
                    table[row][COL_CHANGE_SET] = changeSet;
                    table[row][COL_STATUS] = status;
                    table[row][COL_RULE] = tableCellFormat(item.getRule());
                    table[row][COL_MESSAGE] = tableCellFormat(item.getMessage());

                    maxWidth[COL_RULE] = max(maxWidth[COL_RULE], tableCellWidth(table[row][COL_RULE]));
                    maxWidth[COL_MESSAGE] = max(maxWidth[COL_MESSAGE], tableCellWidth(table[row][COL_MESSAGE]));
                    status = "";
                    changeSet = "";
                    row++;
                }
            }
        }

        printTableHeader(output, maxWidth);
        printTableBody(output, table, maxWidth);
    }

    private static String tableCellFormat(String value) {
        return trimToEmpty(value).replace("\n", "<br>");
    }

    private static int tableCellWidth(String value) {
        return Arrays.stream(trimToEmpty(value).split("<br>"))
            .map(line -> line.length())
            .max(Integer::compare)
            .get();
    }

    private void printTableHeader(PrintWriter output, int[] maxWidth) {
        printTableRow(output, HEADERS, maxWidth);
        for (int col = 0; col < HEADERS.length; col++) {
            output.append("|-").append(repeat("-", maxWidth[col])).append('-');
        }
        output.println('|');
    }

    private void printTableBody(PrintWriter output, String[][] table, int[] maxWidth) {
        for (String[] row : table) {
            printTableRow(output, row, maxWidth);
        }
    }

    private void printTableRow(PrintWriter output, String[] row, int[] maxWidth) {
        for (int col = 0; col < row.length; col++) {
            output.append("| ").append(pad(row[col], maxWidth[col])).append(' ');
        }
        output.println('|');

    }

    @Override
    protected void printSummaryHeader(PrintWriter output, Report report) {
        output.println("# Summary");
    }

    @Override
    protected void printItemTypeSummary(PrintWriter output, ReportItem.ReportItemType type, List<ReportItem> items) {
        output.append("* ").append(type.name()).append(": ").println(items.size());
    }

    @Override
    protected void printSummaryDisabledRules(PrintWriter output, Report report) {
        output.append("* DISABLED: ").println(report.getDisabledRuleCount());
    }

    public static class Builder extends AbstractReporter.Builder<ConsoleReporter.Builder> {

        public Builder() {
            path = "./target/lqlint-report.md";
        }

        @Override
        public MarkdownReporter build() {
            return new MarkdownReporter(this);
        }
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory {

        public Factory() {
            super(NAME, MarkdownReporter.class, new MarkdownReporter.Builder());
        }
    }
}
