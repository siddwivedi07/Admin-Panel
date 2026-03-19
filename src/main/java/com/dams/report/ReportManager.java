package com.dams.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.dams.core.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReportManager - Manages ExtentReports lifecycle.
 * Provides methods to log test steps, pass/fail results, and screenshots.
 */
public class ReportManager {

    private static final Logger log = LogManager.getLogger(ReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private ReportManager() { /* Utility class */ }

    /**
     * Initialises the ExtentReports instance. Call once before all tests.
     */
    public static void initReports() {
        if (extent == null) {
            String timestamp   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath  = config.getReportDir() + "/DAMS_Report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle(config.getReportName());
            spark.config().setReportName("DAMS Selenium Automation Report");
            spark.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "DAMS Delhi - Stage");
            extent.setSystemInfo("Environment", "Staging");
            extent.setSystemInfo("Tester",      "Automation QA");
            extent.setSystemInfo("Browser",     config.getBrowser());

            log.info("ExtentReports initialised. Report will be saved at: {}", reportPath);
        }
    }

    /**
     * Creates a new test entry in the report for the current thread.
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThreadLocal.set(test);
        log.info("ExtentTest created: {}", testName);
        return test;
    }

    /**
     * Returns the ExtentTest for the current thread.
     */
    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    /**
     * Logs an INFO step in the current test.
     */
    public static void logInfo(String message) {
        log.info(message);
        if (getTest() != null) getTest().log(Status.INFO, message);
    }

    /**
     * Logs a PASS step in the current test.
     */
    public static void logPass(String message) {
        log.info("[PASS] {}", message);
        if (getTest() != null) getTest().pass(message);
    }

    /**
     * Logs a FAIL step (with optional screenshot path) in the current test.
     */
    public static void logFail(String message, String screenshotPath) {
        log.error("[FAIL] {}", message);
        if (getTest() != null) {
            getTest().fail(message);
            if (screenshotPath != null) {
                getTest().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            }
        }
    }

    /**
     * Flushes and writes the report to disk. Call once after all tests.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports flushed and saved.");
        }
    }
}

