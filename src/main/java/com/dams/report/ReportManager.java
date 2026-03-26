package com.dams.report;

// ══════════════════════════════════════════════════════════════════════════════
//  ReportManager.java  —  DAMS Report Layer
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya karta hai?
//     ExtentReports setup karta hai — HTML report generate karta hai
//     Pass/Fail/Skip status, logs, screenshots — sab ek jagah
//
//  ✅ Output:
//     test-output/reports/DAMS_Report_yyyyMMdd_HHmmss.html
//
//  ✅ Usage:
//     ReportManager.initReport();
//     ExtentTest test = ReportManager.createTest("LoginTest");
//     test.pass("Login successful");
//     ReportManager.flushReport();
// ══════════════════════════════════════════════════════════════════════════════

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.dams.core.ConfigReader;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    // Singleton ExtentReports instance
    private static ExtentReports extent;

    // ThreadLocal ExtentTest — parallel-safe
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    // Private constructor
    private ReportManager() {}

    // ──────────────────────────────────────────────────────────────────────────
    //  Report initialize karo (BeforeSuite mein call karo)
    // ──────────────────────────────────────────────────────────────────────────
    public static synchronized void initReport() {
        if (extent == null) {
            // Report directory banana
            String reportDir = ConfigReader.getReportPath();
            new File(reportDir).mkdirs();

            // File name with timestamp
            String timestamp  = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportFile = reportDir + "DAMS_Report_" + timestamp + ".html";

            // SparkReporter (modern HTML reporter)
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFile);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle(ConfigReader.getReportTitle());
            spark.config().setReportName(ConfigReader.getReportName());
            spark.config().setEncoding("utf-8");

            // ExtentReports setup
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "DAMS Delhi Admin Panel");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("URL", ConfigReader.getUrl());
            extent.setSystemInfo("Tester", "Siddharth Wivedi");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));

            System.out.println("[REPORT] ✔ ExtentReport initialized: " + reportFile);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Naya test node banana (BeforeMethod mein call karo)
    // ──────────────────────────────────────────────────────────────────────────
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThread.set(test);
        return test;
    }

    public static ExtentTest createTest(String testName) {
        return createTest(testName, "");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Current test get karo
    // ──────────────────────────────────────────────────────────────────────────
    public static ExtentTest getTest() {
        ExtentTest test = testThread.get();
        if (test == null) {
            throw new IllegalStateException("[REPORT] ❌ No ExtentTest found for this thread. Call createTest() first.");
        }
        return test;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Report flush karo (AfterSuite mein call karo)
    //  Flush na karo to report mein data nahi aayega
    // ──────────────────────────────────────────────────────────────────────────
    public static synchronized void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("[REPORT] ✔ ExtentReport flushed and saved.");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  ThreadLocal clean karo (AfterMethod mein call karo)
    // ──────────────────────────────────────────────────────────────────────────
    public static void removeTest() {
        testThread.remove();
    }
}
