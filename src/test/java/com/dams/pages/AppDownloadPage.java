package com.dams.pages;

import com.dams.report.ReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class AppDownloadPage {

    // ── Locators ──────────────────────────────────────────────────────────────

    // STEP 1 — App Download menu item in sidebar
    private static final By APP_DOWNLOAD_MENU_ITEM =
            By.cssSelector("a[href='/download-app']");

    // STEP 2 — Member Wise Report card
    private static final By MEMBER_WISE_REPORT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Member Wise Report']/ancestor::div[@class='ant-card-body']");

    // STEP 3 — Download button
    //  HTML: ant-btn-default ant-btn-color-default ant-btn-variant-outlined
    //  with inline style background-color rgb(72,51,254).
    //  ROOT CAUSE OF PREVIOUS FAILURE: locator used 'ant-btn-outlined' as a
    //  single class — the actual class token is 'ant-btn-variant-outlined'.
    //  Fix: match on the span text 'Download' inside any ant-btn — broadest
    //  reliable selector that avoids any class-name fragility.
    private static final By DOWNLOAD_BTN =
            By.xpath("(//button[contains(@class,'ant-btn') and .//span[normalize-space(text())='Download']])[1]");

    // STEP 4 — App Download Report card
    private static final By APP_DOWNLOAD_REPORT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='App Download Report']/ancestor::div[@class='ant-card-body']");

    // STEP 5 — Export button (primary, download icon + 'Export' text)
    //  Note: rendered with disabled="" — use presenceOfElementLocated + JS click
    private static final By EXPORT_BTN =
            By.xpath("//button[.//span[@aria-label='download'] and .//span[normalize-space(text())='Export']]");

    // STEP 6 — Partner Incentive Report card
    private static final By PARTNER_INCENTIVE_REPORT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Partner Incentive Report']/ancestor::div[@class='ant-card-body']");

    // STEP 7 — Today button (primary solid)
    private static final By TODAY_BTN =
            By.xpath("(//button[contains(@class,'ant-btn-primary') and .//span[normalize-space(text())='Today']])[1]");

    // STEP 8 — Weekly button (outlined/default)
    private static final By WEEKLY_BTN =
            By.xpath("(//button[contains(@class,'ant-btn') and .//span[normalize-space(text())='Weekly']])[1]");

    // STEP 9 — Monthly button (outlined/default)
    private static final By MONTHLY_BTN =
            By.xpath("(//button[contains(@class,'ant-btn') and .//span[normalize-space(text())='Monthly']])[1]");

    // STEP 10 — Yearly button (outlined/default)
    private static final By YEARLY_BTN =
            By.xpath("(//button[contains(@class,'ant-btn') and .//span[normalize-space(text())='Yearly']])[1]");

    // STEP 11 — Date Range button (primary solid)
    private static final By DATE_RANGE_BTN =
            By.xpath("(//button[contains(@class,'ant-btn') and .//span[normalize-space(text())='Date Range']])[1]");

    // ── Driver & Wait ─────────────────────────────────────────────────────────
    private final WebDriver     driver;
    private final WebDriverWait wait;

    public AppDownloadPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 — Click App Download menu
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAppDownloadMenu() {
        try {
            WebElement menu = wait.until(
                    ExpectedConditions.elementToBeClickable(APP_DOWNLOAD_MENU_ITEM));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
            System.out.println("[AppDownloadPage] STEP 1 ✔ App Download menu clicked.");
            ReportManager.logStep("App Download", "STEP 1 – Click App Download Menu", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 1 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 1 – Click App Download Menu", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 — Click Member Wise Report card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickMemberWiseReportCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(MEMBER_WISE_REPORT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[AppDownloadPage] STEP 2 ✔ Member Wise Report card clicked.");
            ReportManager.logStep("App Download", "STEP 2 – Click Member Wise Report Card", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 2 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 2 – Click Member Wise Report Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 — Click Download button
    //
    //  ROOT CAUSE FIX: The button class list is:
    //    ant-btn  ant-btn-default  ant-btn-color-default  ant-btn-variant-outlined
    //  Previous locator used 'ant-btn-outlined' (does not exist as a single token).
    //  New locator matches any ant-btn whose span text is 'Download'.
    //  Uses presenceOfElementLocated + JS click to handle any transient
    //  interactability issues after page load.
    // ══════════════════════════════════════════════════════════════════════════
    public void clickDownloadButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(DOWNLOAD_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 3 ✔ Download button clicked.");
            ReportManager.logStep("App Download", "STEP 3 – Click Download Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 3 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 3 – Click Download Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 — Click App Download Report card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAppDownloadReportCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(APP_DOWNLOAD_REPORT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[AppDownloadPage] STEP 4 ✔ App Download Report card clicked.");
            ReportManager.logStep("App Download", "STEP 4 – Click App Download Report Card", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 4 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 4 – Click App Download Report Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 — Click Export button
    //  NOTE: Button may be rendered as disabled=""; JS click bypasses that.
    // ══════════════════════════════════════════════════════════════════════════
    public void clickExportButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(EXPORT_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 5 ✔ Export button clicked.");
            ReportManager.logStep("App Download", "STEP 5 – Click Export Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 5 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 5 – Click Export Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 — Click Partner Incentive Report card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickPartnerIncentiveReportCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(PARTNER_INCENTIVE_REPORT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[AppDownloadPage] STEP 6 ✔ Partner Incentive Report card clicked.");
            ReportManager.logStep("App Download", "STEP 6 – Click Partner Incentive Report Card", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 6 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 6 – Click Partner Incentive Report Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 — Click Today button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickTodayButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(TODAY_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 7 ✔ Today button clicked.");
            ReportManager.logStep("App Download", "STEP 7 – Click Today Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 7 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 7 – Click Today Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 — Click Weekly button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickWeeklyButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(WEEKLY_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 8 ✔ Weekly button clicked.");
            ReportManager.logStep("App Download", "STEP 8 – Click Weekly Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 8 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 8 – Click Weekly Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 — Click Monthly button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickMonthlyButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(MONTHLY_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 9 ✔ Monthly button clicked.");
            ReportManager.logStep("App Download", "STEP 9 – Click Monthly Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 9 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 9 – Click Monthly Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 10 — Click Yearly button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickYearlyButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(YEARLY_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 10 ✔ Yearly button clicked.");
            ReportManager.logStep("App Download", "STEP 10 – Click Yearly Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 10 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 10 – Click Yearly Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 11 — Click Date Range button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickDateRangeButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(DATE_RANGE_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[AppDownloadPage] STEP 11 ✔ Date Range button clicked.");
            ReportManager.logStep("App Download", "STEP 11 – Click Date Range Button", true);
        } catch (Exception e) {
            System.err.println("[AppDownloadPage] STEP 11 ✘ " + e.getMessage());
            ReportManager.logStep("App Download", "STEP 11 – Click Date Range Button", false);
            throw e;
        }
    }
}
