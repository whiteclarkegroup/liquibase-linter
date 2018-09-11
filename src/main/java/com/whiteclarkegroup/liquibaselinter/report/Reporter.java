package com.whiteclarkegroup.liquibaselinter.report;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Reporter {

    public static final Collection<Reporter> REPORTERS = new ArrayList<>();

    Reporter() {
        REPORTERS.add(this);
    }

    abstract void report(Collection<ReportItem> reportItems);

}
