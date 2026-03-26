package com.dams.pages;

// ══════════════════════════════════════════════════════════════════════════════
//  LoginPage.java  —  DAMS Pages Layer  (Page Object Model)
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya hai?
//     Login page ke saare elements aur actions ek jagah.
//     Page Object Model (POM) design pattern.
//
//  ✅ Kyu zaruri hai?
//     UI change ho (element ka ID/class badal jaye) →
//     SIRF YAHAN CHANGE KARO, test file mein kuch nahi badlega.
//
//  ✅ Kya hota hai?
//     @FindBy — WebElements (locators)
//     Methods  — Actions (click, type, etc.)
//
//  ✅ Usage:
//     LoginPage lp = new LoginPage(driver);
//     lp.enterEmail("test@test.com");
//     lp.clickLogin();
// ══════════════════════════════════════════════════════════════════════════════

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ──────────────────────────────────────────────────────────────────────────
    //  @FindBy — Page Elements (locators)
    //  Agar UI badal jaye to SIRF YAHAN locator update karo
    // ──────────────────────────────────────────────────────────────────────────

    // Step 2: Email input field
    @FindBy(css = "input#email[placeholder='Email']")
    private WebElement emailInput;

    // Step 3: Password input field
    @FindBy(css = "input#password[placeholder='******']")
    private WebElement passwordInput;

    // Step 4: Login button
    @FindBy(xpath = "//button[@type='submit']//span[normalize-space()='Login']/..")
    private WebElement loginButton;

    // Step 5: OTP input field
    @FindBy(xpath = "(//input[@type='text' or @type='number' or @type='tel'])[not(@id='email') and not(@id='password')]")
    private WebElement otpInput;

    // Step 5: Submit button (OTP submit)
    @FindBy(xpath = "//button[@type='submit']//span[normalize-space()='Submit']/..")
    private WebElement submitButton;

    // Error message (negative test mein use hoga)
    @FindBy(css = ".ant-form-item-explain-error, .ant-alert-message, [class*='error']")
    private WebElement errorMessage;

    // ──────────────────────────────────────────────────────────────────────────
    //  Constructor — PageFactory initialize karta hai
    // ──────────────────────────────────────────────────────────────────────────
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
        // PageFactory @FindBy elements ko initialize karta hai
        PageFactory.initElements(driver, this);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Actions — Page par jo karna hai
    // ──────────────────────────────────────────────────────────────────────────

    /** Step 2: Email field mein email type karo */
    public LoginPage enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
        System.out.println("[LoginPage] Email entered: " + email);
        return this;  // Method chaining support
    }

    /** Step 3: Password field mein password type karo */
    public LoginPage enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        System.out.println("[LoginPage] Password entered.");
        return this;
    }

    /** Step 4: Login button click karo */
    public LoginPage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        System.out.println("[LoginPage] Login button clicked.");
        return this;
    }

    /** Step 5: OTP field mein OTP type karo */
    public LoginPage enterOtp(String otp) {
        wait.until(ExpectedConditions.visibilityOf(otpInput));
        otpInput.clear();
        otpInput.sendKeys(otp);
        System.out.println("[LoginPage] OTP entered: " + otp);
        return this;
    }

    /** Step 5: Submit button click karo */
    public LoginPage clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        System.out.println("[LoginPage] Submit button clicked.");
        return this;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Composite Actions — Poora login ek method mein (method chaining)
    // ──────────────────────────────────────────────────────────────────────────

    /** Steps 2+3+4: Email, password enter karke login click */
    public LoginPage fillAndSubmitLoginForm(String email, String password) {
        return enterEmail(email)
                .enterPassword(password)
                .clickLogin();
    }

    /** Step 5: OTP enter karke submit */
    public void submitOtp(String otp) {
        enterOtp(otp).clickSubmit();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Getters — Test assertions ke liye
    // ──────────────────────────────────────────────────────────────────────────

    /** Error message text lo (negative tests ke liye) */
    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /** Check karo — OTP screen show ho raha hai ya nahi */
    public boolean isOtpScreenVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(otpInput));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Current page title lo */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /** Current URL lo */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
