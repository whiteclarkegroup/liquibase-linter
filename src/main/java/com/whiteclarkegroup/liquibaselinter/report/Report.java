package com.whiteclarkegroup.liquibaselinter.report;

import com.google.common.collect.ImmutableList;
import com.whiteclarkegroup.liquibaselinter.config.Config;

import java.util.List;
import java.util.Optional;

public class Report {
    private final Config config;
    private final List<ReportItem> items;

    public Report(Config config, List<ReportItem> items) {
        this.config = config;
        this.items = Optional.ofNullable(items).map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }

    public Config getConfig() {
        return config;
    }

    public List<ReportItem> getItems() {
        return items;
    }
}
