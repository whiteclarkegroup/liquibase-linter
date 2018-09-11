package com.whiteclarkegroup.liquibaselinter.report;

import java.util.Collection;

public class ConsoleReporter implements Reporter {

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String YELLOW = "\033[0;33m";

    @Override
    public void report(Collection<ReportItem> reportItems) {

    }

}
