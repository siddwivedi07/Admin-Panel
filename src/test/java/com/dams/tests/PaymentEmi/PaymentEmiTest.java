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

    // ── TC_01: Login → Click Payment Emi menu link ────────────────────────────
    @Test(priority = 1, description = "Login and click Payment Emi menu link")
    public void clickPaymentEmiLinkTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        ReportManager.logStep("PaymentEmi", "TC_01 – Click Payment Emi Link", true);

        System.out.println("[PaymentEmiTest] Waiting 3s for Payment Emi page to load...");
        sleep(3_000);

        takeScreenshot("tc01_payment_emi_link");
    }

    // ── TC_02: Login → Navigate to Emi Installment List card ─────────────────
    @Test(priority = 2, description = "Login and click Emi Installment List card",
          dependsOnMethods = "clickPaymentEmiLinkTest")
    public void clickEmiInstallmentListTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        ReportManager.logStep("PaymentEmi", "TC_02 – Click Emi Installment List", true);

        System.out.println("[PaymentEmiTest] Waiting 3s for Emi Installment List page to load...");
        sleep(3_000);

        takeScreenshot("tc02_emi_installment_list");
    }

    // ── TC_03: Login → Navigate to list → Click Today ────────────────────────
    @Test(priority = 3, description = "Login and click Today filter button",
          dependsOnMethods = "clickEmiInstallmentListTest")
    public void clickTodayButtonTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        sleep(2_000);
        paymentEmiPage.clickToday();
        ReportManager.logStep("PaymentEmi", "TC_03 – Click Today Filter", true);

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Today...");
        sleep(2_000);

        takeScreenshot("tc03_today_filter");
    }

    // ── TC_04: Login → Navigate to list → Click Weekly ───────────────────────
    @Test(priority = 4, description = "Login and click Weekly filter button",
          dependsOnMethods = "clickTodayButtonTest")
    public void clickWeeklyButtonTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        sleep(2_000);
        paymentEmiPage.clickWeekly();
        ReportManager.logStep("PaymentEmi", "TC_04 – Click Weekly Filter", true);

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Weekly...");
        sleep(2_000);

        takeScreenshot("tc04_weekly_filter");
    }

    // ── TC_05: Login → Navigate to list → Click Monthly ──────────────────────
    @Test(priority = 5, description = "Login and click Monthly filter button",
          dependsOnMethods = "clickWeeklyButtonTest")
    public void clickMonthlyButtonTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        sleep(2_000);
        paymentEmiPage.clickMonthly();
        ReportManager.logStep("PaymentEmi", "TC_05 – Click Monthly Filter", true);

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Monthly...");
        sleep(2_000);

        takeScreenshot("tc05_monthly_filter");
    }

    // ── TC_06: Login → Navigate to list → Click Yearly ───────────────────────
    @Test(priority = 6, description = "Login and click Yearly filter button",
          dependsOnMethods = "clickMonthlyButtonTest")
    public void clickYearlyButtonTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        sleep(2_000);
        paymentEmiPage.clickYearly();
        ReportManager.logStep("PaymentEmi", "TC_06 – Click Yearly Filter", true);

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Yearly...");
        sleep(2_000);

        takeScreenshot("tc06_yearly_filter");
    }

    // ── TC_07: Login → Navigate to list → Click Date Range and fill dates ─────
    @Test(priority = 7, description = "Login and click Date Range button and fill start/end dates",
          dependsOnMethods = "clickYearlyButtonTest")
    public void clickDateRangeAndFillDatesTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("PaymentEmi", "Step 0 – Login", true);

        sleep(5_000);

        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();
        sleep(2_000);
        paymentEmiPage.clickEmiInstallmentList();
        sleep(2_000);
        paymentEmiPage.clickDateRangeAndFill(START_DATE, END_DATE);
        ReportManager.logStep("PaymentEmi", "TC_07 – Click Date Range and Fill Dates", true);

        System.out.println("[PaymentEmiTest] Waiting 3s after filling Date Range...");
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
