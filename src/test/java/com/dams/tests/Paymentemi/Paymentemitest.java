package com.dams.tests.PaymentEmi;

import com.dams.base.BaseTest;
import com.dams.pages.PaymentEmiPage;
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

    // ── TC_01: Click Payment Emi menu link ────────────────────────────────────
    @Test(priority = 1, description = "Click Payment Emi menu link")
    public void clickPaymentEmiLinkTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickPaymentEmiLink();

        System.out.println("[PaymentEmiTest] Waiting 3s for Payment Emi page to load...");
        try { Thread.sleep(3_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc01_payment_emi_link");
    }

    // ── TC_02: Click Emi Installment List card ────────────────────────────────
    @Test(priority = 2, description = "Click Emi Installment List card",
          dependsOnMethods = "clickPaymentEmiLinkTest")
    public void clickEmiInstallmentListTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickEmiInstallmentList();

        System.out.println("[PaymentEmiTest] Waiting 3s for Emi Installment List page to load...");
        try { Thread.sleep(3_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc02_emi_installment_list");
    }

    // ── TC_03: Click Today button ─────────────────────────────────────────────
    @Test(priority = 3, description = "Click Today filter button",
          dependsOnMethods = "clickEmiInstallmentListTest")
    public void clickTodayButtonTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickToday();

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Today...");
        try { Thread.sleep(2_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc03_today_filter");
    }

    // ── TC_04: Click Weekly button ────────────────────────────────────────────
    @Test(priority = 4, description = "Click Weekly filter button",
          dependsOnMethods = "clickTodayButtonTest")
    public void clickWeeklyButtonTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickWeekly();

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Weekly...");
        try { Thread.sleep(2_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc04_weekly_filter");
    }

    // ── TC_05: Click Monthly button ───────────────────────────────────────────
    @Test(priority = 5, description = "Click Monthly filter button",
          dependsOnMethods = "clickWeeklyButtonTest")
    public void clickMonthlyButtonTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickMonthly();

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Monthly...");
        try { Thread.sleep(2_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc05_monthly_filter");
    }

    // ── TC_06: Click Yearly button ────────────────────────────────────────────
    @Test(priority = 6, description = "Click Yearly filter button",
          dependsOnMethods = "clickMonthlyButtonTest")
    public void clickYearlyButtonTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickYearly();

        System.out.println("[PaymentEmiTest] Waiting 2s after clicking Yearly...");
        try { Thread.sleep(2_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        takeScreenshot("tc06_yearly_filter");
    }

    // ── TC_07: Click Date Range button and fill start & end dates ─────────────
    @Test(priority = 7, description = "Click Date Range button and fill start/end dates",
          dependsOnMethods = "clickYearlyButtonTest")
    public void clickDateRangeAndFillDatesTest() {
        PaymentEmiPage paymentEmiPage = new PaymentEmiPage(driver);
        paymentEmiPage.clickDateRangeAndFill(START_DATE, END_DATE);

        System.out.println("[PaymentEmiTest] Waiting 3s after filling Date Range...");
        try { Thread.sleep(3_000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

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
}
