package com.dams.tests.AppDownload;

import com.dams.base.BaseTest;
import com.dams.pages.AppDownloadPage;
import com.dams.pages.LoginPage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppDownloadTest extends BaseTest {

    @Test(description = "Login to admin portal then navigate through App Download flows: "
            + "Member Wise Report → App Download Report → Partner Incentive Report")
    public void appDownloadTest() {

        // ── Login Flow ────────────────────────────────────────────────────────
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();

        // ── App Download Page Flow ────────────────────────────────────────────
        AppDownloadPage appDownload = new AppDownloadPage(driver);

        // ════════════════════════════════════════════════════════════════
        //  FLOW 1 — Member Wise Report
        // ════════════════════════════════════════════════════════════════

        // STEP 1 — Click App Download menu
        System.out.println("[AppDownloadTest] STEP 1 – Clicking App Download menu...");
        appDownload.clickAppDownloadMenu();
        sleep(3_000);

        // STEP 2 — Click Member Wise Report card
        System.out.println("[AppDownloadTest] STEP 2 – Clicking Member Wise Report card...");
        appDownload.clickMemberWiseReportCard();
        sleep(3_000);

        // STEP 3 — Click Download button
        System.out.println("[AppDownloadTest] STEP 3 – Clicking Download button...");
        appDownload.clickDownloadButton();
        sleep(3_000);

        // STEP 4 — Screenshot after Download
        System.out.println("[AppDownloadTest] STEP 4 – Taking screenshot after Download...");
        takeScreenshot("app_download_member_wise");

        // ════════════════════════════════════════════════════════════════
        //  FLOW 2 — App Download Report
        // ════════════════════════════════════════════════════════════════

        // STEP 5 — Click App Download menu (re-navigate)
        System.out.println("[AppDownloadTest] STEP 5 – Re-clicking App Download menu...");
        appDownload.clickAppDownloadMenu();
        sleep(3_000);

        // STEP 6 — Click App Download Report card
        System.out.println("[AppDownloadTest] STEP 6 – Clicking App Download Report card...");
        appDownload.clickAppDownloadReportCard();
        sleep(3_000);

        // STEP 7 — Click Export button
        System.out.println("[AppDownloadTest] STEP 7 – Clicking Export button...");
        appDownload.clickExportButton();
        sleep(3_000);

        // STEP 8 — Screenshot after Export
        System.out.println("[AppDownloadTest] STEP 8 – Taking screenshot after Export...");
        takeScreenshot("app_download_report_export");

        // ════════════════════════════════════════════════════════════════
        //  FLOW 3 — Partner Incentive Report
        // ════════════════════════════════════════════════════════════════

        // STEP 9 — Click App Download menu (re-navigate)
        System.out.println("[AppDownloadTest] STEP 9 – Re-clicking App Download menu...");
        appDownload.clickAppDownloadMenu();
        sleep(3_000);

        // STEP 10 — Click Partner Incentive Report card
        System.out.println("[AppDownloadTest] STEP 10 – Clicking Partner Incentive Report card...");
        appDownload.clickPartnerIncentiveReportCard();
        sleep(3_000);

        // STEP 11 — Click Today button
        System.out.println("[AppDownloadTest] STEP 11 – Clicking Today button...");
        appDownload.clickTodayButton();
        sleep(2_000);

        // STEP 12 — Click Weekly button
        System.out.println("[AppDownloadTest] STEP 12 – Clicking Weekly button...");
        appDownload.clickWeeklyButton();
        sleep(2_000);

        // STEP 13 — Click Monthly button
        System.out.println("[AppDownloadTest] STEP 13 – Clicking Monthly button...");
        appDownload.clickMonthlyButton();
        sleep(2_000);

        // STEP 14 — Click Yearly button
        System.out.println("[AppDownloadTest] STEP 14 – Clicking Yearly button...");
        appDownload.clickYearlyButton();
        sleep(2_000);

        // STEP 15 — Click Date Range button
        System.out.println("[AppDownloadTest] STEP 15 – Clicking Date Range button...");
        appDownload.clickDateRangeButton();
        sleep(3_000);

        // STEP 16 — Screenshot after Date Range
        System.out.println("[AppDownloadTest] STEP 16 – Taking final screenshot after Date Range...");
        takeScreenshot("app_download_partner_incentive_date_range");

        System.out.println("[AppDownloadTest] ✔ All steps completed successfully.");
    }

    // ── Screenshot Helper ─────────────────────────────────────────────────────
    private void takeScreenshot(String label) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + label + "_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));
            System.out.println("[AppDownloadTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[AppDownloadTest] ✘ Screenshot failed: " + e.getMessage());
        }
    }

    // ── Sleep Helper ──────────────────────────────────────────────────────────
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
