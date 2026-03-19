package com.dams.pages;

import com.dams.base.Base;
import com.dams.core.config.ConfigReader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for the DAMS Login screen.
 *
 * Steps automated:
 *   1. Navigate to https://stageadmin.damsdelhi.com/
 *   2. Enter email in the "Email" field
 *   3. Enter password in the "Password" field
 *   4. Click the Login submit button
 */
public class LoginPage extends Base {

    // ── Locators ──────────────────────────────────────────────────────────────

    /** Step 2: Email input — placeholder="Email" */
    @FindBy(xpath = "//input[@placeholder='Email']")
    private WebElement emailField;

    /** Step 3: Password input — placeholder="******" */
    @FindBy(xpath = "//input[@placeholder='******']")
    private WebElement passwordField;

    /** Step 4: Login submit button — type="submit" */
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    /** Post-login: error message element (if credentials are wrong) */
    @FindBy(xpath = "//*[contains(@class,'error') or contains(@class,'alert') or contains(@class,'invalid')]")
    private WebElement errorMessage;

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Step 1: Opens the DAMS login page.
     */
    public LoginPage openLoginPage() {
        String url = ConfigReader.getInstance().getAppUrl();
        log.info("=== Step 1: Opening URL: {} ===", url);
        navigateTo(url);
        log.info("Page title after navigation: '{}'", getPageTitle());
        return this;
    }

    /**
     * Step 2: Enters the email address.
     */
    public LoginPage enterEmail(String email) {
        log.info("=== Step 2: Entering email: {} ===", email);
        type(emailField, email);
        return this;
    }

    /**
     * Step 3: Enters the password.
     */
    public LoginPage enterPassword(String password) {
        log.info("=== Step 3: Entering password ===");
        type(passwordField, password);
        return this;
    }

    /**
     * Step 4: Clicks the Login button.
     */
    public void clickLogin() {
        log.info("=== Step 4: Clicking Login button ===");
        click(loginButton);
    }

    /**
     * Convenience method — performs all 4 steps in sequence.
     */
    public void performLogin(String email, String password) {
        openLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        log.info("Login flow completed for user: {}", email);
    }

    // ── Verifications ─────────────────────────────────────────────────────────

    /**
     * Returns true if the login page URL or title indicates a successful redirect.
     */
    public boolean isLoginSuccessful() {
        String currentUrl = getCurrentUrl();
        log.info("Post-login URL: {}", currentUrl);
        // Successful login usually redirects away from the login/sign-in path
        return !currentUrl.contains("login") && !currentUrl.contains("signin");
    }

    /**
     * Returns true if an error message is visible (failed login).
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Returns the text of any visible error message.
     */
    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            return errorMessage.getText();
        }
        return "";
    }
}

