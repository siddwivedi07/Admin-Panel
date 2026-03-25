package com.dams.base;

import com.dams.core.ConfigReader;
import com.dams.core.DriverManager;
import com.dams.report.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest
 * ─────────────────────────────────────────────────────────────────────────────
 * TestNG lifecycle base for every test class.
 *
 *   @BeforeSuite  – init ReportManager
 *   @BeforeMethod – spin up ChromeDriver, open application URL
 *   @AfterMethod  – quit driver; on failure add TC_6 with screenshot
 *   @AfterSuite   – flush HTML report to disk
 */
public abstract class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected final ConfigReader  config = ConfigReader.getInstance();

    // ── Suite ─────────────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        log.info("=== DAMS Selenium Suite Starting ===");
        ReportManager.getInstance().init();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        ReportManager.getInstance().flush();
        log.info("=== Suite finished – report written ===");
    }

    // ── Method ────────────────────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void methodSetup(java.lang.reflect.Method method) {
        log.info("--- Starting: {} ---", method.getName());
        DriverManager.initDriver();
        DriverManager.getDriver().get(config.getUrl());
        log.info("Opened URL: {}", config.getUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void methodTeardown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.warn("FAILED: {}", result.getName());
            // Capture screenshot for TC_6 row
            byte[] shot = captureCurrentScreenshot();
            ReportManager.getInstance().endTest(false, shot); 
        }
        DriverManager.quitDriver();
    }

    // ── Screenshot helper (overridden by test classes) ────────────────────────

    protected byte[] captureCurrentScreenshot() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.warn("Screenshot failed: {}", e.getMessage());
            return null;
        }
    }
}
