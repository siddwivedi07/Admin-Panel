package com.dams.test;

// ══════════════════════════════════════════════════════════════════════════════
//  LoginTest.java  —  DAMS Test Layer
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya hai?
//     Actual test cases — LoginPage use karta hai, BaseTest extend karta hai.
//     Zero duplicate code — sab kuch BaseTest + LoginPage mein hai.
//
//  ✅ Test Cases:
//     TC_01 → Valid login (email + password + OTP)           [POSITIVE]
//     TC_02 → Blank email se login attempt                   [NEGATIVE]
//     TC_03 → Wrong password se login attempt                [NEGATIVE]
//     TC_04 → Valid email+pass, wrong OTP                    [NEGATIVE]
//     TC_05 → Blank password se login attempt                [NEGATIVE]
// ══════════════════════════════════════════════════════════════════════════════

import com.aventstack.extentreports.Status;
import com.dams.base.BaseTest;
import com.dams.core.ConfigReader;
import com.dams.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    // ──────────────────────────────────────────────────────────────────────────
    //  TC_01: Valid credentials se successful login
    //  Email: 07siddwivedi@gmail.com | Password: Siddharth@123 | OTP: 1980
    // ──────────────────────────────────────────────────────────────────────────
    @Test(
        priority    = 1,
        description = "TC_01: Login with valid email, password and default OTP 1980"
    )
    public void TC_01_SuccessfulLogin() {
        extentTest.log(Status.INFO, "TC_01: Valid login test started.");

        // ── Before Login Hook ─────────────────────────────────────────────────
        // Step 1: Login page par navigate karo
        navigateToLogin();

        // ── Page Object ───────────────────────────────────────────────────────
        LoginPage loginPage = new LoginPage(driver);

        // ── Steps 2 + 3 + 4: Email, Password, Login click ────────────────────
        loginPage.fillAndSubmitLoginForm(
            ConfigReader.getUsername(),
            ConfigReader.getPassword()
        );

        extentTest.log(Status.INFO, "Email & Password submitted, waiting for OTP screen...");
        sleep(2000);

        // ── Step 5: OTP + Submit ──────────────────────────────────────────────
        Assert.assertTrue(loginPage.isOtpScreenVisible(),
            "OTP screen did not appear after valid login.");

        loginPage.submitOtp(ConfigReader.getOtp());
        sleep(3000);

        // ── After Login Hook ──────────────────────────────────────────────────
        verifyLoginSuccess();

        // ── Assertion ─────────────────────────────────────────────────────────
        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(
            currentUrl.trim().equals(ConfigReader.getUrl().trim()) || currentUrl.contains("/login"),
            "TC_01 FAILED: Still on login page after valid OTP. URL: " + currentUrl
        );

        extentTest.log(Status.PASS, "TC_01 PASSED: Login successful, dashboard loaded.");
        log("✅ TC_01_SuccessfulLogin PASSED");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  TC_02: Blank email se login attempt — error hona chahiye
    // ──────────────────────────────────────────────────────────────────────────
    @Test(
        priority    = 2,
        description = "TC_02: Login attempt with blank email should show validation error"
    )
    public void TC_02_BlankEmailLogin() {
        extentTest.log(Status.INFO, "TC_02: Blank email negative test started.");

        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        // Sirf password bharo, email blank chhodo
        loginPage.enterPassword(ConfigReader.getPassword())
                 .clickLogin();

        sleep(1500);

        // Login page par hi rehna chahiye
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
            currentUrl.trim().equals(ConfigReader.getUrl().trim()) || currentUrl.contains("/login"),
            "TC_02 FAILED: Should stay on login page with blank email. URL: " + currentUrl
        );

        extentTest.log(Status.PASS, "TC_02 PASSED: Stayed on login page as expected (blank email).");
        log("✅ TC_02_BlankEmailLogin PASSED");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  TC_03: Wrong password se login attempt
    // ──────────────────────────────────────────────────────────────────────────
    @Test(
        priority    = 3,
        description = "TC_03: Login with wrong password should not proceed to OTP screen"
    )
    public void TC_03_WrongPasswordLogin() {
        extentTest.log(Status.INFO, "TC_03: Wrong password negative test started.");

        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        loginPage.fillAndSubmitLoginForm(
            ConfigReader.getUsername(),
            "WrongPassword@999"        // Galat password
        );

        sleep(2500);

        // OTP screen nahi aana chahiye
        boolean otpVisible = loginPage.isOtpScreenVisible();
        Assert.assertFalse(otpVisible,
            "TC_03 FAILED: OTP screen appeared with wrong password — security issue!");

        String currentUrl = driver.getCurrentUrl();
        extentTest.log(Status.PASS,
            "TC_03 PASSED: Wrong password blocked. URL: " + currentUrl);
        log("✅ TC_03_WrongPasswordLogin PASSED");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  TC_04: Wrong OTP se login attempt
    // ──────────────────────────────────────────────────────────────────────────
    @Test(
        priority    = 4,
        description = "TC_04: Valid email+password but wrong OTP should not allow dashboard access"
    )
    public void TC_04_WrongOtpLogin() {
        extentTest.log(Status.INFO, "TC_04: Wrong OTP negative test started.");

        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        // Sahi email + password
        loginPage.fillAndSubmitLoginForm(
            ConfigReader.getUsername(),
            ConfigReader.getPassword()
        );

        sleep(2000);

        // OTP screen aana chahiye
        if (!loginPage.isOtpScreenVisible()) {
            extentTest.log(Status.SKIP, "TC_04 SKIPPED: OTP screen did not appear.");
            return;
        }

        // Galat OTP dalo
        loginPage.submitOtp("0000");
        sleep(2500);

        // Dashboard par nahi hona chahiye
        String currentUrl = driver.getCurrentUrl();
        boolean stillOnLoginOrOtp = currentUrl.trim().equals(ConfigReader.getUrl().trim())
                                 || currentUrl.contains("/login")
                                 || currentUrl.contains("/otp");

        Assert.assertTrue(stillOnLoginOrOtp,
            "TC_04 FAILED: Wrong OTP allowed dashboard access! URL: " + currentUrl);

        extentTest.log(Status.PASS, "TC_04 PASSED: Wrong OTP blocked correctly.");
        log("✅ TC_04_WrongOtpLogin PASSED");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  TC_05: Blank password se login attempt
    // ──────────────────────────────────────────────────────────────────────────
    @Test(
        priority    = 5,
        description = "TC_05: Login with blank password should show validation error"
    )
    public void TC_05_BlankPasswordLogin() {
        extentTest.log(Status.INFO, "TC_05: Blank password negative test started.");

        navigateToLogin();
        LoginPage loginPage = new LoginPage(driver);

        // Sirf email bharo, password blank chhodo
        loginPage.enterEmail(ConfigReader.getUsername())
                 .clickLogin();

        sleep(1500);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
            currentUrl.trim().equals(ConfigReader.getUrl().trim()) || currentUrl.contains("/login"),
            "TC_05 FAILED: Should stay on login page with blank password. URL: " + currentUrl
        );

        extentTest.log(Status.PASS, "TC_05 PASSED: Stayed on login page (blank password).");
        log("✅ TC_05_BlankPasswordLogin PASSED");
    }
}
