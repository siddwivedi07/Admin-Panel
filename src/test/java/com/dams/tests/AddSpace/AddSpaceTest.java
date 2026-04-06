package com.dams.tests.AddSpace;

import com.dams.base.BaseTest;
import com.dams.pages.AddSpacePage;
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
 * TestNG Test Class for Add Space Module.
 * Package : com.dams.tests.AddSpace
 * Suite   : testng.xml → <class name="com.dams.tests.AddSpace.AddSpaceTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_12)
 * run in one browser session.
 *
 * Flow:
 *   TC_01 – Click Add Space menu link  (/add-space)
 *   TC_02 – Click Adspace page setting card
 *   TC_03 – Search "home page" in Search by page name box
 *   TC_04 – Navigate back from Adspace page setting
 *   TC_05 – Click Add Vendor card
 *   TC_06 – Click Add Vendor button (opens popup)
 *   TC_07 – Fill vendor form (Name / Email / Mobile) → submit
 *   TC_08 – Search "Aadityasharma" in search by name/email box
 *   TC_09 – Navigate back from Add Vendor
 *   TC_10 – Click Add Campaign card
 *   TC_11 – Search "Amazon" in search vendor/campaign box
 *   TC_12 – Click Add Campaign button → fill popup form → Create Campaign
 */
public class AddSpaceTest extends BaseTest {

    // Primary locator — span.ant-menu-title-content > a[href='/add-space']
    private static final By ADD_SPACE_MENU_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/add-space']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By ADD_SPACE_MENU_HREF = By.xpath(
        "//a[@href='/add-space']"
    );

    // Fallback 2 — match sidebar <li> that contains the text "Add Space"
    private static final By ADD_SPACE_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Add Space']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text "Add Space"
    private static final By ADD_SPACE_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Add Space' or " +
        "normalize-space(.)='Add Space'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Add Space – full flow: login → Adspace page setting → "
            + "Add Vendor → Add Campaign → Create Campaign")
    public void addSpaceFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[AddSpaceTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("AddSpace", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[AddSpaceTest] Step 0: Waiting for Add Space menu link...");
        waitForAddSpaceMenuVisible();
        System.out.println("[AddSpaceTest] Step 0: Dashboard ready ✔");

        AddSpacePage page = new AddSpacePage(driver);

        // ── TC_01: Click Add Space menu link ──────────────────────────────────
        page.clickAddSpaceMenu();
        ReportManager.logStep("AddSpace", "TC_01 – Click Add Space Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_add_space_menu");

        // ── TC_02: Click Adspace page setting card ────────────────────────────
        page.clickAdspacePageSettingCard();
        ReportManager.logStep("AddSpace", "TC_02 – Click Adspace Page Setting Card", true);
        sleep(3_000);
        takeScreenshot("tc02_adspace_page_setting_card");

        // ── TC_03: Search "home page" in Search by page name box ──────────────
        page.searchByPageName();
        ReportManager.logStep("AddSpace", "TC_03 – Search Home Page in Page Name Box", true);
        sleep(2_000);
        takeScreenshot("tc03_search_home_page");

        // ── TC_04: Navigate back from Adspace page setting ────────────────────
        page.navigateBackFromAdspacePageSetting();
        ReportManager.logStep("AddSpace", "TC_04 – Navigate Back from Adspace Page Setting", true);
        sleep(3_000);
        takeScreenshot("tc04_back_from_adspace_page_setting");

        // ── TC_05: Click Add Vendor card ──────────────────────────────────────
        page.clickAddVendorCard();
        ReportManager.logStep("AddSpace", "TC_05 – Click Add Vendor Card", true);
        sleep(3_000);
        takeScreenshot("tc05_add_vendor_card");

        // ── TC_06: Click Add Vendor button (opens popup) ──────────────────────
        page.clickAddVendorButton();
        ReportManager.logStep("AddSpace", "TC_06 – Click Add Vendor Button", true);
        sleep(2_000);
        takeScreenshot("tc06_add_vendor_popup");

        // ── TC_07: Fill vendor form (Name / Email / Mobile) → submit ──────────
        // Name   : Aaditya sharma
        // Email  : 06aadityasharma@gmail.com
        // Mobile : 6387626349
        page.fillVendorFormAndSubmit();
        ReportManager.logStep("AddSpace", "TC_07 – Fill Vendor Form and Submit", true);
        sleep(2_000);
        takeScreenshot("tc07_vendor_form_submitted");

        // ── TC_08: Search "Aadityasharma" in search by name/email box ─────────
        page.searchVendorByName();
        ReportManager.logStep("AddSpace", "TC_08 – Search Aadityasharma in Vendor Search Box", true);
        sleep(2_000);
        takeScreenshot("tc08_search_vendor_by_name");

        // ── TC_09: Navigate back from Add Vendor ──────────────────────────────
        page.navigateBackFromAddVendor();
        ReportManager.logStep("AddSpace", "TC_09 – Navigate Back from Add Vendor", true);
        sleep(3_000);
        takeScreenshot("tc09_back_from_add_vendor");

        // ── TC_10: Click Add Campaign card ────────────────────────────────────
        page.clickAddCampaignCard();
        ReportManager.logStep("AddSpace", "TC_10 – Click Add Campaign Card", true);
        sleep(3_000);
        takeScreenshot("tc10_add_campaign_card");

        // ── TC_11: Search "Amazon" in search vendor/campaign box ──────────────
        page.searchAmazonInCampaign();
        ReportManager.logStep("AddSpace", "TC_11 – Search Amazon in Campaign Search Box", true);
        sleep(2_000);
        takeScreenshot("tc11_search_amazon_campaign");

        // ── TC_12: Click Add Campaign button → fill popup form → Create Campaign
        // Vendor      : Amazon
        // Campaign    : siddharth
        // Start Date  : today (auto-generated)
        // Start Time  : random (auto-generated)
        // End Date    : today + 7 days (auto-generated)
        // End Time    : random (auto-generated)
        // Placement   : Home Screen
        page.clickAddCampaignAndFillForm();
        ReportManager.logStep("AddSpace", "TC_12 – Add Campaign Form Filled and Created", true);
        sleep(3_000);
        takeScreenshot("tc12_create_campaign_submitted");

        System.out.println("[AddSpaceTest] ✅ addSpaceFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the Add Space menu link using multiple XPath strategies.
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
    private void waitForAddSpaceMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(ADD_SPACE_MENU_PRIMARY));
            System.out.println("[AddSpaceTest] Add Space menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[AddSpaceTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(ADD_SPACE_MENU_HREF));
            System.out.println("[AddSpaceTest] Add Space menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[AddSpaceTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(ADD_SPACE_MENU_ITEM_TEXT));
            System.out.println("[AddSpaceTest] Add Space menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[AddSpaceTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(ADD_SPACE_MENU_ANY_TEXT));
            System.out.println("[AddSpaceTest] Add Space menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[AddSpaceTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"add-space\"]').length > 0;"
            );
            if (found) {
                System.out.println("[AddSpaceTest] Add Space menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[AddSpaceTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[AddSpaceTest] ✘ Add Space menu link not found after all fallback strategies.");
        System.err.println("[AddSpaceTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[AddSpaceTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[AddSpaceTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[AddSpaceTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[AddSpaceTest] Add Space menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/add-space' has not been renamed."
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

            System.out.println("[AddSpaceTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[AddSpaceTest] ✘ Screenshot failed: " + e.getMessage());
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
