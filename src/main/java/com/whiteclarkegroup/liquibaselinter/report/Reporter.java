package com.whiteclarkegroup.liquibaselinter.report;

import java.util.Collection;

public interface Reporter {
    void report(Collection<ReportItem> reportItems);
}
