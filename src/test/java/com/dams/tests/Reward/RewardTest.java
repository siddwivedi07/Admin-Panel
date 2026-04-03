package com.dams.tests.Reward;

import com.dams.base.BaseTest;
import com.dams.pages.RewardPage;
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
 * TestNG Test Class for Reward Management Module.
 * Package : com.dams.tests.Reward
 * Suite   : testng.xml → <class name="com.dams.tests.Reward.RewardTest"/>
 *
 * Single @Test method so all steps (Login + TC_01 … TC_04)
 * run in one browser session.
 *
 * Flow:
 *   TC_01 – Click Reward menu link  (/reward_management)
 *   TC_02 – Click User Points card
 *   TC_03 – Type "Ravi" in Search box + screenshot
 *   TC_04 – Click first View Record button
 */
public class RewardTest extends BaseTest {

    // Primary locator — span.ant-menu-title-content > a[href='/reward_management']
    private static final By REWARD_MENU_LINK_PRIMARY = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/reward_management']"
    );

    // Fallback 1 — match only by href (menu may be collapsed / icon-only)
    private static final By REWARD_MENU_LINK_HREF = By.xpath(
        "//a[@href='/reward_management']"
    );

    // Fallback 2 — match sidebar <li> that contains the text "Reward"
    private static final By REWARD_MENU_ITEM_TEXT = By.xpath(
        "//li[contains(@class,'ant-menu-item')]" +
        "[.//span[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Reward']]"
    );

    // Fallback 3 — any element inside sidebar/menu area with the text "Reward"
    private static final By REWARD_MENU_ANY_TEXT = By.xpath(
        "//*[normalize-space(text())='Reward' or " +
        "normalize-space(.)='Reward'][ancestor::*[contains(@class,'ant-menu') " +
        "or contains(@class,'ant-layout-sider')]]"
    );

    @Test(description = "Reward – full flow: login → User Points → Search Ravi → View Record")
    public void rewardFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[RewardTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("Reward", "Step 0 – Login", true);

        // Allow post-login dashboard to settle
        sleep(5_000);

        // Wait for sidebar to fully load using multiple fallback strategies
        System.out.println("[RewardTest] Step 0: Waiting for Reward menu link...");
        waitForRewardMenuVisible();
        System.out.println("[RewardTest] Step 0: Dashboard ready ✔");

        RewardPage page = new RewardPage(driver);

        // ── TC_01: Click Reward menu link ─────────────────────────────────────
        page.clickRewardMenu();
        ReportManager.logStep("Reward", "TC_01 – Click Reward Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_reward_menu");

        // ── TC_02: Click User Points card ─────────────────────────────────────
        page.clickUserPointsCard();
        ReportManager.logStep("Reward", "TC_02 – Click User Points Card", true);
        sleep(3_000);
        takeScreenshot("tc02_user_points_card");

        // ── TC_03: Type "Ravi" in Search box + screenshot ─────────────────────
        page.searchRavi();
        ReportManager.logStep("Reward", "TC_03 – Search Ravi in Search Box", true);
        sleep(2_000);
        takeScreenshot("tc03_search_ravi");   // screenshot captured after search results load

        // ── TC_04: Click first View Record button ─────────────────────────────
        page.clickViewRecordButton();
        ReportManager.logStep("Reward", "TC_04 – Click View Record Button", true);
        sleep(2_000);
        takeScreenshot("tc04_view_record");

        System.out.println("[RewardTest] ✅ rewardFullFlowTest PASSED");
    }

    // ── Wait helper: tries multiple locator strategies before failing ─────────

    /**
     * Attempts to locate the Reward menu link using multiple XPath strategies.
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
    private void waitForRewardMenuVisible() {
        WebDriverWait longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Strategy 1 — primary locator
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(REWARD_MENU_LINK_PRIMARY));
            System.out.println("[RewardTest] Reward menu found via primary locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RewardTest] Primary locator timed out — trying fallbacks...");
        }

        // Strategy 2 — href-only
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(REWARD_MENU_LINK_HREF));
            System.out.println("[RewardTest] Reward menu found via href-only locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RewardTest] Fallback 1 (href-only) not found.");
        }

        // Strategy 3 — sidebar <li> with matching text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(REWARD_MENU_ITEM_TEXT));
            System.out.println("[RewardTest] Reward menu found via menu-item text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RewardTest] Fallback 2 (menu-item text) not found.");
        }

        // Strategy 4 — any element in sidebar with the text
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(REWARD_MENU_ANY_TEXT));
            System.out.println("[RewardTest] Reward menu found via any-sidebar-text locator ✔");
            return;
        } catch (Exception ignored) {
            System.out.println("[RewardTest] Fallback 3 (any sidebar text) not found.");
        }

        // Strategy 5 — JavaScript querySelector as broadest last resort
        try {
            boolean found = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelectorAll('a[href*=\"reward_management\"]').length > 0;"
            );
            if (found) {
                System.out.println("[RewardTest] Reward menu found via JS querySelector ✔");
                return;
            }
        } catch (Exception ignored) {
            System.out.println("[RewardTest] JS fallback also failed.");
        }

        // All strategies exhausted — dump debug info and fail clearly
        System.err.println("[RewardTest] ✘ Reward menu link not found after all fallback strategies.");
        System.err.println("[RewardTest] Current URL: " + driver.getCurrentUrl());
        System.err.println("[RewardTest] Page title:  " + driver.getTitle());

        try {
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            System.err.println("[RewardTest] All <a> hrefs on page (" + allLinks.size() + "):");
            for (WebElement link : allLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    System.err.println("  " + href);
                }
            }
        } catch (Exception e) {
            System.err.println("[RewardTest] Could not enumerate links: " + e.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[RewardTest] Reward menu link not present in DOM after login. " +
            "All locator strategies exhausted. Check sidebar rendering after OTP login, " +
            "or verify the route '/reward_management' has not been renamed."
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

            System.out.println("[RewardTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[RewardTest] ✘ Screenshot failed: " + e.getMessage());
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
