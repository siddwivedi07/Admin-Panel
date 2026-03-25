package com.dams.base;

import com.dams.driver.DriverFactory;
import com.dams.pages.LoginPage;
import com.dams.report.ExtentTestListener;
import com.dams.report.ReportManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/**
 * BaseTest.java — Foundation for All Test Classes
 *
 * RESPONSIBILITY:
 *   Manages the setup and teardown lifecycle for every test.
 *   Every test class extends this class to inherit:
 *     - BeforeSuite : initialise the HTML report (runs once before all tests)
 *     - BeforeLogin : start browser, navigate to URL, perform full login
 *     - AfterLogin  : close browser after each test
 *     - AfterSuite  : write/flush the HTML report to disk (runs once after all tests)
 *
 * TESTNG EXECUTION ORDER:
 *   @BeforeSuite  → runs ONCE before the entire test suite starts
 *   @BeforeMethod → runs BEFORE EACH @Test method
 *   @Test         → the actual test (defined in child class)
 *   @AfterMethod  → runs AFTER EACH @Test method
 *   @AfterSuite   → runs ONCE after the entire test suite finishes
 *
 * CREDENTIALS:
 *   Read from environment variables (set as GitHub Secrets in CI).
 *   If not set, falls back to the default values below for local development.
 *   NEVER hardcode production credentials in committed code.
 *
 *   Set locally:
 *     export ADMIN_USERNAME="07siddwivedi@gmail.com"
 *     export ADMIN_PASSWORD="Siddharth@123"
 *     export ADMIN_OTP="1980"
 *
 * @Listeners annotation:
 *   Tells TestNG to call ExtentTestListener on each test event (start/pass/fail/skip).
 *   This is what auto-populates the HTML report without manual logging in every test.
 */
@Listeners(ExtentTestListener.class)
public class BaseTest {

    // Shared across all test methods in the same thread via protected access
    protected WebDriver driver;
    protected LoginPage  loginPage;

    // ── Credentials (env vars → fallback defaults) ──────────────────────────
    private static final String DEFAULT_USERNAME = "07siddwivedi@gmail.com";
    private static final String DEFAULT_PASSWORD = "Siddharth@123";
    private static final String DEFAULT_OTP      = "1980";

    // ── Suite Lifecycle ──────────────────────────────────────────────────────

    /**
     * BEFORE SUITE
     * Runs exactly ONCE before the first test in the suite begins.
     * Initialises the HTML report engine (creates the output file + sets theme).
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("\n========================================");
        System.out.println("  DAMS Automation Suite — Starting");
        System.out.println("========================================");
        ReportManager.initialise();
        System.out.println("[BaseTest] @BeforeSuite complete — report initialised.");
    }

    /**
     * AFTER SUITE
     * Runs exactly ONCE after the last test in the suite completes.
     * Flushes (writes) the HTML report to disk so it's readable.
     * Without this, the HTML file is empty/incomplete.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ReportManager.flush();
        System.out.println("[BaseTest] @AfterSuite complete — HTML report written to disk.");
        System.out.println("========================================");
        System.out.println("  DAMS Automation Suite — Finished");
        System.out.println("========================================\n");
    }

    // ── Test Method Lifecycle ────────────────────────────────────────────────

    /**
     * BEFORE LOGIN (BeforeMethod)
     * Runs before EACH @Test method.
     *
     * What it does:
     *   1. Reads credentials from environment variables (or defaults)
     *   2. Starts a new ChromeDriver via DriverFactory
     *   3. Creates a LoginPage instance
     *   4. Performs the full login: URL → Email → Password → Login → OTP → Submit
     *
     * After this method, the browser is on the DAMS dashboard, ready for the test.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeLogin() {
        System.out.println("\n[BaseTest] @BeforeMethod — Setting up browser and logging in...");

        // Read credentials: environment variable first, then fallback default
        String username = getEnvOrDefault("ADMIN_USERNAME", DEFAULT_USERNAME);
        String password = getEnvOrDefault("ADMIN_PASSWORD", DEFAULT_PASSWORD);
        String otp      = getEnvOrDefault("ADMIN_OTP",      DEFAULT_OTP);

        // Start browser via DriverFactory
        driver    = DriverFactory.initDriver();
        loginPage = new LoginPage(driver);

        // Perform full login flow (all 6 steps are inside LoginPage)
        loginPage.performFullLogin(username, password, otp);

        System.out.println("[BaseTest] @BeforeMethod complete — logged in, dashboard ready.");
    }

    /**
     * AFTER LOGIN (AfterMethod)
     * Runs after EACH @Test method (whether it passed, failed, or was skipped).
     * alwaysRun=true ensures this runs even if the test threw an exception.
     *
     * What it does:
     *   1. Quits the browser (closes all windows, ends ChromeDriver process)
     *   2. Removes driver from ThreadLocal (prevents memory leaks)
     */
    @AfterMethod(alwaysRun = true)
    public void afterLogin() {
        System.out.println("[BaseTest] @AfterMethod — Closing browser...");
        DriverFactory.quitDriver();
        System.out.println("[BaseTest] @AfterMethod complete — browser closed.");
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    /**
     * Reads an environment variable. Returns the fallback value if not set.
     * Logs which source was used so CI debugging is easier.
     */
    private String getEnvOrDefault(String envKey, String defaultValue) {
        String value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            System.out.println("[BaseTest] Using env var: " + envKey);
            return value.trim();
        }
        System.out.println("[BaseTest] Env var '" + envKey + "' not set — using default.");
        return defaultValue;
    }
}

