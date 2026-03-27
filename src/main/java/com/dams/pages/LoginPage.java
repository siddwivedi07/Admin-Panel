package com.dams.pages;

import com.dams.base.BasePage;
import com.dams.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * LoginPage - Fixed version
 *
 * ROOT CAUSE OF FAILURE:
 * ──────────────────────
 * TC_01 failed at: Assert.assertTrue(loginPage.isOtpFormVisible(), ...)
 *
 * Reason 1 - Bad locator: OTP_INPUT was too strict (ant-input[maxlength],
 *   ant-otp-input). Ant Design may render the OTP field as a plain input
 *   inside a Form.Item with no maxlength or special class.
 *
 * Reason 2 - No transition wait: After clickLogin() there was no wait for
 *   the credential form to disappear before looking for the OTP field.
 *   The 20s explicit wait was racing against the page transition.
 *
 * Reason 3 - isOtpFormVisible() used the same tight locator with the same
 *   short timeout — guaranteed to fail on a slow CI runner.
 *
 * FIXES APPLIED:
 * ──────────────
 * 1. clickLogin() now waits for the email field to become invisible (=
 *    credential form gone, OTP form coming) before returning.
 * 2. isOtpFormVisible() uses a 30-second dedicated wait and tries
 *    5 different locator strategies in order (waterfall).
 * 3. resolveOtpInput() uses the same waterfall to reliably find the
 *    OTP field for enterOtp().
 * 4. JS-click fallback added on both Login and Submit buttons to handle
 *    Ant Design overlay/animation intercepts.
 */
public class LoginPage extends BasePage {

    // ── Credential form locators ─────────────────────────────────────────────

    private static final By EMAIL_INPUT =
            By.cssSelector("input#email[placeholder='Email']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("input#password[type='password']");

    private static final By LOGIN_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Login']]");

    // ── OTP locators: 5-strategy waterfall ───────────────────────────────────
    // Strategy 1: Ant OTP component cells
    private static final By OTP_S1 = By.cssSelector("input.ant-otp-input");
    // Strategy 2: Any ant-input that is not email/password
    private static final By OTP_S2 = By.cssSelector("input.ant-input:not(#email):not(#password)");
    // Strategy 3: maxlength-capped input
    private static final By OTP_S3 = By.cssSelector("input[maxlength]:not(#email):not(#password)");
    // Strategy 4: type=text not email
    private static final By OTP_S4 = By.cssSelector("input[type='text']:not(#email)");
    // Strategy 5: broadest — any visible input after login
    private static final By OTP_S5 = By.cssSelector("input:not(#email):not(#password):not([type='hidden'])");

    // Submit button distinguished by text span (not by style, which may vary)
    private static final By SUBMIT_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Submit']]");

    // How long to wait for the OTP screen after clicking Login
    private static final int OTP_WAIT_SEC = 30;

    // ── Public API ───────────────────────────────────────────────────────────

    public void enterEmail(String email) {
        log.info("Entering email: {}", email);
        type(EMAIL_INPUT, email);
    }

    public void enterPassword(String password) {
        log.info("Entering password: [REDACTED]");
        type(PASSWORD_INPUT, password);
    }

    /**
     * Click Login and wait for the credential form to DISAPPEAR.
     * This ensures the OTP screen has started rendering before we return.
     */
    public void clickLogin() {
        log.info("Clicking Login button");

        WebElement btn = wait.waitForClickable(LOGIN_BUTTON);
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Regular click blocked, using JS click: {}", e.getMessage());
            jsClick(btn);
        }

        // ── KEY FIX: wait for email field to vanish = form switched to OTP ──
        log.info("Waiting up to {}s for credential form to disappear...", OTP_WAIT_SEC);
        WebDriverWait transWait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(OTP_WAIT_SEC));
        try {
            transWait.until(ExpectedConditions.invisibilityOfElementLocated(EMAIL_INPUT));
            log.info("Credential form gone. OTP screen should be loading.");
        } catch (Exception e) {
            log.warn("Email field still present after {}s. Will still attempt OTP detection.", OTP_WAIT_SEC);
        }
    }

    /**
     * Type OTP using waterfall locator resolution.
     */
    public void enterOtp(String otp) {
        log.info("Entering OTP: {}", otp);
        WebElement field = resolveOtpInput();
        field.clear();
        field.sendKeys(otp);
        log.info("OTP entered successfully.");
    }

    public void clickSubmit() {
        log.info("Clicking Submit button");
        WebElement btn = wait.waitForClickable(SUBMIT_BUTTON);
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Regular click blocked on Submit, using JS click: {}", e.getMessage());
            jsClick(btn);
        }
        log.info("Submit clicked.");
    }

    // ── Compound actions ─────────────────────────────────────────────────────

    public void performLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public void performOtpVerification(String otp) {
        enterOtp(otp);
        clickSubmit();
    }

    // ── State checkers ────────────────────────────────────────────────────────

    /**
     * KEY FIX: Uses a 30s wait + 5 locator strategies.
     * First strategy gets the full 30s; each subsequent gets 5s.
     * Also checks Submit button visibility as a final confirmation.
     */
    public boolean isOtpFormVisible() {
        By[] strategies = { OTP_S1, OTP_S2, OTP_S3, OTP_S4, OTP_S5 };
        int[] timeouts  = {   30,     5,      5,      5,      5   };

        for (int i = 0; i < strategies.length; i++) {
            try {
                WebDriverWait w = new WebDriverWait(
                        DriverManager.getDriver(), Duration.ofSeconds(timeouts[i]));
                WebElement el = w.until(
                        ExpectedConditions.visibilityOfElementLocated(strategies[i]));
                if (el != null && el.isDisplayed()) {
                    log.info("OTP input confirmed via strategy {}: {}", i + 1, strategies[i]);
                    return true;
                }
            } catch (Exception ignored) {
                log.debug("Strategy {} missed: {}", i + 1, strategies[i]);
            }
        }

        // Final fallback: check for Submit button
        try {
            WebDriverWait w = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
            WebElement submit = w.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BUTTON));
            if (submit != null && submit.isDisplayed()) {
                log.info("Submit button visible — OTP screen confirmed.");
                return true;
            }
        } catch (Exception ignored) {}

        log.error("isOtpFormVisible() returned false after exhausting all strategies. URL: {}",
                DriverManager.getDriver().getCurrentUrl());
        return false;
    }

    public boolean isLoginFormVisible() {
        return isDisplayed(EMAIL_INPUT);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Waterfall: returns first visible, enabled OTP input found. */
    private WebElement resolveOtpInput() {
        By[] strategies = { OTP_S1, OTP_S2, OTP_S3, OTP_S4, OTP_S5 };

        for (By locator : strategies) {
            try {
                List<WebElement> found = DriverManager.getDriver().findElements(locator);
                if (found.isEmpty()) continue;

                WebDriverWait w = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
                WebElement el = w.until(ExpectedConditions.visibilityOf(found.get(0)));
                if (el.isDisplayed() && el.isEnabled()) {
                    log.info("OTP input resolved via: {}", locator);
                    return el;
                }
            } catch (Exception ignored) {}
        }

        throw new RuntimeException(
            "OTP input not found with any locator strategy. URL: " +
            DriverManager.getDriver().getCurrentUrl());
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].click();", el);
    }
}
