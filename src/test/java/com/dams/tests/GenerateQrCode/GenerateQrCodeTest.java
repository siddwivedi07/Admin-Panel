package com.dams.tests.GenerateQrCode;

import com.dams.base.BaseTest;
import com.dams.pages.GenerateQrCodePage;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TestNG Test Class for Generate QR Code Module.
 * Package : com.dams.tests.GenerateQrCode
 * Suite   : testng.xml → <class name="com.dams.tests.GenerateQrCode.GenerateQrCodeTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_11)
 * run in one browser session — same pattern as PaymentEmiTest.
 */
public class GenerateQrCodeTest extends BaseTest {

    private static final String START_DATE = "2025-01-01";
    private static final String END_DATE   = "2025-03-31";

    // Primary locator — full match with ant-menu-title-content span
    private static final By QR_MENU_LINK_PRIMARY = By.xpath(
        "//a[@href='/qrcode-generate']" +
        "[.//*[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Generate QR Code']]"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By QR_MENU_LINK_HREF = By.xpath(
        "//a[@href='/qrcode-generate']"
    );

    // Fallback 2 — match sidebar <li> that contains the text
    private static final By QR_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Generate QR Code']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text
    private static final By QR_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Generate QR Code' or " +
        "normalize-space(.)='Generate QR Code'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Generate QR Code – full flow: login → Partner QR Report filters → Generate Code CRUD")
    public void generateQrCodeFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[GenerateQrCodeTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("GenerateQrCode", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for the sidebar/menu to fully load using multiple fallback strategies
        System.out.println("[GenerateQrCodeTest] Step 0: Waiting for QR Code menu link...");
        waitForQrMenuVisible();
        System.out.println("[GenerateQrCodeTest] Step 0: Dashboard ready ✔");

        GenerateQrCodePage page = new GenerateQrCodePage(driver);

        // ── TC_01: Click Generate QR Code menu link ───────────────────────────
        page.clickGenerateQrCodeMenu();
        ReportManager.logStep("GenerateQrCode", "TC_01 – Click Generate QR Code Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_generate_qr_code_menu");

        // ── TC_02: Click Partner QR Code Report card ──────────────────────────
        page.clickPartnerQrCodeReportCard();
        ReportManager.logStep("GenerateQrCode", "TC_02 – Click Partner QR Code Report Card", true);
        sleep(3_000);
        takeScreenshot("tc02_partner_qr_code_report");

        // ── TC_03: Click Today button ─────────────────────────────────────────
        page.clickToday();
        ReportManager.logStep("GenerateQrCode", "TC_03 – Click Today Filter", true);
        sleep(2_000);
        takeScreenshot("tc03_today_filter");

        // ── TC_04: Click Weekly button ────────────────────────────────────────
        page.clickWeekly();
        ReportManager.logStep("GenerateQrCode", "TC_04 – Click Weekly Filter", true);
        sleep(2_000);
        takeScreenshot("tc04_weekly_filter");

        // ── TC_05: Click Monthly button ───────────────────────────────────────
        page.clickMonthly();
        ReportManager.logStep("GenerateQrCode", "TC_05 – Click Monthly Filter", true);
        sleep(2_000);
        takeScreenshot("tc05_monthly_filter");

        // ── TC_06: Click Yearly button ────────────────────────────────────────
        page.clickYearly();
        ReportManager.logStep("GenerateQrCode", "TC_06 – Click Yearly Filter", true);
        sleep(2_000);
        takeScreenshot("tc06_yearly_filter");

        // ── TC_07: Click Date Range and fill dates ────────────────────────────
        page.clickDateRangeAndFill(START_DATE, END_DATE);
        ReportManager.logStep("GenerateQrCode", "TC_07 – Click Date Range and Fill Dates", true);
        sleep(3_000);
        takeScreenshot("tc07_date_range_filter");

        // ── TC_08: Navigate back and click Generate Code card ─────────────────
        // navigate().back() returns to the QR Code cards page (/qrcode-generate)
        driver.navigate().back();
        sleep(3_000);
        page.clickGenerateCodeCard();
        ReportManager.logStep("GenerateQrCode", "TC_08 – Click Generate Code Card", true);
        sleep(3_000);
        takeScreenshot("tc08_generate_code_card");

        // ── TC_09: Click first View button (only once) ────────────────────────
        page.clickFirstViewButton();
        ReportManager.logStep("GenerateQrCode", "TC_09 – Click View Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc09_view_button");

        // FIX: navigate().back() after View returns to the CARD page, not the table.
        // We must re-click the Generate Code card to get back to the table.
        driver.navigate().back();
        sleep(3_000);
        System.out.println("[GenerateQrCodeTest] TC_09 → Navigated back — re-entering Generate Code table for TC_10...");
        page.clickGenerateCodeCard();
        sleep(3_000);

        // ── TC_10: Click first Edit button (only once) ────────────────────────
        page.clickFirstEditButton();
        ReportManager.logStep("GenerateQrCode", "TC_10 – Click Edit Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc10_edit_button");

        // FIX: same as TC_09 — navigate().back() returns to card page, re-enter table.
        driver.navigate().back();
        sleep(3_000);
        System.out.println("[GenerateQrCodeTest] TC_10 → Navigated back — re-entering Generate Code table for TC_11...");
        page.clickGenerateCodeCard();
        sleep(3_000);

        // ── TC_11: Click first Delete button ─────────────────────────────────
        page.clickFirstDeleteButton();
        ReportManager.logStep("GenerateQrCode", "TC_11 – Click Delete Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc11_delete_button");

        System.out.println("[GenerateQrCodeTest] ✅ generateQrCodeFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the QR Code menu link using multiple XPath strategies.
     *
     * Root cause of the original timeout: after OTP login, Ant Design sidebar
     * menus render asynchronously and may initially be in a collapsed/icon-only
     * state, making the strict primary XPath (href + ant-menu-title-content)
     * absent from the DOM even after the page appears visually ready.
     */
    private void waitForQrMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(QR_MENU_LINK_PRIMARY));
            System.out.println("[GenerateQrCodeTest] QR menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodeTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only (menu collapsed or icon state)
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(QR_MENU_LINK_HREF));
            System.out.println("[GenerateQrCodeTest] QR menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodeTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(QR_MENU_ITEM_TEXT));
            System.out.println("[GenerateQrCodeTest] QR menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodeTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(QR_MENU_ANY_TEXT));
            System.out.println("[GenerateQrCodeTest] QR menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodeTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"qrcode-generate\"]').length > 0;"
            );
            if (found) {
                System.out.println("[GenerateQrCodeTest] QR menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodeTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[GenerateQrCodeTest] ✘ QR Code menu link not found after all fallback strategies.");
        System.err.println("[GenerateQrCodeTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[GenerateQrCodeTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[GenerateQrCodeTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[GenerateQrCodeTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[GenerateQrCodeTest] QR Code menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/qrcode-generate' has not been renamed."
        );
    }

    // ── Screenshot helper ─────────────────────────────────────────────────────

    private void takeScreenshot(String testName) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));

            System.out.println("[GenerateQrCodeTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[GenerateQrCodeTest] ✘ Screenshot failed: " + e.getMessage());
        }
    }

    // ── Sleep helper ──────────────────────────────────────────────────────────

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
