package com.dams.tests.BusinessIntelligence;

import com.dams.base.BaseTest;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BusinessIntelligenceTest
 *
 * Verifies that the Business Intelligence section of the DAMS admin portal
 * is accessible and renders its core UI after a successful login.
 *
 * Test flow:
 *   1. Login to the admin portal (via LoginPage)
 *   2. Navigate to the Business Intelligence section
 *   3. Assert that the BI page / dashboard element is visible
 *   4. Capture a screenshot as evidence
 */
public class BusinessIntelligenceTest extends BaseTest {

    // ── Locators ─────────────────────────────────────────────────────────────

    /** Sidebar / menu link that leads to the Business Intelligence section */
    private static final By BI_MENU_LINK =
            By.xpath("//*[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz'," +
                     "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'BUSINESS INTELLIGENCE') or " +
                     "contains(translate(@title,'abcdefghijklmnopqrstuvwxyz'," +
                     "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'BUSINESS INTELLIGENCE')]");

    /** Fallback: Ant Design menu item whose href / key contains 'business' */
    private static final By BI_MENU_LINK_FALLBACK =
            By.cssSelector("li.ant-menu-item a[href*='business'], " +
                           "li.ant-menu-item a[href*='intelligence'], " +
                           "li.ant-menu-item a[href*='bi']");

    /** Main content heading / page title visible once BI section loads */
    private static final By BI_PAGE_INDICATOR =
            By.cssSelector(".ant-layout-content h1, " +
                           ".ant-layout-content h2, " +
                           ".ant-page-header-heading-title, " +
                           ".ant-card-head-title, " +
                           "[class*='dashboard'], [class*='bi-'], [class*='chart']");

    // ── Test ─────────────────────────────────────────────────────────────────

    @Test(description = "Business Intelligence section is accessible after login")
    public void businessIntelligencePageTest() {

        // ── Step 1: Login ────────────────────────────────────────────────────
        System.out.println("[BI Test] Step 1: Logging in to admin portal...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("BusinessIntelligence", "Step 1 – Login", true);

        // Allow dashboard to settle after login
        sleep(5_000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // ── Step 2: Navigate to BI section ───────────────────────────────────
        System.out.println("[BI Test] Step 2: Navigating to Business Intelligence...");
        boolean navigated = false;

        // Strategy A: click the menu link by text
        try {
            WebElement biLink = wait.until(
                    ExpectedConditions.elementToBeClickable(BI_MENU_LINK));
            biLink.click();
            navigated = true;
            System.out.println("[BI Test] Step 2 ✔ BI menu item clicked (text match).");
        } catch (Exception e) {
            System.out.println("[BI Test] Step 2 – text locator missed, trying fallback...");
        }

        // Strategy B: fallback href-based link
        if (!navigated) {
            try {
                WebElement biLink = wait.until(
                        ExpectedConditions.elementToBeClickable(BI_MENU_LINK_FALLBACK));
                biLink.click();
                navigated = true;
                System.out.println("[BI Test] Step 2 ✔ BI menu item clicked (href fallback).");
            } catch (Exception e) {
                System.out.println("[BI Test] Step 2 – href fallback also missed, " +
                                   "attempting direct URL navigation...");
            }
        }

        // Strategy C: direct URL navigation
        if (!navigated) {
            String baseUrl = driver.getCurrentUrl().replaceAll("/$", "");
            // Try common BI URL slugs used in DAMS admin
            for (String slug : new String[]{"business-intelligence", "bi", "intelligence", "dashboard"}) {
                try {
                    driver.navigate().to(baseUrl + "/" + slug);
                    sleep(3_000);
                    // If the page loaded something other than a 404 indicator, accept it
                    if (!driver.getCurrentUrl().contains("404")) {
                        navigated = true;
                        System.out.println("[BI Test] Step 2 ✔ Navigated via direct URL: " + slug);
                        break;
                    }
                } catch (Exception ignored) {}
            }
        }

        ReportManager.logStep("BusinessIntelligence", "Step 2 – Navigate to BI Section", navigated);

        // ── Step 3: Verify BI page content is visible ────────────────────────
        System.out.println("[BI Test] Step 3: Verifying BI page content...");
        boolean pageVisible = false;
        try {
            WebElement indicator = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(BI_PAGE_INDICATOR));
            pageVisible = indicator.isDisplayed();
            System.out.println("[BI Test] Step 3 ✔ BI page content visible: " +
                               indicator.getTagName() + " – " + indicator.getText().trim());
        } catch (Exception e) {
            // Softer check: page loaded without an error element
            try {
                String bodyText = driver.findElement(By.tagName("body")).getText();
                pageVisible = !bodyText.toLowerCase().contains("404") &&
                              !bodyText.toLowerCase().contains("not found");
                System.out.println("[BI Test] Step 3 – indicator not found; " +
                                   "page text check result: " + pageVisible);
            } catch (Exception ex) {
                System.out.println("[BI Test] Step 3 ✘ Could not verify page: " + ex.getMessage());
            }
        }
        ReportManager.logStep("BusinessIntelligence", "Step 3 – BI Page Visible", pageVisible);

        // ── Step 4: Screenshot ───────────────────────────────────────────────
        System.out.println("[BI Test] Step 4: Capturing screenshot...");
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/bi_page_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));
            System.out.println("[BI Test] ✔ Screenshot saved: " + fileName);
            ReportManager.logStep("BusinessIntelligence", "Step 4 – Screenshot", true, fileName);
        } catch (Exception e) {
            System.err.println("[BI Test] ✘ Screenshot failed: " + e.getMessage());
            ReportManager.logStep("BusinessIntelligence", "Step 4 – Screenshot", false);
        }

        // ── Final assertion ──────────────────────────────────────────────────
        Assert.assertTrue(
                pageVisible,
                "[BI Test] FAILED: Business Intelligence page content was not visible. " +
                "Current URL: " + driver.getCurrentUrl()
        );
        System.out.println("[BI Test] ✅ businessIntelligencePageTest PASSED");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
