package com.dams.base;

// ══════════════════════════════════════════════════════════════════════════════
//  BaseTest.java  —  DAMS Base Layer  (PARENT CLASS)
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya hai?
//     Sabhi test classes ki PARENT / BASE class.
//     Ye class "extends" karke koi bhi test likhna shuru karo.
//
//  ✅ Kyu zaruri hai?
//     DRY Principle → Code repeat na ho.
//     Browser setup, teardown, login hooks — sab ek jagah.
//
//  ✅ Kya hota hai andar?
//     @BeforeSuite  → Report init, folder setup
//     @BeforeClass  → Class level log
//     @BeforeMethod → Browser launch (DriverManager), test report node
//     navigateToLogin()    → BeforeLogin hook (URL open, page verify)
//     performLogin()       → Login steps 2→3→4→5
//     verifyLoginSuccess() → AfterLogin hook (URL check)
//     @AfterMethod  → Screenshot, report update, browser close
//     @AfterClass   → Class end log
//     @AfterSuite   → Report flush, final summary
// ══════════════════════════════════════════════════════════════════════════════

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.dams.core.ConfigReader;
import com.dams.core.DriverManager;
import com.dams.report.ReportManager;
import com.dams.core.ScreenshotUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    // ──────────────────────────────────────────────────────────────────────────
    //  Protected — child classes seedha access kar sakti hain
    // ──────────────────────────────────────────────────────────────────────────
    protected WebDriver     driver;
    protected WebDriverWait wait;
    protected ExtentTest    extentTest;

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @BeforeSuite — Poore suite ke shuru mein SIRF EK BAAR
    //  Kaam: ExtentReport initialize, output folders banana, header log
    // ══════════════════════════════════════════════════════════════════════════
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log("═══════════════════════════════════════════════════════════");
        log("  🚀  DAMS SELENIUM TEST SUITE  —  STARTING");
        log("  URL     : " + ConfigReader.getUrl());
        log("  User    : " + ConfigReader.getUsername());
        log("  Browser : " + ConfigReader.getBrowser());
        log("  Time    : " + now());
        log("═══════════════════════════════════════════════════════════");

        // Output directories ensure karo
        ensureDir(ConfigReader.getScreenshotPath());
        ensureDir(ConfigReader.getReportPath());
        ensureDir(ConfigReader.getLogPath());

        // ExtentReport initialize karo
        ReportManager.initReport();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @BeforeClass — Har test class shuru hone par
    // ══════════════════════════════════════════════════════════════════════════
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        log("▶ [CLASS START] " + this.getClass().getSimpleName() + "  @  " + now());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @BeforeMethod — Har test method se PEHLE
    //  Kaam: Browser launch karna, WebDriverWait set karna,
    //        ExtentReport mein test node banana
    // ══════════════════════════════════════════════════════════════════════════
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(java.lang.reflect.Method method) {
        String testName = method.getName();
        log("─────────────────────────────────────────────────────────");
        log("▶ [TEST START] " + testName + "  @  " + now());

        // DriverManager se browser initialize karo
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        // Explicit wait setup
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));

        // ExtentReport test node banana
        extentTest = ReportManager.createTest(testName);
        extentTest.log(Status.INFO, "Browser launched: Chrome");
        extentTest.log(Status.INFO, "Test started at: " + now());

        log("[BROWSER] ✔ Driver ready.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ BEFORE LOGIN HOOK — navigateToLogin()
    //  Login se PEHLE call karo — URL open karna, page load verify karna
    //  Step 1 of the login flow
    // ══════════════════════════════════════════════════════════════════════════
    protected void navigateToLogin() {
        String url = ConfigReader.getUrl();
        log("[BEFORE LOGIN] Navigating to: " + url);
        extentTest.log(Status.INFO, "Opening URL: " + url);

        driver.get(url);

        // Login page ready hone ka wait — email field visible ho tab tak
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("input#email[placeholder='Email']")
        ));

        log("[BEFORE LOGIN] ✔ Login page loaded.");
        extentTest.log(Status.INFO, "Login page loaded successfully.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ LOGIN FLOW — performLogin()
    //  Step 2 → Email | Step 3 → Password | Step 4 → Login Button
    //  Step 5 → OTP + Submit
    //  No duplicate code — ek baar likha, sab jagah call karo
    // ══════════════════════════════════════════════════════════════════════════
    protected void performLogin(String email, String password, String otp) {
        log("[LOGIN] ▶ Login flow starting...");

        // ── Step 2: Email input ──────────────────────────────────────────────
        log("[LOGIN] Step 2 → Entering email: " + email);
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input#email[placeholder='Email']")
            )
        );
        clearAndType(emailField, email);
        extentTest.log(Status.INFO, "Step 2: Email entered → " + email);

        // ── Step 3: Password input ───────────────────────────────────────────
        log("[LOGIN] Step 3 → Entering password...");
        WebElement passField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input#password[placeholder='******']")
            )
        );
        clearAndType(passField, password);
        extentTest.log(Status.INFO, "Step 3: Password entered.");

        // ── Step 4: Login button ─────────────────────────────────────────────
        log("[LOGIN] Step 4 → Clicking Login button...");
        clickByText("Login");
        extentTest.log(Status.INFO, "Step 4: Login button clicked.");

        sleep(2000);  // OTP screen animation wait

        // ── Step 5: OTP input ────────────────────────────────────────────────
        log("[LOGIN] Step 5 → Entering OTP: " + otp);
        WebElement otpField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath(
                    "(//input[@type='text' or @type='number' or @type='tel'])" +
                    "[not(@id='email') and not(@id='password')]"
                )
            )
        );
        clearAndType(otpField, otp);
        extentTest.log(Status.INFO, "Step 5: OTP entered → " + otp);

        // ── Step 5 (cont): Submit button ─────────────────────────────────────
        log("[LOGIN] Step 5 → Clicking Submit button...");
        clickByText("Submit");
        extentTest.log(Status.INFO, "Step 5: Submit button clicked.");

        sleep(3000);  // Dashboard redirect wait
        log("[LOGIN] ✔ Login flow completed.");
    }

    // Default credentials se login (config se)
    protected void performLogin() {
        navigateToLogin();
        performLogin(
            ConfigReader.getUsername(),
            ConfigReader.getPassword(),
            ConfigReader.getOtp()
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ AFTER LOGIN HOOK — verifyLoginSuccess()
    //  Login ke BAAD call karo — URL check, dashboard verify
    // ══════════════════════════════════════════════════════════════════════════
    protected void verifyLoginSuccess() {
        String currentUrl   = driver.getCurrentUrl();
        String currentTitle = driver.getTitle();

        log("[AFTER LOGIN] URL   : " + currentUrl);
        log("[AFTER LOGIN] Title : " + currentTitle);

        extentTest.log(Status.INFO, "After Login URL: " + currentUrl);
        extentTest.log(Status.INFO, "Page Title: " + currentTitle);

        boolean isOnLoginPage = currentUrl.trim().equals(ConfigReader.getUrl().trim())
                             || currentUrl.contains("/login");

        if (!isOnLoginPage) {
            log("[AFTER LOGIN] ✔ Login successful — redirected to dashboard.");
            extentTest.log(Status.PASS, "Login verified — dashboard loaded.");
        } else {
            log("[AFTER LOGIN] ⚠ Warning: May still be on login page.");
            extentTest.log(Status.WARNING, "Still on login page after submit.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @AfterMethod — Har test ke BAAD
    //  Kaam: Screenshot, Report update (PASS/FAIL/SKIP), Browser close
    // ══════════════════════════════════════════════════════════════════════════
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        int    status   = result.getStatus();

        // Screenshot lo — PASS ya FAIL dono mein
        String screenshotPath = null;
        if (driver != null) {
            String prefix = (status == ITestResult.SUCCESS) ? "PASSED" : "FAILED";
            screenshotPath = ScreenshotUtil.capture(driver, prefix + "_" + testName);
        }

        // ExtentReport mein result dalo
        if (status == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "✅ Test PASSED: " + testName);
            if (screenshotPath != null) {
                try { extentTest.addScreenCaptureFromPath(screenshotPath, "Pass Screenshot"); }
                catch (Exception ignored) {}
            }
        } else if (status == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, "❌ Test FAILED: " + result.getThrowable().getMessage());
            if (screenshotPath != null) {
                try { extentTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot"); }
                catch (Exception ignored) {}
            }
        } else if (status == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "⏭ Test SKIPPED: " + testName);
        }

        extentTest.log(Status.INFO, "Test ended at: " + now());

        // Browser close karo + memory clean karo
        DriverManager.quitDriver();
        driver = null;
        ReportManager.removeTest();

        log("[TEST END] " + statusLabel(status) + " — " + testName + "  @  " + now());
        log("─────────────────────────────────────────────────────────");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @AfterClass — Har class khatam hone par
    // ══════════════════════════════════════════════════════════════════════════
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        log("■ [CLASS END] " + this.getClass().getSimpleName() + "  @  " + now());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ✅ @AfterSuite — Poore suite ke END mein SIRF EK BAAR
    //  Kaam: ExtentReport flush karna, final summary print karna
    // ══════════════════════════════════════════════════════════════════════════
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Report flush — is ke bina HTML file mein data nahi aayega
        ReportManager.flushReport();

        log("═══════════════════════════════════════════════════════════");
        log("  🏁  DAMS TEST SUITE COMPLETED");
        log("  Report   : " + ConfigReader.getReportPath());
        log("  Screenshots: " + ConfigReader.getScreenshotPath());
        log("  Time     : " + now());
        log("═══════════════════════════════════════════════════════════");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  🛠  PROTECTED UTILITIES — Child classes bhi use kar sakti hain
    // ══════════════════════════════════════════════════════════════════════════

    /** Ant-design button ko uski text se click karna */
    protected void clickByText(String buttonText) {
        WebElement btn = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']//span[normalize-space()='" + buttonText + "']/..")
            )
        );
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    /** Field clear karke value type karna */
    protected void clearAndType(WebElement el, String text) {
        el.clear();
        el.sendKeys(text);
    }

    /** Explicit wait — element visible hone tak */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Explicit wait — element clickable hone tak */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** JavaScript scroll — element screen par aaye */
    protected void scrollTo(WebElement el) {
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", el);
    }

    /** Thread safe sleep */
    protected void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Private helpers
    // ──────────────────────────────────────────────────────────────────────────
    private void ensureDir(String path) {
        File d = new File(path);
        if (!d.exists()) { d.mkdirs(); }
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String statusLabel(int s) {
        return switch (s) {
            case ITestResult.SUCCESS -> "✅ PASSED";
            case ITestResult.FAILURE -> "❌ FAILED";
            case ITestResult.SKIP    -> "⏭ SKIPPED";
            default -> "❓ UNKNOWN";
        };
    }

    protected void log(String msg) { System.out.println(msg); }
}
