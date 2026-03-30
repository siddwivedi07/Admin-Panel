package com.dams.report;

import org.testng.*;

public class SuiteListener implements ITestListener, ISuiteListener {

    @Override
    public void onTestStart(ITestResult result) {
        ReportManager.startTest(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportManager.endTest("PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ReportManager.endTest("FAIL");
    }

    @Override
    public void onFinish(ISuite suite) {
        ReportManager.flush();
    }
}
