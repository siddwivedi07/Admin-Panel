package com.dams.test;

import com.dams.core.utils.ScreenshotUtils;
import com.dams.report.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener - TestNG listener that hooks into test lifecycle
 * to auto-log results and capture screenshots on failure.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶ TEST STARTED: {}", result.getName());
        ReportManager.logInfo("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ TEST PASSED: {}", result.getName());
        ReportManager.logPass("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ TEST FAILED: {}", result.getName());
        String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName());
        ReportManager.logFail(
            "Test failed: " + result.getName() + " | Cause: " + result.getThrowable().getMessage(),
            screenshotPath
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⚠ TEST SKIPPED: {}", result.getName());
        ReportManager.logInfo("Test skipped: " + result.getName());
    }
}
