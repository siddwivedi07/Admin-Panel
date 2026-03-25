package com.dams.report;

import com.dams.utility.ScreenshotUtility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ExtentTestListener.java — Automatic Report Hook
 *
 * RESPONSIBILITY:
 *   Listens to TestNG events and automatically updates the HTML report.
 *   This class is the "bridge" between TestNG and ReportManager.
 *
 *   You never call this class directly — TestNG calls it automatically
 *   because BaseTest is annotated with @Listeners(ExtentTestListener.class).
 *
 * TESTNG LISTENER EVENTS:
 *   onTestStart   → test is about to begin   → create a new report entry
 *   onTestSuccess → test passed               → log PASS
 *   onTestFailure → test threw exception      → log FAIL + capture screenshot
 *   onTestSkipped → test was skipped          → log SKIP
 *   onFinish      → all tests done            → flush report to disk
 *
 * WHY A SEPARATE LISTENER?
 *   Without this, you'd have to manually call ReportManager.logPass()
 *   at the end of every test. The listener automates the pass/fail/skip
 *   status — you only need to log individual steps inside the test.
 */
public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Build a readable display name: "LoginTest → verifyLoginWithValidCredentials"
        String testName = result.getTestClass().getRealClass().getSimpleName()
                          + " → "
                          + result.getMethod().getMethodName();

        // Description from @Test(description="...") or a default message
        String desc = result.getMethod().getDescription();
        if (desc == null || desc.isEmpty()) {
            desc = "Automated UI test";
        }

        ReportManager.createTest(testName, desc);
        ReportManager.logInfo("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportManager.logPass("✔ Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Capture a screenshot of the browser at the moment of failure
        String screenshotPath = ScreenshotUtility.captureScreenshot(
                result.getMethod().getMethodName());

        // Log failure with exception message and embed the screenshot
        ReportManager.markFail(result.getThrowable(), screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ReportManager.logInfo("⚠ Test SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush is already handled by BaseTest @AfterSuite — nothing needed here.
        // But we log a summary to console for visibility.
        System.out.println("[ExtentTestListener] Suite finished. "
            + "Passed: "  + context.getPassedTests().size()
            + " | Failed: " + context.getFailedTests().size()
            + " | Skipped: " + context.getSkippedTests().size());
    }
}
