package com.dams.tests.Jobs;

import com.dams.base.BaseTest;
import com.dams.pages.JobsPage;
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
 * TestNG Test Class for Jobs Module.
 * Package : com.dams.tests.Jobs
 * Suite   : testng.xml → <class name="com.dams.tests.Jobs.JobsTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_08)
 * run in one browser session.
 *
 * Flow:
 *   TC_01 – Click Jobs menu link  (/jobs)
 *   TC_02 – Click Applied Job card
 *   TC_03 – Search "Ravi" in Applied Job search box + screenshot
 *   TC_04 – Navigate back from Applied Job
 *   TC_05 – Click Job Post card
 *   TC_06 – Search "Dentist" in Job Post search box
 *   TC_07 – Click Add Job button + fill form
 *             (Company=Max Hospital, Title=Dentist, Location=Delhi,
 *              Type=Full Time, Experience=1-3 Yr, Salary Type=Monthly,
 *              Salary Range=30000)
 *   TC_08 – Click Post Job (submit) button
 */
public class JobsTest extends BaseTest {

    // Primary locator — span.ant-menu-title-content > a[href='/jobs']
    private static final By JOBS_MENU_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/jobs']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By JOBS_MENU_HREF = By.xpath(
        "//a[@href='/jobs']"
    );

    // Fallback 2 — match sidebar <li> that contains the text "Jobs"
    private static final By JOBS_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Jobs']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text "Jobs"
    private static final By JOBS_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Jobs' or " +
        "normalize-space(.)='Jobs'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Jobs – full flow: login → Applied Job search → "
            + "Job Post search → Add Job form → Post Job")
    public void jobsFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[JobsTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("Jobs", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[JobsTest] Step 0: Waiting for Jobs menu link...");
        waitForJobsMenuVisible();
        System.out.println("[JobsTest] Step 0: Dashboard ready ✔");

        JobsPage page = new JobsPage(driver);

        // ── TC_01: Click Jobs menu link ───────────────────────────────────────
        page.clickJobsMenu();
        ReportManager.logStep("Jobs", "TC_01 – Click Jobs Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_jobs_menu");

        // ── TC_02: Click Applied Job card ─────────────────────────────────────
        page.clickAppliedJobCard();
        ReportManager.logStep("Jobs", "TC_02 – Click Applied Job Card", true);
        sleep(3_000);
        takeScreenshot("tc02_applied_job_card");

        // ── TC_03: Search "Ravi" in Applied Job search box + screenshot ────────
        page.searchRaviInAppliedJob();
        ReportManager.logStep("Jobs", "TC_03 – Search Ravi in Applied Job", true);
        sleep(2_000);
        takeScreenshot("tc03_search_ravi_applied_job"); // captures loaded search results

        // ── TC_04: Navigate back from Applied Job ─────────────────────────────
        page.navigateBackFromAppliedJob();
        ReportManager.logStep("Jobs", "TC_04 – Navigate Back from Applied Job", true);
        sleep(3_000);
        takeScreenshot("tc04_back_from_applied_job");

        // ── TC_05: Click Job Post card ────────────────────────────────────────
        page.clickJobPostCard();
        ReportManager.logStep("Jobs", "TC_05 – Click Job Post Card", true);
        sleep(3_000);
        takeScreenshot("tc05_job_post_card");

        // ── TC_06: Search "Dentist" in Job Post search box ────────────────────
        page.searchDentistInJobPost();
        ReportManager.logStep("Jobs", "TC_06 – Search Dentist in Job Post", true);
        sleep(2_000);
        takeScreenshot("tc06_search_dentist_job_post");

        // ── TC_07: Click Add Job button + fill entire form ─────────────────────
        // Fills: Company=Max Hospital, Title=Dentist, Location=Delhi,
        //        Type=Full Time, Experience=1 - 3 Yr,
        //        Salary Type=Monthly, Salary Range=30000
        page.clickAddJobAndFillForm();
        ReportManager.logStep("Jobs", "TC_07 – Add Job Form Filled", true);
        sleep(2_000);
        takeScreenshot("tc07_add_job_form_filled");

        // ── TC_08: Click Post Job (submit) button ─────────────────────────────
        page.clickPostJobButton();
        ReportManager.logStep("Jobs", "TC_08 – Click Post Job Button", true);
        sleep(3_000);
        takeScreenshot("tc08_post_job_submitted");

        System.out.println("[JobsTest] ✅ jobsFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the Jobs menu link using multiple XPath strategies.
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
    private void waitForJobsMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(JOBS_MENU_PRIMARY));
            System.out.println("[JobsTest] Jobs menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[JobsTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(JOBS_MENU_HREF));
            System.out.println("[JobsTest] Jobs menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[JobsTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(JOBS_MENU_ITEM_TEXT));
            System.out.println("[JobsTest] Jobs menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[JobsTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(JOBS_MENU_ANY_TEXT));
            System.out.println("[JobsTest] Jobs menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[JobsTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href=\"/jobs\"]').length > 0;"
            );
            if (found) {
                System.out.println("[JobsTest] Jobs menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[JobsTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[JobsTest] ✘ Jobs menu link not found after all fallback strategies.");
        System.err.println("[JobsTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[JobsTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[JobsTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[JobsTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[JobsTest] Jobs menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/jobs' has not been renamed."
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

            System.out.println("[JobsTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[JobsTest] ✘ Screenshot failed: " + e.getMessage());
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
