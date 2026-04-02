package com.dams.tests.CeoReport;

import com.dams.base.BaseTest;
import com.dams.pages.CeoReportPage;
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
 * TestNG Test Class for CEO Report Module.
 * Package : com.dams.tests.CeoReport
 * Suite   : testng.xml → <class name="com.dams.tests.CeoReport.CeoReportTest"/>
 *
 * Single @Test method — all steps (Login + TC_01 … TC_23) run in one session.
 *
 * Corrected navigation flow:
 *   TC_01  – Click CEO Report menu
 *   TC_02  – Click Sales Report Partner card
 *   TC_03  – Today filter  (Sales)
 *   TC_04  – Weekly filter (Sales)
 *   TC_05  – Monthly filter (Sales)
 *   TC_06  – Yearly filter (Sales)
 *   TC_07  – Date Range filter + dates (Sales)
 *   TC_08  – Navigate back from Sales Report Partner
 *   TC_09  – Click Instructor Report card
 *   TC_10  – Navigate back from Instructor Report      ← FIX
 *   TC_11  – Click Team Member Wise Sale card          ← now on CEO Report cards page
 *   TC_12  – Today filter  (Team)
 *   TC_13  – Weekly filter (Team)
 *   TC_14  – Monthly filter (Team)
 *   TC_15  – Yearly filter (Team)
 *   TC_16  – Date Range filter + dates (Team)
 *   TC_17  – Navigate back from Team Member Wise Sale
 *   TC_18  – Click Category Wise Revenue card
 *   TC_19  – Today segmented filter (Category)
 *   TC_20  – Weekly segmented filter (Category)
 *   TC_21  – Monthly segmented filter (Category)
 *   TC_22  – Yearly segmented filter (Category)
 *   TC_23  – Date Range segmented filter + dates (Category)
 */
public class CeoReportTest extends BaseTest {

