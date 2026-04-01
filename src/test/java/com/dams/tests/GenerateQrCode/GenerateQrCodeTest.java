package com.dams.tests.GenerateQrCode;

import com.dams.base.BaseTest;
import com.dams.pages.GenerateQrCodePage;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG Test Class for Generate QR Code Module.
 * Package : com.dams.tests.GenerateQrCode
 * Suite   : testng.xml → <class name="com.dams.tests.GenerateQrCode.GenerateQrCodeTest"/>
 *
 * Single @Test method so that all steps (Login + TC_01 … TC_11)
 * run in one browser session — the same pattern used by PaymentEmiTest.
 */
public class GenerateQrCodeTest extends BaseTest {

    private static final String START_DATE = "2025-01-01";
    private static final String END_DATE   = "2025-03-31";

    @Test(description = "Generate QR Code – full flow: login → Partner QR Report filters → Generate Code CRUD")
    public void generateQrCodeFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        System.out.println("[GenerateQrCodeTest] Step 0: Logging in to admin portal...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("GenerateQrCode", "Step 0 – Login", true);

        sleep(5_000);

        GenerateQrCodePage page = new GenerateQrCodePage(driver);

        // ── TC_01: Click Generate QR Code menu link ───────────────────────────
        page.clickGenerateQrCodeMenu();
        ReportManager.logStep("GenerateQrCode", "TC_01 – Click Generate QR Code Menu", true);
        sleep(2_000);
        takeScreenshot("tc01_generate_qr_code_menu");

        // ── TC_02: Click Partner QR Code Report card ──────────────────────────
        page.clickPartnerQrCodeReportCard();
        ReportManager.logStep("GenerateQrCode", "TC_02 – Click Partner QR Code Report Card", true);
        sleep(2_000);
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
        sleep(2_000);
        takeScreenshot("tc07_date_range_filter");

        // ── TC_08: Navigate back and click Generate Code card ─────────────────
        driver.navigate().back();
        sleep(2_000);
        page.clickGenerateCodeCard();
        ReportManager.logStep("GenerateQrCode", "TC_08 – Click Generate Code Card", true);
        sleep(2_000);
        takeScreenshot("tc08_generate_code_card");

        // ── TC_09: Click first View button (only once) ────────────────────────
        page.clickFirstViewButton();
        ReportManager.logStep("GenerateQrCode", "TC_09 – Click View Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc09_view_button");
        driver.navigate().back();
        sleep(2_000);

        // ── TC_10: Click first Edit button (only once) ────────────────────────
        page.clickFirstEditButton();
        ReportManager.logStep("GenerateQrCode", "TC_10 – Click Edit Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc10_edit_button");
        driver.navigate().back();
        sleep(2_000);

        // ── TC_11: Click first Delete button ─────────────────────────────────
        page.clickFirstDeleteButton();
        ReportManager.logStep("GenerateQrCode", "TC_11 – Click Delete Button (first row)", true);
        sleep(2_000);
        takeScreenshot("tc11_delete_button");

        System.out.println("[GenerateQrCodeTest] ✅ generateQrCodeFullFlowTest PASSED");
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
