package com.whiteclarkegroup.liquibaselinter.report;

import java.util.Set;

public interface Reporter {
    boolean isEnabled();
    void process(Report report);

    interface Config {
        boolean isEnabled();
        Set<ReportItem.ReportItemType> getFilter();
        String getPath();
    }

    interface Factory {
        boolean supports(String name);
        Reporter create(Object config);
        Reporter create(boolean enabled);
        Reporter create(String path);
    }
}