    // Primary locator — full match with ant-menu-title-content span
    private static final By CEO_MENU_LINK_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/ceo-report']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By CEO_MENU_LINK_HREF = By.xpath(
        "//a[@href='/ceo-report']"
    );

    // Fallback 2 — match sidebar <li> that contains the text
    private static final By CEO_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='CEO Report']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text
    private static final By CEO_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='CEO Report' or " +
        "normalize-space(.)='CEO Report'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "CEO Report – full flow: login → Sales Report Partner → "
            + "Instructor Report → Team Member Wise Sale → Category Wise Revenue")
    public void ceoReportFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[CeoReportTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("CeoReport", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[CeoReportTest] Step 0: Waiting for CEO Report menu link...");
        waitForCeoMenuVisible();
        System.out.println("[CeoReportTest] Step 0: Dashboard ready ✔");

        CeoReportPage page = new CeoReportPage(driver);

        // ── TC_01: Click CEO Report menu link ─────────────────────────────────
        page.clickCeoReportMenu();
        ReportManager.logStep("CeoReport", "TC_01 – Click CEO Report Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_ceo_report_menu");

        // ── TC_02: Click Sales Report Partner card ────────────────────────────
        page.clickSalesReportPartnerCard();
        ReportManager.logStep("CeoReport", "TC_02 – Click Sales Report Partner Card", true);
        sleep(3_000);
        takeScreenshot("tc02_sales_report_partner");

        // ── TC_03: Today filter (Sales Report Partner) ────────────────────────
        page.clickSalesToday();
        ReportManager.logStep("CeoReport", "TC_03 – Today Filter (Sales)", true);
        sleep(2_000);
        takeScreenshot("tc03_sales_today");

        // ── TC_04: Weekly filter (Sales Report Partner) ───────────────────────
        page.clickSalesWeekly();
        ReportManager.logStep("CeoReport", "TC_04 – Weekly Filter (Sales)", true);
        sleep(2_000);
        takeScreenshot("tc04_sales_weekly");

        // ── TC_05: Monthly filter (Sales Report Partner) ──────────────────────
        page.clickSalesMonthly();
        ReportManager.logStep("CeoReport", "TC_05 – Monthly Filter (Sales)", true);
        sleep(2_000);
        takeScreenshot("tc05_sales_monthly");

        // ── TC_06: Yearly filter (Sales Report Partner) ───────────────────────
        page.clickSalesYearly();
        ReportManager.logStep("CeoReport", "TC_06 – Yearly Filter (Sales)", true);
        sleep(2_000);
        takeScreenshot("tc06_sales_yearly");

        // ── TC_07: Date Range filter + fill dates (Sales Report Partner) ──────
        page.clickSalesDateRangeAndFill();
        ReportManager.logStep("CeoReport", "TC_07 – Date Range Filter + Dates (Sales)", true);
        sleep(3_000);
        takeScreenshot("tc07_sales_date_range");

        // ── TC_08: Navigate back from Sales Report Partner ────────────────────
        page.navigateBackFromSalesReport();
        ReportManager.logStep("CeoReport", "TC_08 – Navigate Back from Sales Report Partner", true);
        sleep(3_000);
        takeScreenshot("tc08_back_from_sales");

        // ── TC_09: Click Instructor Report card ───────────────────────────────
        page.clickInstructorReportCard();
        ReportManager.logStep("CeoReport", "TC_09 – Click Instructor Report Card", true);
        sleep(3_000);
        takeScreenshot("tc09_instructor_report");

        // ── TC_10: Navigate back from Instructor Report ───────────────────────
        // FIX: Instructor Report is its own sub-page. Must navigate back to the
        // CEO Report cards page before Team Member Wise Sale card is accessible.
        page.navigateBackFromInstructorReport();
        ReportManager.logStep("CeoReport", "TC_10 – Navigate Back from Instructor Report", true);
        sleep(3_000);
        takeScreenshot("tc10_back_from_instructor");

        // ── TC_11: Click Team Member Wise Sale card ───────────────────────────
        // Now on the CEO Report cards page after navigating back from Instructor Report.
        page.clickTeamMemberWiseSaleCard();
        ReportManager.logStep("CeoReport", "TC_11 – Click Team Member Wise Sale Card", true);
        sleep(3_000);
        takeScreenshot("tc11_team_member_wise_sale");

        // ── TC_12: Today filter (Team Member Wise Sale) ───────────────────────
        page.clickTeamToday();
        ReportManager.logStep("CeoReport", "TC_12 – Today Filter (Team)", true);
        sleep(2_000);
        takeScreenshot("tc12_team_today");

        // ── TC_13: Weekly filter (Team Member Wise Sale) ──────────────────────
        page.clickTeamWeekly();
        ReportManager.logStep("CeoReport", "TC_13 – Weekly Filter (Team)", true);
        sleep(2_000);
        takeScreenshot("tc13_team_weekly");

        // ── TC_14: Monthly filter (Team Member Wise Sale) ─────────────────────
        page.clickTeamMonthly();
        ReportManager.logStep("CeoReport", "TC_14 – Monthly Filter (Team)", true);
        sleep(2_000);
        takeScreenshot("tc14_team_monthly");

        // ── TC_15: Yearly filter (Team Member Wise Sale) ──────────────────────
        page.clickTeamYearly();
        ReportManager.logStep("CeoReport", "TC_15 – Yearly Filter (Team)", true);
        sleep(2_000);
        takeScreenshot("tc15_team_yearly");

        // ── TC_16: Date Range filter + fill dates (Team Member Wise Sale) ─────
        page.clickTeamDateRangeAndFill();
        ReportManager.logStep("CeoReport", "TC_16 – Date Range Filter + Dates (Team)", true);
        sleep(3_000);
        takeScreenshot("tc16_team_date_range");

        // ── TC_17: Navigate back from Team Member Wise Sale ───────────────────
        page.navigateBackFromTeamMemberWiseSale();
        ReportManager.logStep("CeoReport", "TC_17 – Navigate Back from Team Member Wise Sale", true);
        sleep(3_000);
        takeScreenshot("tc17_back_from_team");

        // ── TC_18: Click Category Wise Revenue card ───────────────────────────
        page.clickCategoryWiseRevenueCard();
        ReportManager.logStep("CeoReport", "TC_18 – Click Category Wise Revenue Card", true);
        sleep(3_000);
        takeScreenshot("tc18_category_wise_revenue");

        // ── TC_19: Today segmented filter (Category Wise Revenue) ─────────────
        page.clickCategoryToday();
        ReportManager.logStep("CeoReport", "TC_19 – Today Filter (Category)", true);
        sleep(2_000);
        takeScreenshot("tc19_category_today");

        // ── TC_20: Weekly segmented filter (Category Wise Revenue) ────────────
        page.clickCategoryWeekly();
        ReportManager.logStep("CeoReport", "TC_20 – Weekly Filter (Category)", true);
        sleep(2_000);
        takeScreenshot("tc20_category_weekly");

        // ── TC_21: Monthly segmented filter (Category Wise Revenue) ───────────
        page.clickCategoryMonthly();
        ReportManager.logStep("CeoReport", "TC_21 – Monthly Filter (Category)", true);
        sleep(2_000);
        takeScreenshot("tc21_category_monthly");

        // ── TC_22: Yearly segmented filter (Category Wise Revenue) ────────────
        page.clickCategoryYearly();
        ReportManager.logStep("CeoReport", "TC_22 – Yearly Filter (Category)", true);
        sleep(2_000);
        takeScreenshot("tc22_category_yearly");

        // ── TC_23: Date Range segmented filter + fill dates (Category) ────────
        page.clickCategoryDateRangeAndFill();
        ReportManager.logStep("CeoReport", "TC_23 – Date Range Filter + Dates (Category)", true);
        sleep(3_000);
        takeScreenshot("tc23_category_date_range");

        System.out.println("[CeoReportTest] ✅ ceoReportFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the CEO Report menu link using multiple XPath strategies.
     *
     * After OTP login, Ant Design sidebar menus render asynchronously and may
     * be in a collapsed/icon-only state, making strict XPaths absent from the
     * DOM even after the page appears visually ready.
     */
    private void waitForCeoMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(CEO_MENU_LINK_PRIMARY));
            System.out.println("[CeoReportTest] CEO menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[CeoReportTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(CEO_MENU_LINK_HREF));
            System.out.println("[CeoReportTest] CEO menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[CeoReportTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(CEO_MENU_ITEM_TEXT));
            System.out.println("[CeoReportTest] CEO menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[CeoReportTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(CEO_MENU_ANY_TEXT));
            System.out.println("[CeoReportTest] CEO menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[CeoReportTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"ceo-report\"]').length > 0;"
            );
            if (found) {
                System.out.println("[CeoReportTest] CEO menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[CeoReportTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[CeoReportTest] ✘ CEO Report menu link not found after all fallback strategies.");
        System.err.println("[CeoReportTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[CeoReportTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[CeoReportTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[CeoReportTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[CeoReportTest] CEO Report menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/ceo-report' has not been renamed."
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

            System.out.println("[CeoReportTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[CeoReportTest] ✘ Screenshot failed: " + e.getMessage());
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
