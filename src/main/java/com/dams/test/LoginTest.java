package com.dams.test;

// ══════════════════════════════════════════════════════════════════════════════
//  LoginTest.java  —  DAMS Test Layer
//  ──────────────────────────────────────────────────────────────────────────
//  Extends BaseTest which provides (this repo's actual API):
//    protected WebDriver     driver        — set in @BeforeMethod
//    protected WebDriverWait wait          — set in @BeforeMethod
//    protected ExtentTest    extentTest    — set in @BeforeMethod
//    protected void navigateToLogin()      — opens URL, waits for email field
//    protected void performLogin(email, password, otp)
//    protected void verifyLoginSuccess()
//    protected void clickByText(String)
//    protected void clearAndType(WebElement, String)
//    protected void sleep(long ms)
//    protected void log(String)
//
//  ConfigReader (static methods — this repo's actual API):
//    ConfigReader.getUsername()   — email value
//    ConfigReader.getPassword()
//    ConfigReader.getOtp()
//    ConfigReader.getUrl()
//
//  ReportManager (static methods — this repo's actual API):
//    ReportManager.initReport()
//    ReportManager.createTest(name)  — returns ExtentTest (used in @BeforeMethod)
//    ReportManager.flushReport()
//    ReportManager.removeTest()
//    ReportManager.getTest()         — gets current thread's ExtentTest
// ══════════════════════════════════════════════════════════════════════════════

import com.aventstack.extentreports.Status;
import com.dams.base.BaseTest;
import com.dams.core.ConfigReader;
import com.dams.core.DriverManager;
import com.dams.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(
        priority    = 1,
        description = "TC_01: Login with valid email, password and default OTP 1980"
    )
    public void TC_01_SuccessfulLogin() {
        extentTest.log(Status.INFO, "TC_01: Valid login test started.");

        // Step 1: Navigate to login page (BaseTest.navigateToLogin opens URL
        //         and waits for the email field to be visible)
        navigateToLogin();

        LoginPage loginPage = new LoginPage();

        // Step 2: Enter Email
        try {
            loginPage.enterEmail(ConfigReader.getUsername());
            extentTest.log(Status.INFO, "Step 2: Email entered → " + ConfigReader.getUsername());
            log("  ✔ Step 2: Email entered.");
        } catch (Exception e) {
            extentTest.log(Status.FAIL, "Step 2 FAILED: " + e.getMessage());
            Assert.fail("Failed to enter email: " + e.getMessage());
        }

        // Step 3: Enter Password
        try {
            loginPage.enterPassword(ConfigReader.getPassword());
            extentTest.log(Status.INFO, "Step 3: Password entered.");
            log("  ✔ Step 3: Password entered.");
        } catch (Exception e) {
            extentTest.log(Status.FAIL, "Step 3 FAILED: " + e.getMessage());
            Assert.fail("Failed to enter password: " + e.getMessage());
        }

        // Step 4: Click Login
        // clickLogin() waits internally for the credential form to disappear
        try {
            loginPage.clickLogin();
            extentTest.log(Status.INFO, "Step 4: Login button clicked.");
            log("  ✔ Step 4: Login button clicked.");
        } catch (Exception e) {
            extentTest.log(Status.FAIL, "Step 4 FAILED: " + e.getMessage());
            Assert.fail("Failed to click Login button: " + e.getMessage());
        }

        // Step 5a: Verify OTP screen appeared
        // isOtpFormVisible() tries 5 locator strategies over up to 30s
        Assert.assertTrue(
            loginPage.isOtpFormVisible(),
            "OTP screen did not appear after clicking Login. " +
            "Check credentials in config.properties."
        );
        extentTest.log(Status.INFO, "Step 5: OTP screen visible.");

        // Step 5b: Enter OTP
        try {
            loginPage.enterOtp(ConfigReader.getOtp());
            extentTest.log(Status.INFO, "Step 5: OTP entered → " + ConfigReader.getOtp());
            log("  ✔ Step 5: OTP entered.");
        } catch (Exception e) {
            extentTest.log(Status.FAIL, "Step 5 OTP entry FAILED: " + e.getMessage());
            Assert.fail("Failed to enter OTP: " + e.getMessage());
        }

        // Step 5c: Click Submit
        try {
            loginPage.clickSubmit();
            extentTest.log(Status.INFO, "Step 5: Submit button clicked.");
            log("  ✔ Step 5: Submit clicked.");
        } catch (Exception e) {
            extentTest.log(Status.FAIL, "Step 5 Submit FAILED: " + e.getMessage());
            Assert.fail("Failed to click Submit: " + e.getMessage());
        }

        sleep(3000);

        // After login: verify redirected to dashboard
        verifyLoginSuccess();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(
            currentUrl.trim().equals(ConfigReader.getUrl().trim()) || currentUrl.contains("login"),
            "TC_01 FAILED: Still on login page. URL: " + currentUrl
        );

        extentTest.log(Status.PASS, "TC_01 PASSED: Login successful. URL: " + currentUrl);
        log("✅ TC_01_SuccessfulLogin PASSED");
    }

    @Test(
        priority    = 2,
        description = "TC_02: Login with blank email should stay on login page"
    )
    public void TC_02_BlankEmailLogin() {
        extentTest.log(Status.INFO, "TC_02: Blank email negative test.");
        navigateToLogin();
        LoginPage loginPage = new LoginPage();

        loginPage.enterPassword(ConfigReader.getPassword());
        clickByText("Login");
        sleep(1500);

        String url = driver.getCurrentUrl();
        Assert.assertTrue(
            url.trim().equals(ConfigReader.getUrl().trim()) || url.contains("login"),
            "TC_02 FAILED: Should stay on login page. URL: " + url
        );
        extentTest.log(Status.PASS, "TC_02 PASSED: Stayed on login page (blank email).");
        log("✅ TC_02_BlankEmailLogin PASSED");
    }

    @Test(
        priority    = 3,
        description = "TC_03: Login with wrong password should not show OTP screen"
    )
    public void TC_03_WrongPasswordLogin() {
        extentTest.log(Status.INFO, "TC_03: Wrong password negative test.");
        navigateToLogin();
        LoginPage loginPage = new LoginPage();

        loginPage.enterEmail(ConfigReader.getUsername());
        loginPage.enterPassword("WrongPassword@999");
        clickByText("Login");
        sleep(2500);

        boolean otpVisible = loginPage.isOtpFormVisible();
        if (otpVisible) {
            extentTest.log(Status.FAIL, "TC_03 FAILED: OTP appeared with wrong password.");
            Assert.fail("OTP screen appeared with wrong password — security issue.");
        }
        extentTest.log(Status.PASS, "TC_03 PASSED: Wrong password blocked correctly.");
        log("✅ TC_03_WrongPasswordLogin PASSED");
    }
}
