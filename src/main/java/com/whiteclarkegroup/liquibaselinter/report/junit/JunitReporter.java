package com.whiteclarkegroup.liquibaselinter.report.junit;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import com.whiteclarkegroup.liquibaselinter.report.junit.generated.Testsuite;

@AutoService(Reporter.class)
public class JunitReporter implements Reporter {

    @Override
    public void processReport(Report report) {
        Testsuite testsuite = new Testsuite();
        testsuite.setName("liquibase-linter");
        testsuite.setTests("0");
        testsuite.setDisabled(String.valueOf(report.countIgnored()));
        testsuite.setFailures(String.valueOf(report.countErrors()));

    }

}
