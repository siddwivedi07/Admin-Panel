package com.dams.pages;

import com.dams.core.config.Config;
import com.dams.report.ReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class LoginPage {

    // ── Credentials (read from env via Config) ────────────────────────────────
    private static final String EMAIL    = Config.getEmail();
    private static final String PASSWORD = Config.getPassword();

    // ── Locators ──────────────────────────────────────────────────────────────

    // STEP 2 — Email field
    private static final By EMAIL_INPUT =
            By.cssSelector("input#email[placeholder='Email']");

    // STEP 2 — Password field
    private static final By PASSWORD_INPUT =
            By.cssSelector("input#password[placeholder='******']");

    // STEP 3 — Login submit button
    private static final By LOGIN_BUTTON =
            By.cssSelector("button[type='submit'].ant-btn-primary span");

    // STEP 4 — OTP widget: 4 individual digit boxes (Ant Design OTP input)
    private static final By OTP_INPUTS_INDIVIDUAL =
            By.cssSelector("div.otp-input input[aria-label*='Please enter OTP character']");

    // STEP 4 — OTP fallback: single consolidated input
    private static final By OTP_INPUT_SINGLE =
            By.cssSelector("input[maxlength='4'], input[placeholder*='OTP'], input[placeholder*='otp']");

    // STEP 5 — OTP Submit button
    private static final By OTP_SUBMIT_BUTTON =
            By.cssSelector("button[type='submit'].ant-btn-primary.ant-btn-color-primary" +
                           ".ant-btn-variant-solid.ant-btn-lg.ant-btn-block");

    // Post-login dashboard indicator — used by isLoggedIn() check
    private static final By DASHBOARD_INDICATOR =
            By.cssSelector(".ant-layout-sider, .ant-menu, li.ant-menu-item a[href='/']");

    // ── Driver & Wait ─────────────────────────────────────────────────────────
    private final WebDriver     driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void loginToAdminPortal() {
        if (isLoggedIn()) {
            System.out.println("[LoginPage] ✔ Session already active — skipping login.");
            ReportManager.logStep("Login", "Session Already Active — Login Skipped", true);
            return;
        }
        System.out.println("[LoginPage] Starting admin portal login flow...");
        login();
    }

    public boolean isLoggedIn() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.presenceOfElementLocated(DASHBOARD_INDICATOR));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void login() {
        enterEmail(EMAIL);
        enterPassword(PASSWORD);
        clickLoginButton();
        waitAndEnterOtp();
        clickOtpSubmitButton();
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 — Enter Email
    // ══════════════════════════════════════════════════════════════════════════
    public void enterEmail(String email) {
        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT));
        emailField.clear();
        emailField.sendKeys(email);
        System.out.println("[LoginPage] STEP 2 ✔ Email entered: " + email);
        ReportManager.logStep("Login", "STEP 2 – Enter Email", true);
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 — Enter Password
    // ══════════════════════════════════════════════════════════════════════════
    public void enterPassword(String password) {
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("[LoginPage] STEP 2 ✔ Password entered.");
        ReportManager.logStep("Login", "STEP 2 – Enter Password", true);
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 — Click Login Button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickLoginButton() {
        WebElement loginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginBtn.click();
        System.out.println("[LoginPage] STEP 3 ✔ Login button clicked.");
        ReportManager.logStep("Login", "STEP 3 – Click Login Button", true);
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 — Wait for OTP screen, then enter OTP
    // ══════════════════════════════════════════════════════════════════════════
    public void waitAndEnterOtp() {
        System.out.println("[LoginPage] STEP 4 – Waiting 30s for OTP screen...");
        sleep(30_000);

        String otp = Config.getOtp();

        if (otp == null || otp.isBlank() || "-".equals(otp)) {
            ReportManager.logStep("Login", "STEP 4 – OTP Not Configured", false);
            throw new IllegalStateException(
                    "[LoginPage] OTP is not set. Please configure ADMIN_LOGIN GitHub Secret.");
        }
        if (otp.length() != 4) {
            ReportManager.logStep("Login", "STEP 4 – OTP Invalid Length", false);
            throw new IllegalArgumentException(
                    "[LoginPage] OTP must be exactly 4 digits.");
        }

        List<WebElement> splitInputs;
        try {
            splitInputs = wait.until(
                    ExpectedConditions.numberOfElementsToBe(OTP_INPUTS_INDIVIDUAL, 4));
        } catch (TimeoutException e) {
            splitInputs = driver.findElements(OTP_INPUTS_INDIVIDUAL);
        }

        if (!splitInputs.isEmpty()) {
            enterOtpInSplitInputs(splitInputs, otp);
        } else {
            enterOtpInSingleInput(otp);
        }

        System.out.println("[LoginPage] STEP 4 ✔ OTP entered successfully.");
        ReportManager.logStep("Login", "STEP 4 – Enter OTP", true);
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 — Click OTP Submit Button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickOtpSubmitButton() {
        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(OTP_SUBMIT_BUTTON));
        submitBtn.click();
        System.out.println("[LoginPage] STEP 5 ✔ OTP Submit button clicked.");
        ReportManager.logStep("Login", "STEP 5 – Click OTP Submit", true);
    }
    // ══════════════════════════════════════════════════════════════════════════
    //  Private helpers
    // ══════════════════════════════════════════════════════════════════════════
    /** Fills each digit into its own Ant Design OTP box. */
    private void enterOtpInSplitInputs(List<WebElement> inputs, String otp) {
        for (int i = 0; i < otp.length() && i < inputs.size(); i++) {
            WebElement box = inputs.get(i);
            box.clear();
            box.sendKeys(String.valueOf(otp.charAt(i)));
            System.out.println("[LoginPage] OTP digit " + (i + 1) + " entered.");
        }
    }
    /** Types the full 4-digit OTP into a single consolidated input field. */
    private void enterOtpInSingleInput(String otp) {
        WebElement otpField;
        try {
            otpField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(OTP_INPUT_SINGLE));
        } catch (TimeoutException e) {
            throw new NoSuchElementException(
                    "[LoginPage] Could not locate any OTP input field on the page.");
        }
        otpField.clear();
        otpField.sendKeys(otp);
        System.out.println("[LoginPage] OTP entered in single input field.");
    }
    /** Convenience wrapper around Thread.sleep. */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
