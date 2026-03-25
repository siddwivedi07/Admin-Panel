package com.dams.test;

import com.dams.base.BaseTest;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dams.core.DriverManager;

/**
 * LoginTest
 * ─────────────────────────────────────────────────────────────────────────────
 * Single TestNG test method that executes the full 5-step login + OTP flow and
 * logs exactly 6 rows to the DAMS report (matching the reference screenshot):
 *
 *   TC_1 | Login     | STEP 2 – Enter Email
 *   TC_2 | Login     | STEP 2 – Enter Password
 *   TC_3 | Login     | STEP 3 – Click Login Button
 *   TC_4 | Login     | STEP 4 – Enter OTP
 *   TC_5 | Login     | STEP 5 – Click OTP Submit
 *   TC_6 | Execution | adminLoginTest            ← with View screenshot
 *
 * Step 1 (open URL) is handled by BaseTest.@BeforeMethod.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @Test(description = "adminLoginTest – Login + OTP end-to-end flow")
    public void adminLoginTest() {

        ReportManager report = ReportManager.getInstance();
        report.startTest("adminLoginTest");

        loginPage = new LoginPage();

        // ── TC_1 | STEP 2 – Enter Email ──────────────────────────────────────
        boolean step1Ok = false;
        try {
            loginPage.enterEmail(config.getEmail());
            step1Ok = true;
        } catch (Exception e) {
            log.error("TC_1 failed: {}", e.getMessage());
        }
        report.logStep("TC_1", "Login", "STEP 2 \u2013 Enter Email", step1Ok);
        Assert.assertTrue(step1Ok, "TC_1: Failed to enter email");

        // ── TC_2 | STEP 2 – Enter Password ───────────────────────────────────
        boolean step2Ok = false;
        try {
            loginPage.enterPassword(config.getPassword());
            step2Ok = true;
        } catch (Exception e) {
            log.error("TC_2 failed: {}", e.getMessage());
        }
        report.logStep("TC_2", "Login", "STEP 2 \u2013 Enter Password", step2Ok);
        Assert.assertTrue(step2Ok, "TC_2: Failed to enter password");

        // ── TC_3 | STEP 3 – Click Login Button ───────────────────────────────
        boolean step3Ok = false;
        try {
            loginPage.clickLogin();
            step3Ok = true;
        } catch (Exception e) {
            log.error("TC_3 failed: {}", e.getMessage());
        }
        report.logStep("TC_3", "Login", "STEP 3 \u2013 Click Login Button", step3Ok);
        Assert.assertTrue(step3Ok, "TC_3: Failed to click Login button");

        // Wait for OTP form
        Assert.assertTrue(loginPage.isOtpFormVisible(),
                "OTP form did not appear after clicking Login");

        // ── TC_4 | STEP 4 – Enter OTP ────────────────────────────────────────
        boolean step4Ok = false;
        try {
            loginPage.enterOtp(config.getOtp());
            step4Ok = true;
        } catch (Exception e) {
            log.error("TC_4 failed: {}", e.getMessage());
        }
        report.logStep("TC_4", "Login", "STEP 4 \u2013 Enter OTP", step4Ok);
        Assert.assertTrue(step4Ok, "TC_4: Failed to enter OTP");

        // ── TC_5 | STEP 5 – Click OTP Submit ─────────────────────────────────
        boolean step5Ok = false;
        try {
            loginPage.clickSubmit();
            step5Ok = true;
        } catch (Exception e) {
            log.error("TC_5 failed: {}", e.getMessage());
        }
        report.logStep("TC_5", "Login", "STEP 5 \u2013 Click OTP Submit", step5Ok);
        Assert.assertTrue(step5Ok, "TC_5: Failed to click OTP Submit");

        // ── TC_6 | Execution – overall with screenshot ────────────────────────
        byte[] finalShot = captureCurrentScreenshot();
        boolean allPassed = step1Ok && step2Ok && step3Ok && step4Ok && step5Ok;
        report.endTest(allPassed, finalShot);

        // Final URL assertion – should have navigated away from the login page
        String url = DriverManager.getDriver().getCurrentUrl();
        log.info("Post-login URL: {}", url);
        Assert.assertFalse(
                url.equals(config.getUrl()) || url.contains("login"),
                "Should be on dashboard after login. Actual URL: " + url
        );
    }

    // ── Screenshot override ───────────────────────────────────────────────────

    @Override
    protected byte[] captureCurrentScreenshot() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.warn("Screenshot capture failed: {}", e.getMessage());
            return null;
        }
    }
}
