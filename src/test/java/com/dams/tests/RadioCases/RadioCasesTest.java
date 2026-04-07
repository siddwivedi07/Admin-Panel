package com.dams.tests.RadioCases;

import com.dams.base.BaseTest;
import com.dams.pages.RadioCasesPage;
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
 * TestNG Test Class for Radio Cases Module.
 * Package : com.dams.tests.RadioCases
 * Suite   : testng.xml → <class name="com.dams.tests.RadioCases.RadioCasesTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_16)
 * run in one browser session.
 *
 * Flow:
 *   TC_01  – Click Radio Cases menu  (/radio-cases)
 *   TC_02  – Click Radio Modality List card
 *   TC_03  – Search "Pradeep" in Modality search box
 *   TC_04  – Click Add Modality button (opens popup)
 *   TC_05  – Fill Modality Name "Pradeep" → Submit
 *   TC_06  – Navigate back from Radio Modality List
 *   TC_07  – Click Radio Category List card
 *   TC_08  – Search "88" in Category search box
 *   TC_09  – Click Edit → update name to "MentalHealthIssues" → Update
 *   TC_10  – Click Delete button (first row)
 *   TC_11  – Click Add Category → fill "MentalHealthIssues" → Submit
 *   TC_12  – Navigate back from Radio Category List
 *   TC_13  – Click Add Case card
 *   TC_14  – Search "65" in Case search box
 *   TC_15  – Click Add button (opens Add Case form)
 *   TC_16  – Fill Add Case form → Save Case
 */
public class RadioCasesTest extends BaseTest {

