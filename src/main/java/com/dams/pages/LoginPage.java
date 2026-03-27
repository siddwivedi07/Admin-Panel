package com.dams.pages;

// ══════════════════════════════════════════════════════════════════════════════
//  LoginPage.java  —  DAMS Pages Layer (Page Object Model)
//  ──────────────────────────────────────────────────────────────────────────
//  FIX: This repo has NO BasePage class — LoginPage now uses DriverManager
//  and WebDriverWait directly instead of extending a missing BasePage.
//
//  APIs used (all exist in this repo):
//    DriverManager.getDriver()       — com.dams.core.DriverManager
//    ConfigReader.getExplicitWait()  — com.dams.core.ConfigReader (static)
//    WebDriver, WebDriverWait, By    — Selenium
//
//  OTP FIX (for "OTP screen did not appear"):
//    1. clickLogin() waits for email field to DISAPPEAR after click,
//       confirming the credential form has been replaced by the OTP screen.
//    2. isOtpFormVisible() uses 5 locator strategies with 30s on first try.
//    3. resolveOtpInput() uses same waterfall so enterOtp() never fails.
//    4. JS-click fallback on Login + Submit for Ant Design overlay intercepts.
// ══════════════════════════════════════════════════════════════════════════════

import com.dams.core.ConfigReader;
import com.dams.core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Constructor ───────────────────────────────────────────────────────────
    public LoginPage() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    // ── Credential form locators ──────────────────────────────────────────────
    private static final By EMAIL_INPUT =
            By.cssSelector("input#email[placeholder='Email']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("input#password[type='password']");

    private static final By LOGIN_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Login']]");

    // ── OTP locators: 5-strategy waterfall (first match wins) ─────────────────
    private static final By OTP_S1 = By.cssSelector("input.ant-otp-input");
    private static final By OTP_S2 = By.cssSelector("input.ant-input:not(#email):not(#password)");
    private static final By OTP_S3 = By.cssSelector("input[maxlength]:not(#email):not(#password)");
    private static final By OTP_S4 = By.cssSelector("input[type='text']:not(#email)");
    private static final By OTP_S5 = By.cssSelector("input:not(#email):not(#password):not([type='hidden'])");

    private static final By SUBMIT_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Submit']]");

    /** Long wait specifically for the OTP screen transition after Login click */
    private static final int OTP_WAIT_SEC = 30;

    // ── Public Actions ────────────────────────────────────────────────────────

    /** Step 2: Type email into the Email field */
    public void enterEmail(String email) {
        log.info("Entering email: {}", email);
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT));
        el.clear();
        el.sendKeys(email);
    }

    /** Step 3: Type password into the Password field */
    public void enterPassword(String password) {
        log.info("Entering password: [REDACTED]");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT));
        el.clear();
        el.sendKeys(password);
    }

    /**
     * Step 4: Click Login button.
     * KEY FIX: after clicking, waits for the email field to disappear —
     * this confirms the credential form has been replaced by the OTP screen.
     */
    public void clickLogin() {
        log.info("Clicking Login button");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Normal click intercepted, using JS click: {}", e.getMessage());
            jsClick(btn);
        }

        // Wait for credential form to vanish = OTP screen transition started
        log.info("Waiting up to {}s for credential form to disappear...", OTP_WAIT_SEC);
        WebDriverWait transWait = new WebDriverWait(driver, Duration.ofSeconds(OTP_WAIT_SEC));
        try {
            transWait.until(ExpectedConditions.invisibilityOfElementLocated(EMAIL_INPUT));
            log.info("Credential form gone — OTP screen loading.");
        } catch (Exception e) {
            log.warn("Email field still present after {}s — OTP detection will continue.", OTP_WAIT_SEC);
        }
    }

    /** Step 5: Type OTP using waterfall locator resolution */
    public void enterOtp(String otp) {
        log.info("Entering OTP: {}", otp);
        WebElement field = resolveOtpInput();
        field.clear();
        field.sendKeys(otp);
        log.info("OTP entered successfully.");
    }

    /** Step 5: Click the Submit button (OTP form) */
    public void clickSubmit() {
        log.info("Clicking Submit button");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Normal click intercepted on Submit, using JS click: {}", e.getMessage());
            jsClick(btn);
        }
        log.info("Submit clicked.");
    }

    // ── Compound actions ──────────────────────────────────────────────────────

    /** Steps 2+3+4: fill credentials and click Login */
    public void performLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    /** Step 5: enter OTP and click Submit */
    public void performOtpVerification(String otp) {
        enterOtp(otp);
        clickSubmit();
    }

    // ── State checkers ────────────────────────────────────────────────────────

    /**
     * Returns true when the OTP form is visible.
     * Strategy 1 gets 30s; strategies 2-5 get 5s each.
     * Final fallback: checks Submit button visibility.
     */
    public boolean isOtpFormVisible() {
        By[] strategies = { OTP_S1, OTP_S2, OTP_S3, OTP_S4, OTP_S5 };
        int[] timeouts  = {    30,      5,      5,      5,      5   };

        for (int i = 0; i < strategies.length; i++) {
            try {
                WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(timeouts[i]));
                WebElement el = w.until(ExpectedConditions.visibilityOfElementLocated(strategies[i]));
                if (el != null && el.isDisplayed()) {
                    log.info("OTP input confirmed via strategy {}: {}", i + 1, strategies[i]);
                    return true;
                }
            } catch (Exception ignored) {
                log.debug("OTP strategy {} did not match: {}", i + 1, strategies[i]);
            }
        }

        // Last resort: Submit button visible = we are on OTP screen
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement submit = w.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BUTTON));
            if (submit != null && submit.isDisplayed()) {
                log.info("Submit button visible — OTP screen confirmed.");
                return true;
            }
        } catch (Exception ignored) {}

        log.error("isOtpFormVisible() failed — all strategies exhausted. URL: {}", driver.getCurrentUrl());
        return false;
    }

    /** True when the login form's email field is present */
    public boolean isLoginFormVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Waterfall: returns first visible + enabled OTP input found */
    private WebElement resolveOtpInput() {
        By[] strategies = { OTP_S1, OTP_S2, OTP_S3, OTP_S4, OTP_S5 };
        for (By locator : strategies) {
            try {
                List<WebElement> found = driver.findElements(locator);
                if (found.isEmpty()) continue;
                WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement el = w.until(ExpectedConditions.visibilityOf(found.get(0)));
                if (el.isDisplayed() && el.isEnabled()) {
                    log.info("OTP input resolved via: {}", locator);
                    return el;
                }
            } catch (Exception ignored) {}
        }
        throw new RuntimeException(
            "OTP input not found with any locator strategy. URL: " + driver.getCurrentUrl());
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
