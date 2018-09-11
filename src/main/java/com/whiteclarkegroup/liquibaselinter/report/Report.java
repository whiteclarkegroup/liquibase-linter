package com.whiteclarkegroup.liquibaselinter.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {

    private final Collection<ReportItem> reportItems = new ArrayList<>();

    public Collection<ReportItem> getReportItems() {
        return reportItems;
    }

    public Map<String, List<ReportItem>> getByFileName() {
        return reportItems.stream().collect(Collectors.groupingBy(ReportItem::getFilePath));
    }

    public long countErrors() {
        return reportItems.stream().filter(item -> item.getType() == ReportItem.ReportItemType.ERROR).count();
    }

    public boolean hasItems() {
        return !reportItems.isEmpty();
    }

}
