package com.dams.pages;

import com.dams.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * LoginPage
 * ─────────────────────────────────────────────────────────────────────────────
 * Page Object for the DAMS Admin login flow, which has two steps:
 *
 *   Step A – Credential form
 *     1. Enter email
 *     2. Enter password
 *     3. Click "Login"
 *
 *   Step B – OTP form (appears after successful credential submission)
 *     4. Enter OTP
 *     5. Click "Submit"
 */
public class LoginPage extends BasePage {

    // ── Locators – Credential form ───────────────────────────────────────────

    private static final By EMAIL_INPUT    = By.cssSelector("input#email");
    private static final By PASSWORD_INPUT = By.cssSelector("input#password");

    /**
     * Login button matched by visible text – avoids fragile span:not([style]) tricks.
     */
    private static final By LOGIN_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Login']]");

    // ── Locators – OTP form ──────────────────────────────────────────────────

    /**
     * OTP form indicator: any element that only exists on the OTP step.
     * Covers:
     *   • Ant Design 5.x individual OTP cells  (maxlength="1")
     *   • Single OTP field                      (maxlength="4..8")
     *   • An explicit #otp id
     *   • A heading / label containing "OTP" text
     */
    private static final By OTP_FORM_INDICATOR = By.xpath(
            "//*["
            + "self::input[@maxlength='1'][not(@type='password')] "
            + "or self::input[@maxlength='4'][not(@type='password')] "
            + "or self::input[@maxlength='6'][not(@type='password')] "
            + "or self::input[@id='otp'] "
            + "or self::input[contains(@class,'ant-otp')] "
            + "or self::*[contains(normalize-space(),'OTP') "
            + "           and not(self::script) and not(self::style)]]"
    );

    /**
     * First OTP input cell (for sending keys).
     */
    private static final By OTP_INPUT_FIRST = By.xpath(
            "(//input[@maxlength='1'][not(@type='password')] "
            + "| //input[@maxlength='4'][not(@type='password')] "
            + "| //input[@maxlength='6'][not(@type='password')] "
            + "| //input[@id='otp'] "
            + "| //input[contains(@class,'ant-otp')])[1]"
    );

    /** Submit button matched by visible text. */
    private static final By SUBMIT_BUTTON =
            By.xpath("//button[@type='submit'][.//span[normalize-space()='Submit']]");

    /** Extra wait time for OTP form to appear (server round-trip). */
    private static final int OTP_FORM_TIMEOUT_SEC = 30;

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
     * Click the Login button; falls back to JS click if intercepted.
     */
    public void clickLogin() {
        log.info("Clicking Login button");
        try {
            click(LOGIN_BUTTON);
        } catch (Exception e) {
            log.warn("Standard click failed for Login button, retrying via JS: {}", e.getMessage());
            jsClick(LOGIN_BUTTON);
        }
    }

    /**
     * Enter OTP – handles both single-field and multi-cell (Ant Design) OTP inputs.
     */
    public void enterOtp(String otp) {
        log.info("Entering OTP");

        // Detect multi-cell OTP (maxlength="1" inputs)
        List<WebElement> cells = driver.findElements(
                By.xpath("//input[@maxlength='1'][not(@type='password')]"));

        if (cells.size() >= 2) {
            log.info("Detected multi-cell OTP ({} cells)", cells.size());
            char[] digits = otp.toCharArray();
            for (int i = 0; i < Math.min(digits.length, cells.size()); i++) {
                WebElement cell = cells.get(i);
                wait.waitForClickable(cell);
                cell.clear();
                cell.sendKeys(String.valueOf(digits[i]));
            }
        } else {
            // Single OTP field
            type(OTP_INPUT_FIRST, otp);
        }
    }

    /**
     * Click the Submit button; falls back to JS click if intercepted.
     */
    public void clickSubmit() {
        log.info("Clicking Submit button");
        try {
            click(SUBMIT_BUTTON);
        } catch (Exception e) {
            log.warn("Standard click failed for Submit button, retrying via JS: {}", e.getMessage());
            jsClick(SUBMIT_BUTTON);
        }
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

    // ── State helpers ────────────────────────────────────────────────────────

    /**
     * Waits up to OTP_FORM_TIMEOUT_SEC for the OTP form to appear.
     * Uses a dedicated longer wait because the OTP step requires a
     * server-side credential validation round-trip before rendering.
     */
    public boolean isOtpFormVisible() {
        log.info("Waiting up to {}s for OTP form...", OTP_FORM_TIMEOUT_SEC);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(OTP_FORM_TIMEOUT_SEC))
                    .until(ExpectedConditions.visibilityOfElementLocated(OTP_FORM_INDICATOR));
            log.info("OTP form appeared successfully");
            return true;
        } catch (Exception e) {
            log.error("OTP form NOT visible after {}s – URL: {} | Cause: {}",
                    OTP_FORM_TIMEOUT_SEC, driver.getCurrentUrl(), e.getMessage());
            logPageSourceSnippet();
            return false;
        }
    }

    public boolean isLoginFormVisible() {
        return isDisplayed(EMAIL_INPUT);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void jsClick(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void logPageSourceSnippet() {
        try {
            String src = driver.getPageSource();
            log.debug("Page source (first 3000 chars for diagnosis):\n{}",
                    src.substring(0, Math.min(src.length(), 3000)));
        } catch (Exception ignored) {}
    }
}
