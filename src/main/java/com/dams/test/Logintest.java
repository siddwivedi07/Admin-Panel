package com.dams.test;

import com.dams.core.config.ConfigReader;
import com.dams.core.utils.ScreenshotUtils;
import com.dams.driver.driverfactory.DriverFactory;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;

/**
 * LoginTest - TestNG test class covering the DAMS admin login flow.
 *
 * Test cases:
 *   TC_LOGIN_001 - Successful login with valid credentials
 *   TC_LOGIN_002 - Failed login with invalid credentials
 */
@Listeners(com.dams.test.TestListener.class)
public class LoginTest {

    private static final Logger log = LogManager.getLogger(LoginTest.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    private LoginPage loginPage;

    // ── Suite-level setup / teardown ─────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        // Ensure output directories exist
        new File(config.getScreenshotDir()).mkdirs();
        new File(config.getReportDir()).mkdirs();

        ReportManager.initReports();
        log.info("===== Test Suite Started: DAMS Login Tests =====");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ReportManager.flushReports();
        log.info("===== Test Suite Completed =====");
    }

    // ── Method-level setup / teardown ────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
        loginPage = new LoginPage();
        log.info("---- WebDriver initialised for test method ----");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && config.isScreenshotOnFail()) {
            String path = ScreenshotUtils.captureScreenshot(result.getName());
            ReportManager.logFail("Test failed — screenshot: " + path, path);
        }
        DriverFactory.quitDriver();
        log.info("---- WebDriver quit after test method ----");
    }

    // ── Test Cases ────────────────────────────────────────────────────────────

    /**
     * TC_LOGIN_001
     * Verify that a user can successfully log in with valid credentials.
     */
    @Test(
        testName     = "TC_LOGIN_001",
        description  = "Verify successful login with valid credentials",
        priority     = 1,
        groups       = {"smoke", "regression"}
    )
    public void testSuccessfulLogin() {
        ReportManager.createTest(
            "TC_LOGIN_001 - Successful Login",
            "Verify that the user can log in with valid email and password"
        );

        ReportManager.logInfo("Step 1: Navigating to DAMS login page");
        loginPage.openLoginPage();

        ReportManager.logInfo("Step 2: Entering email — " + config.getEmail());
        loginPage.enterEmail(config.getEmail());

        ReportManager.logInfo("Step 3: Entering password");
        loginPage.enterPassword(config.getPassword());

        ReportManager.logInfo("Step 4: Clicking Login button");
        loginPage.clickLogin();

        ReportManager.logInfo("Verifying login was successful...");
        boolean loginSuccess = loginPage.isLoginSuccessful();

        if (loginSuccess) {
            ReportManager.logPass("Login successful! Redirected to: " + loginPage.getCurrentUrl());
        } else {
            String errMsg = loginPage.isErrorMessageDisplayed()
                ? loginPage.getErrorMessageText()
                : "No error message displayed, but still on login page.";
            ReportManager.logFail("Login failed. Reason: " + errMsg, null);
        }

        Assert.assertTrue(loginSuccess,
            "Expected successful login, but user is still on the login page.");
    }

    /**
     * TC_LOGIN_002
     * Verify that login fails with invalid credentials and an error message is shown.
     */
    @Test(
        testName    = "TC_LOGIN_002",
        description = "Verify login failure with invalid credentials",
        priority    = 2,
        groups      = {"regression"}
    )
    public void testInvalidLoginShowsError() {
        ReportManager.createTest(
            "TC_LOGIN_002 - Invalid Login",
            "Verify that an error is shown for invalid credentials"
        );

        ReportManager.logInfo("Step 1: Navigating to DAMS login page");
        loginPage.openLoginPage();

        ReportManager.logInfo("Step 2: Entering invalid email");
        loginPage.enterEmail("invalid@dams.com");

        ReportManager.logInfo("Step 3: Entering invalid password");
        loginPage.enterPassword("WrongPassword@999");

        ReportManager.logInfo("Step 4: Clicking Login button");
        loginPage.clickLogin();

        ReportManager.logInfo("Verifying error message is displayed...");
        boolean errorShown = loginPage.isErrorMessageDisplayed();

        if (errorShown) {
            ReportManager.logPass("Error message displayed as expected: " + loginPage.getErrorMessageText());
        } else {
            ReportManager.logFail("Expected an error message but none was found.", null);
        }

        Assert.assertTrue(errorShown,
            "Expected an error message for invalid credentials, but none was displayed.");
    }
}

