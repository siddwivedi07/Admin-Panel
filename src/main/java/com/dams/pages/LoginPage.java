package com.dams.pages;

import com.dams.core.Config;
import com.dams.driver.DriverFactory;
import com.dams.report.ReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage.java — Page Object for the DAMS Admin Login Page
 *
 * RESPONSIBILITY:
 *   Contains ALL login-related scripts for the DAMS Admin Panel login flow:
 *     Step 1 → Navigate to the URL
 *     Step 2 → Enter Email
 *     Step 3 → Enter Password
 *     Step 4 → Click Login button
 *     Step 5 → Enter OTP (4-character field)
 *     Step 6 → Click Submit button
 *
 * PAGE OBJECT MODEL (POM) PATTERN:
 *   - Locators (By objects) are defined at the top as class fields
 *   - Methods represent user actions on this page
 *   - Test classes call these methods — they never use By/findElement directly
 *   - If the website HTML changes, only this file needs updating
 *
 * LOCATOR TYPES USED:
 *   By.cssSelector → targets elements by CSS (fast, recommended for IDs/classes)
 *   By.xpath       → targets elements by XML path (powerful for complex conditions)
 *
 *   CSS  "#email"          → finds element with id="email"
 *   CSS  "#password"       → finds element with id="password"
 *   XPath //button[...]    → finds button containing specific class and text
 *   XPath //input[@aria-label='...'] → finds OTP fields by accessibility label
 *
 * EXPLICIT WAIT (WebDriverWait):
 *   Instead of Thread.sleep(), we use WebDriverWait with ExpectedConditions.
 *   This waits up to N seconds for a condition to be true, then proceeds.
 *   Much faster than fixed sleep, and more reliable on slow networks.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators — Element Selectors ────────────────────────────────────────
    // These match the exact HTML on https://devadmin.damsdelhi.com/

    // Step 2: Email input field
    // HTML: <input placeholder="Email" type="text" id="email" ...>
    private final By emailInput = By.cssSelector("#email");

    // Step 3: Password input field
    // HTML: <input placeholder="******" type="password" id="password" ...>
    private final By passwordInput = By.cssSelector("#password");

    // Step 4: Login submit button
    // HTML: <button type="submit" class="ant-btn ... ant-btn-primary ..."><span>Login</span></button>
    private final By loginButton = By.xpath(
        "//button[@type='submit' and contains(@class,'ant-btn-primary')][.//span[text()='Login']]"
    );

    // Step 5a: OTP input field (Ant Design OTP component — single input that accepts all 4 digits)
    // HTML: <input aria-label="Please enter OTP character 1" ...>
    // Note: Sending all 4 digits to character-1 field works because Ant Design
    //       auto-distributes digits across all OTP character inputs.
    private final By otpFirstChar = By.xpath(
        "//input[@aria-label='Please enter OTP character 1']"
    );

    // Step 6: OTP Submit button
    // HTML: <button type="submit" class="ant-btn ... ant-btn-primary ..." style="margin-top: 20px;"><span>Submit</span></button>
    private final By submitOtpButton = By.xpath(
        "//button[@type='submit' and contains(@class,'ant-btn-primary')][.//span[text()='Submit']]"
    );

    // ── Constructor ─────────────────────────────────────────────────────────

    /**
     * Constructor: receives the driver, sets up explicit wait.
     * @param driver  Active WebDriver instance from DriverFactory
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(
                          Config.getInstance().getExplicitWait()));
    }

    // ── Page Actions ─────────────────────────────────────────────────────────

    /**
     * STEP 1 — Open Application URL
     * Navigates the browser to the DAMS Admin Panel login page.
     */
    public void openLoginPage() {
        String url = Config.getInstance().getBaseUrl();
        driver.get(url);
        ReportManager.logInfo("STEP 1 — Opened URL: " + url);
        System.out.println("[LoginPage] Navigated to: " + url);
    }

    /**
     * STEP 2 — Enter Email
     * Waits for the email field to be visible, then types the email address.
     *
     * @param email  The email to enter (e.g., "07siddwivedi@gmail.com")
     */
    public void enterEmail(String email) {
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailField.clear();
        emailField.sendKeys(email);
        ReportManager.logPass("STEP 2 — Email entered: " + email);
        System.out.println("[LoginPage] Email entered.");
    }

    /**
     * STEP 3 — Enter Password
     * Finds the password field and types the password.
     *
     * @param password  The password to enter (e.g., "Siddharth@123")
     */
    public void enterPassword(String password) {
        WebElement passField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passField.clear();
        passField.sendKeys(password);
        ReportManager.logPass("STEP 3 — Password entered successfully.");
        System.out.println("[LoginPage] Password entered.");
    }

    /**
     * STEP 4 — Click Login Button
     * Waits for the Login button to be clickable, then clicks it.
     * After click, the website sends an OTP to the registered mobile/email.
     */
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        ReportManager.logPass("STEP 4 — Login button clicked.");
        System.out.println("[LoginPage] Login button clicked. Waiting for OTP screen...");
    }

    /**
     * STEP 5 — Enter OTP
     * Waits for the OTP field to appear (after Login button click),
     * then sends all 4 digits to the first character field.
     * Ant Design OTP component auto-fills remaining fields.
     *
     * @param otp  4-digit OTP string (e.g., "1980")
     */
    public void enterOtp(String otp) {
        WebElement otpField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(otpFirstChar));
        otpField.clear();
        otpField.sendKeys(otp);
        ReportManager.logPass("STEP 5 — OTP entered: " + otp);
        System.out.println("[LoginPage] OTP entered: " + otp);
    }

    /**
     * STEP 6 — Click Submit (OTP Verification)
     * Clicks the Submit button to verify the OTP.
     * After success, the dashboard should load.
     */
    public void clickSubmitOtp() {
        wait.until(ExpectedConditions.elementToBeClickable(submitOtpButton)).click();
        ReportManager.logPass("STEP 6 — OTP Submit button clicked.");
        System.out.println("[LoginPage] OTP Submit clicked. Awaiting dashboard...");
    }

    // ── Compound Action: Full Login Flow ─────────────────────────────────────

    /**
     * Performs the complete login flow in sequence:
     *   openLoginPage → enterEmail → enterPassword → clickLogin → enterOtp → clickSubmit
     *
     * Credentials are read from environment variables (GitHub Secrets in CI,
     * or system environment on local machine). Falls back to provided defaults.
     *
     * @param email     Login email address
     * @param password  Login password
     * @param otp       4-digit OTP
     */
    public void performFullLogin(String email, String password, String otp) {
        openLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        enterOtp(otp);
        clickSubmitOtp();
    }

    // ── Verification Helpers ─────────────────────────────────────────────────

    /**
     * Returns the current page URL.
     * Used in tests to verify navigation to the dashboard after login.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Returns the current page title.
     * Used in tests to assert the correct page loaded.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Checks whether the OTP input field is present and visible on screen.
     * Useful for verifying the OTP step appeared after clicking Login.
     */
    public boolean isOtpFieldVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(otpFirstChar));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