    // Primary locator — span.ant-menu-title-content > a[href='/radio-cases']
    private static final By RADIO_CASES_MENU_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/radio-cases']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By RADIO_CASES_MENU_HREF = By.xpath(
        "//a[@href='/radio-cases']"
    );

    // Fallback 2 — match sidebar <li> that contains the text "Radio Cases"
    private static final By RADIO_CASES_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Radio Cases']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text
    private static final By RADIO_CASES_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Radio Cases' or " +
        "normalize-space(.)='Radio Cases'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Radio Cases – full flow: login → Modality List → "
            + "Category List → Add Case form → Save Case")
    public void radioCasesFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[RadioCasesTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("RadioCases", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[RadioCasesTest] Step 0: Waiting for Radio Cases menu link...");
        waitForRadioCasesMenuVisible();
        System.out.println("[RadioCasesTest] Step 0: Dashboard ready ✔");

        RadioCasesPage page = new RadioCasesPage(driver);

        // ── TC_01: Click Radio Cases menu link ────────────────────────────────
        page.clickRadioCasesMenu();
        ReportManager.logStep("RadioCases", "TC_01 – Click Radio Cases Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_radio_cases_menu");

        // ── TC_02: Click Radio Modality List card ─────────────────────────────
        page.clickRadioModalityListCard();
        ReportManager.logStep("RadioCases", "TC_02 – Click Radio Modality List Card", true);
        sleep(3_000);
        takeScreenshot("tc02_radio_modality_list_card");

        // ── TC_03: Search "Pradeep" in Modality search box ────────────────────
        page.searchModality();
        ReportManager.logStep("RadioCases", "TC_03 – Search Pradeep in Modality Search", true);
        sleep(2_000);
        takeScreenshot("tc03_search_modality_pradeep");

        // ── TC_04: Click Add Modality button (opens popup) ────────────────────
        page.clickAddModalityButton();
        ReportManager.logStep("RadioCases", "TC_04 – Click Add Modality Button", true);
        sleep(2_000);
        takeScreenshot("tc04_add_modality_popup");

        // ── TC_05: Fill Modality Name "Pradeep" → Submit ──────────────────────
        page.fillModalityNameAndSubmit();
        ReportManager.logStep("RadioCases", "TC_05 – Fill Modality Name and Submit", true);
        sleep(2_000);
        takeScreenshot("tc05_modality_submitted");

        // ── TC_06: Navigate back from Radio Modality List ─────────────────────
        page.navigateBackFromModalityList();
        ReportManager.logStep("RadioCases", "TC_06 – Navigate Back from Radio Modality List", true);
        sleep(3_000);
        takeScreenshot("tc06_back_from_modality_list");

        // ── TC_07: Click Radio Category List card ─────────────────────────────
        page.clickRadioCategoryListCard();
        ReportManager.logStep("RadioCases", "TC_07 – Click Radio Category List Card", true);
        sleep(3_000);
        takeScreenshot("tc07_radio_category_list_card");

        // ── TC_08: Search "88" in Category search box ─────────────────────────
        page.searchCategory();
        ReportManager.logStep("RadioCases", "TC_08 – Search 88 in Category Search", true);
        sleep(2_000);
        takeScreenshot("tc08_search_category_88");

        // ── TC_09: Click Edit → update name to "MentalHealthIssues" → Update ──
        page.clickEditAndUpdate();
        ReportManager.logStep("RadioCases", "TC_09 – Edit Category and Update", true);
        sleep(2_000);
        takeScreenshot("tc09_category_updated");

        // ── TC_10: Click Delete button (first row) ────────────────────────────
        page.clickDeleteButton();
        ReportManager.logStep("RadioCases", "TC_10 – Click Delete Button", true);
        sleep(2_000);
        takeScreenshot("tc10_category_deleted");

        // ── TC_11: Click Add Category → fill "MentalHealthIssues" → Submit ────
        page.clickAddCategoryAndSubmit();
        ReportManager.logStep("RadioCases", "TC_11 – Add Category MentalHealthIssues", true);
        sleep(2_000);
        takeScreenshot("tc11_category_added");

        // ── TC_12: Navigate back from Radio Category List ─────────────────────
        page.navigateBackFromCategoryList();
        ReportManager.logStep("RadioCases", "TC_12 – Navigate Back from Radio Category List", true);
        sleep(3_000);
        takeScreenshot("tc12_back_from_category_list");

        // ── TC_13: Click Add Case card ────────────────────────────────────────
        page.clickAddCaseCard();
        ReportManager.logStep("RadioCases", "TC_13 – Click Add Case Card", true);
        sleep(3_000);
        takeScreenshot("tc13_add_case_card");

        // ── TC_14: Search "65" in Case search box ─────────────────────────────
        page.searchCase();
        ReportManager.logStep("RadioCases", "TC_14 – Search Case 65", true);
        sleep(2_000);
        takeScreenshot("tc14_search_case_65");

        // ── TC_15: Click Add button (opens Add Case form) ─────────────────────
        page.clickAddButton();
        ReportManager.logStep("RadioCases", "TC_15 – Click Add Button", true);
        sleep(3_000);
        takeScreenshot("tc15_add_case_form_opened");

        // ── TC_16: Fill Add Case form → Save Case ─────────────────────────────
        // Case Title   : Acute Necrotizing Encephalopathy in a Young Adult
        // Modality     : Pradeep
        // Body Part    : testng body
        // Difficulty   : Beginner
        // Category     : MentalHealthIssues
        // Pacsbin URL  : https://www.pacsbin.com/c/ZkhD47HHtn
        // Diagnosis    : It is correct
        // Key Findings : it is correct
        page.fillAddCaseFormAndSave();
        ReportManager.logStep("RadioCases", "TC_16 – Fill Add Case Form and Save", true);
        sleep(3_000);
        takeScreenshot("tc16_case_saved");

        System.out.println("[RadioCasesTest] ✅ radioCasesFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the Radio Cases menu link using multiple XPath
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
    private void waitForRadioCasesMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(RADIO_CASES_MENU_PRIMARY));
            System.out.println("[RadioCasesTest] Radio Cases menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RadioCasesTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(RADIO_CASES_MENU_HREF));
            System.out.println("[RadioCasesTest] Radio Cases menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RadioCasesTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(RADIO_CASES_MENU_ITEM_TEXT));
            System.out.println("[RadioCasesTest] Radio Cases menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RadioCasesTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(RADIO_CASES_MENU_ANY_TEXT));
            System.out.println("[RadioCasesTest] Radio Cases menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RadioCasesTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"radio-cases\"]').length > 0;"
            );
            if (found) {
                System.out.println("[RadioCasesTest] Radio Cases menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[RadioCasesTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[RadioCasesTest] ✘ Radio Cases menu not found after all fallback strategies.");
        System.err.println("[RadioCasesTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[RadioCasesTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[RadioCasesTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[RadioCasesTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[RadioCasesTest] Radio Cases menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/radio-cases' has not been renamed."
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

            System.out.println("[RadioCasesTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[RadioCasesTest] ✘ Screenshot failed: " + e.getMessage());
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
