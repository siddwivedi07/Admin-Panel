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
 * LoginPage — Page Object for DAMS Admin login (credentials + OTP).
 *
 * ONLY uses BasePage's actual protected API:
 *   log, wait, driver, type(By,String), click(By), isDisplayed(By)
 *
 * FIX for "OTP screen did not appear":
 *   1. clickLogin() waits for email field to DISAPPEAR before returning
 *      (confirms credential form has been replaced by OTP screen).
 *   2. isOtpFormVisible() uses 5 locator strategies with a 30s first-try
 *      to absorb server latency on CI runners.
 *   3. enterOtp() uses the same waterfall resolver.
 *   4. JS-click fallback on Login and Submit for Ant Design overlays.
 */
public class LoginPage extends BasePage {

    // ── Credential form locators ─────────────────────────────────────────────
    private static final By EMAIL_INPUT =
            By.cssSelector("input#email[placeholder='Email']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("input#password[type='password']");

    private static final By LOGIN_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Login']]");

    // ── OTP locators: 5-strategy waterfall (first match wins) ────────────────
    private static final By OTP_S1 = By.cssSelector("input.ant-otp-input");
    private static final By OTP_S2 = By.cssSelector("input.ant-input:not(#email):not(#password)");
    private static final By OTP_S3 = By.cssSelector("input[maxlength]:not(#email):not(#password)");
    private static final By OTP_S4 = By.cssSelector("input[type='text']:not(#email)");
    private static final By OTP_S5 = By.cssSelector("input:not(#email):not(#password):not([type='hidden'])");

    private static final By SUBMIT_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Submit']]");

    private static final int OTP_WAIT_SEC = 30;

    // ── Step 2: Enter Email ──────────────────────────────────────────────────
    public void enterEmail(String email) {
        log.info("Entering email: {}", email);
        type(EMAIL_INPUT, email);
    }

    // ── Step 3: Enter Password ───────────────────────────────────────────────
    public void enterPassword(String password) {
        log.info("Entering password: [REDACTED]");
        type(PASSWORD_INPUT, password);
    }

    /**
     * Step 4: Click Login button.
     * KEY FIX: waits for email field to become invisible after click,
     * confirming the OTP screen transition has started.
     */
    public void clickLogin() {
        log.info("Clicking Login button");
        WebElement btn = wait.waitForClickable(LOGIN_BUTTON);
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Normal click intercepted, using JS click: {}", e.getMessage());
            jsClick(btn);
        }

        // Wait for credential form to disappear = OTP screen is loading
        log.info("Waiting up to {}s for credential form to transition to OTP screen...", OTP_WAIT_SEC);
        WebDriverWait transWait = new WebDriverWait(driver, Duration.ofSeconds(OTP_WAIT_SEC));
        try {
            transWait.until(ExpectedConditions.invisibilityOfElementLocated(EMAIL_INPUT));
            log.info("Credential form gone — OTP screen is loading.");
        } catch (Exception e) {
            log.warn("Email field still visible after {}s. OTP detection will still be attempted.", OTP_WAIT_SEC);
        }
    }

    // ── Step 5: Enter OTP ────────────────────────────────────────────────────
    public void enterOtp(String otp) {
        log.info("Entering OTP: {}", otp);
        WebElement field = resolveOtpInput();
        field.clear();
        field.sendKeys(otp);
        log.info("OTP entered successfully.");
    }

    // ── Step 5: Click Submit ─────────────────────────────────────────────────
    public void clickSubmit() {
        log.info("Clicking Submit button");
        WebElement btn = wait.waitForClickable(SUBMIT_BUTTON);
        try {
            btn.click();
        } catch (Exception e) {
            log.warn("Normal click intercepted on Submit, using JS click: {}", e.getMessage());
            jsClick(btn);
        }
        log.info("Submit clicked.");
    }

    /**
     * Returns true when the OTP form is visible.
     * Tries 5 locator strategies; first gets 30s, rest get 5s each.
     * Also checks Submit button as a final fallback.
     */
    public boolean isOtpFormVisible() {
        By[] strategies = { OTP_S1, OTP_S2, OTP_S3, OTP_S4, OTP_S5 };
        int[] timeouts  = {    30,      5,      5,      5,      5   };

        for (int i = 0; i < strategies.length; i++) {
            try {
                WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(timeouts[i]));
                WebElement el = w.until(
                        ExpectedConditions.visibilityOfElementLocated(strategies[i]));
                if (el != null && el.isDisplayed()) {
                    log.info("OTP input confirmed via strategy {}: {}", i + 1, strategies[i]);
                    return true;
                }
            } catch (Exception ignored) {
                log.debug("OTP strategy {} did not match: {}", i + 1, strategies[i]);
            }
        }

        // Last resort: Submit button visible = OTP screen is present
        try {
            WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement submit = w.until(
                    ExpectedConditions.visibilityOfElementLocated(SUBMIT_BUTTON));
            if (submit != null && submit.isDisplayed()) {
                log.info("Submit button visible — OTP screen confirmed.");
                return true;
            }
        } catch (Exception ignored) {}

        log.error("isOtpFormVisible() exhausted all strategies. URL: {}", driver.getCurrentUrl());
        return false;
    }

    public boolean isLoginFormVisible() {
        return isDisplayed(EMAIL_INPUT);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Tries each OTP locator in order; returns first visible, enabled element. */
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
