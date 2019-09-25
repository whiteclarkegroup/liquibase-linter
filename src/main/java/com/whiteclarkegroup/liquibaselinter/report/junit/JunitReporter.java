package com.whiteclarkegroup.liquibaselinter.report.junit;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import com.whiteclarkegroup.liquibaselinter.report.junit.generated.Testsuites;

@AutoService(Reporter.class)
public class JunitReporter implements Reporter {

    @Override
    public void processReport(Report report) {
        Testsuites testsuites = new Testsuites();
        testsuites.setFailures(String.valueOf(report.countErrors()));
    }

}
