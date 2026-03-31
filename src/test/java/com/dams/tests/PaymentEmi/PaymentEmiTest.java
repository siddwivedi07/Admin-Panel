package com.dams.tests.PaymentEmi;

import com.dams.base.BaseTest;
import com.dams.pages.LoginPage;
import com.dams.pages.PaymentEmiPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentEmiTest extends BaseTest {

    private static final String START_DATE = "2025-01-01";
    private static final String END_DATE   = "2025-03-31";

    /**
     * Single test method so that all steps (Login + TC_01 … TC_07)
     * are logged under ONE "PaymentEmi" module in the report.
     */
    @Test(description = "Payment EMI – full flow: login → installment list → all filters")
    public void paymentEmiFullFlowTest() {

        // ── Step 0: Login ─────────────────────────────────────────────────────
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);

        // ── TC_01: Click Payment Emi menu link ────────────────────────────────
        paymentEmiPage.clickPaymentEmiLink();
        ReportManager.logStep("PaymentEmi", "TC_01 – Click Payment Emi Link", true);
        sleep(3_000);
        takeScreenshot("tc01_payment_emi_link");

        // ── TC_02: Click Emi Installment List card ────────────────────────────
        paymentEmiPage.clickEmiInstallmentList();
        ReportManager.logStep("PaymentEmi", "TC_02 – Click Emi Installment List", true);
        sleep(3_000);
        takeScreenshot("tc02_emi_installment_list");

        // ── TC_03: Click Today button ─────────────────────────────────────────
        paymentEmiPage.clickToday();
        ReportManager.logStep("PaymentEmi", "TC_03 – Click Today Filter", true);
        sleep(2_000);
        takeScreenshot("tc03_today_filter");

        // ── TC_04: Click Weekly button ────────────────────────────────────────
        paymentEmiPage.clickWeekly();
        ReportManager.logStep("PaymentEmi", "TC_04 – Click Weekly Filter", true);
        sleep(2_000);
        takeScreenshot("tc04_weekly_filter");

        // ── TC_05: Click Monthly button ───────────────────────────────────────
        paymentEmiPage.clickMonthly();
        ReportManager.logStep("PaymentEmi", "TC_05 – Click Monthly Filter", true);
        sleep(2_000);
        takeScreenshot("tc05_monthly_filter");

        // ── TC_06: Click Yearly button ────────────────────────────────────────
        paymentEmiPage.clickYearly();
        ReportManager.logStep("PaymentEmi", "TC_06 – Click Yearly Filter", true);
        sleep(2_000);
        takeScreenshot("tc06_yearly_filter");

        // ── TC_07: Click Date Range and fill dates ────────────────────────────
        paymentEmiPage.clickDateRangeAndFill(START_DATE, END_DATE);
        ReportManager.logStep("PaymentEmi", "TC_07 – Click Date Range and Fill Dates", true);
        sleep(3_000);
        takeScreenshot("tc07_date_range_filter");
    }

    // ── Screenshot helper (same pattern as LoginTest) ─────────────────────────
    private void takeScreenshot(String testName) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));

            System.out.println("[PaymentEmiTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[PaymentEmiTest] ✘ Screenshot failed: " + e.getMessage());
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
