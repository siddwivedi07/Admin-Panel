package com.dams.tests.PrintQbank;

import com.dams.base.BaseTest;
import com.dams.pages.PrintQbank;
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
 * TestNG Test Class for Print Qbank Module.
 * Package : com.dams.tests.PrintQbank
 * Suite   : testng.xml → <class name="com.dams.tests.PrintQbank.PrinttQbankTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_03)
 * run in one browser session.
 *
 * Flow:
 *   TC_01 – Click Print Qbank menu link  (/print-qbank)
 *   TC_02 – Click Select Subject dropdown and choose first option
 *   TC_03 – Click Submit button
 */
public class PrintQbankTest extends BaseTest {

    // Primary locator — span.ant-menu-title-content > a[href='/print-qbank']
    private static final By PRINT_QBANK_MENU_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/print-qbank']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By PRINT_QBANK_MENU_HREF = By.xpath(
        "//a[@href='/print-qbank']"
    );

    // Fallback 2 — match sidebar <li> that contains the text "Print Qbank"
    private static final By PRINT_QBANK_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Print Qbank']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text
    private static final By PRINT_QBANK_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Print Qbank' or " +
        "normalize-space(.)='Print Qbank'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Print Qbank – full flow: login → Select Subject → Submit")
    public void printQbankFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[PrintQbankTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PrintQbank", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[PrintQbankTest] Step 0: Waiting for Print Qbank menu link...");
        waitForPrintQbankMenuVisible();
        System.out.println("[PrintQbankTest] Step 0: Dashboard ready ✔");

        PrintQbank page = new PrintQbank(driver);

        // ── TC_01: Click Print Qbank menu link ────────────────────────────────
        page.clickPrintQbankMenu();
        ReportManager.logStep("PrintQbank", "TC_01 – Click Print Qbank Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_print_qbank_menu");

        // ── TC_02: Click Select Subject dropdown and choose first option ───────
        page.selectSubject();
        ReportManager.logStep("PrintQbank", "TC_02 – Select Subject Dropdown", true);
        sleep(2_000);
        takeScreenshot("tc02_select_subject");

        // ── TC_03: Click Submit button ────────────────────────────────────────
        page.clickSubmitButton();
        ReportManager.logStep("PrintQbank", "TC_03 – Click Submit Button", true);
        sleep(3_000);
        takeScreenshot("tc03_submit_button");

        System.out.println("[PrintQbankTest] ✅ printQbankFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the Print Qbank menu link using multiple XPath
     * strategies.
     *
     * After OTP login, Ant Design sidebar menus render asynchronously and may
     * be in a collapsed/icon-only state, making strict XPaths absent from the
     * DOM even after the page appears visually ready.
     *
     * Strategy order:
     *  1. Primary full XPath — up to 60 s
     *  2. href-only           — 10 s  (collapsed or icon-only menu)
     *  3. sidebar <li> text  — 10 s  (alternate Ant Design structure)
     *  4. any ancestor text  — 10 s  (any sidebar/menu ancestor)
     *  5. JS querySelector   — instant (last resort, broadest)
     */
    private void waitForPrintQbankMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(PRINT_QBANK_MENU_PRIMARY));
            System.out.println("[PrintQbankTest] Print Qbank menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[PrintQbankTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(PRINT_QBANK_MENU_HREF));
            System.out.println("[PrintQbankTest] Print Qbank menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[PrintQbankTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(PRINT_QBANK_MENU_ITEM_TEXT));
            System.out.println("[PrintQbankTest] Print Qbank menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[PrintQbankTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(PRINT_QBANK_MENU_ANY_TEXT));
            System.out.println("[PrintQbankTest] Print Qbank menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[PrintQbankTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"print-qbank\"]').length > 0;"
            );
            if (found) {
                System.out.println("[PrintQbankTest] Print Qbank menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[PrintQbankTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[PrintQbankTest] ✘ Print Qbank menu link not found after all fallback strategies.");
        System.err.println("[PrintQbankTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[PrintQbankTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[PrintQbankTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[PrintQbankTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[PrintQbankTest] Print Qbank menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/print-qbank' has not been renamed."
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

            System.out.println("[PrintQbankTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[PrintQbankTest] ✘ Screenshot failed: " + e.getMessage());
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
