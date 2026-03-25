package com.dams.pages;

import com.dams.base.BasePage;
import org.openqa.selenium.By;

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
 *
 * All locators match the HTML provided in the requirements exactly.
 */
public class LoginPage extends BasePage {

    // ── Locators – Credential form ───────────────────────────────────────────

    /**
     * <input placeholder="Email" type="text" id="email"
     *        aria-required="true" class="ant-input css-tjsggz" />
     */
    private static final By EMAIL_INPUT =
            By.cssSelector("input#email[placeholder='Email']");

    /**
     * <input placeholder="******" type="password" id="password"
     *        aria-required="true" class="ant-input ant-input-lg css-tjsggz" />
     */
    private static final By PASSWORD_INPUT =
            By.cssSelector("input#password[type='password']");

    /**
     * <button type="submit" class="ant-btn … ant-btn-lg ant-btn-block">
     *   <span>Login</span>
     * </button>
     */
    private static final By LOGIN_BUTTON =
            By.cssSelector("button[type='submit'] span:not([style])");

    // ── Locators – OTP form ──────────────────────────────────────────────────

    /**
     * The OTP input field.  Ant Design renders a single <input> inside the
     * OTP step.  We target the first visible text/number input that is NOT
     * the password field (which is already submitted at this point).
     *
     * Fallback: By.cssSelector("input[maxlength]") targets Ant OTP inputs.
     */
    private static final By OTP_INPUT =
            By.cssSelector("input.ant-input[maxlength], input.ant-otp-input, input[type='text']:not(#email)");

    /**
     * <button type="submit" style="margin-top: 20px;" class="ant-btn … ant-btn-lg ant-btn-block">
     *   <span>Submit</span>
     * </button>
     *
     * Distinguished from the Login button by the inline margin-top style.
     */
    private static final By SUBMIT_BUTTON =
            By.cssSelector("button[type='submit'][style*='margin-top']");

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Type the email address into the Email field.
     *
     * @param email e.g. "07siddwivedi@gmail.com"
     */
    public void enterEmail(String email) {
        log.info("Entering email: {}", email);
        type(EMAIL_INPUT, email);
    }

    /**
     * Type the password into the Password field.
     *
     * @param password e.g. "Siddharth@123"
     */
    public void enterPassword(String password) {
        log.info("Entering password: [REDACTED]");
        type(PASSWORD_INPUT, password);
    }

    /**
     * Click the Login button (submits the credential form).
     */
    public void clickLogin() {
        log.info("Clicking Login button");
        // Use XPath to find the button containing the text "Login"
        By loginBtn = By.xpath("//button[@type='submit'][.//span[text()='Login']]");
        click(loginBtn);
    }

    /**
     * Type the OTP into the OTP input that appears after login.
     *
     * @param otp e.g. "1980"
     */
    public void enterOtp(String otp) {
        log.info("Entering OTP: {}", otp);
        type(OTP_INPUT, otp);
    }

    /**
     * Click the Submit button to complete OTP verification.
     */
    public void clickSubmit() {
        log.info("Clicking Submit button");
        // XPath: button[type=submit] with inline margin-top style containing "Submit" span
        By submitBtn = By.xpath(
                "//button[@type='submit'][@style and contains(@style,'margin-top')][.//span[text()='Submit']]"
        );
        click(submitBtn);
    }

    // ── Compound actions ─────────────────────────────────────────────────────

    /**
     * Full Step A: fill credentials and click Login.
     */
    public void performLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Full Step B: enter OTP and click Submit.
     */
    public void performOtpVerification(String otp) {
        enterOtp(otp);
        clickSubmit();
    }

    // ── State helpers ────────────────────────────────────────────────────────

    /** True when the OTP form is visible (used to assert page transition). */
    public boolean isOtpFormVisible() {
        return isDisplayed(OTP_INPUT);
    }

    /** True when the credential form's email field is present. */
    public boolean isLoginFormVisible() {
        return isDisplayed(EMAIL_INPUT);
    }
}
 
