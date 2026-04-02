package com.dams.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object Model for the CEO Report module.
 *
 * Covers:
 *  Step 1  – CEO Report sidebar menu item
 *  Step 2  – Sales Report Partner card
 *  Steps 3–7  – Filter buttons (Today / Weekly / Monthly / Yearly / Date Range)
 *              + Date Range calendar picker
 *  Step 8  – Navigate back from Sales Report Partner
 *  Step 9  – Instructor Report card
 *  Step 10 – Team Member Wise Sale card
 *  Steps 11–15 – Filter buttons + Date Range calendar picker
 *  Step 16 – Navigate back from Team Member Wise Sale
 *  Step 17 – Category Wise Revenue card
 *  Steps 18–22 – Segmented filter buttons + Date Range calendar picker
 */
public class CeoReportPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String START_DATE = "2025-01-01";
    private static final String END_DATE   = "2025-03-31";

    // ── Locators ──────────────────────────────────────────────────────────────

    // Step 1 – CEO Report sidebar menu link
    // HTML: <span class="ant-menu-title-content"><a href="/ceo-report">CEO Report</a></span>
    private final By ceoReportMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/ceo-report']"
    );
    // Fallback – href only
    private final By ceoReportMenuLinkHref = By.xpath(
        "//a[@href='/ceo-report']"
    );

    // Step 2 – Sales Report Partner card
    // HTML: <div class="textData">Sales Report Partner</div>
    private final By salesReportPartnerCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Sales Report Partner']]"
    );

    // Steps 3–7 – Filter buttons inside Sales Report Partner
    // These use ant-btn-primary / ant-btn-default variant buttons
    private final By salesTodayBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Today']]"
    );
    private final By salesWeeklyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Weekly']]"
    );
    private final By salesMonthlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Monthly']]"
    );
    private final By salesYearlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Yearly']]"
    );
    private final By salesDateRangeBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Date Range']]"
    );

    // Step 7 / Step 15 / Step 22 – Ant Design range picker inputs
    // HTML: <input placeholder="Start date" date-range="start">
    //       <input placeholder="End date"   date-range="end">
    private final By startDateInput = By.xpath(
        "//div[contains(@class,'ant-picker-range')]" +
        "//input[@placeholder='Start date' or @date-range='start']"
    );
    private final By endDateInput = By.xpath(
        "//div[contains(@class,'ant-picker-range')]" +
        "//input[@placeholder='End date' or @date-range='end']"
    );

    // Step 9 – Instructor Report card
    // HTML: <div class="textData">Instructor Report</div>
    private final By instructorReportCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Instructor Report']]"
    );

    // Step 10 – Team Member Wise Sale card
    // HTML: <div class="textData">Team Member Wise Sale</div>
    private final By teamMemberWiseSaleCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Team Member Wise Sale']]"
    );

    // Steps 11–15 – Filter buttons inside Team Member Wise Sale
    // HTML: <button style="margin-right: 10px;"><span>Today</span></button>
    // Using normalize-space on span text to be robust to style differences
    private final By teamTodayBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Today']]"
    );
    private final By teamWeeklyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Weekly']]"
    );
    private final By teamMonthlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Monthly']]"
    );
    private final By teamYearlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Yearly']]"
    );
    private final By teamDateRangeBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Date Range']]"
    );

    // Step 17 – Category Wise Revenue card
    // HTML: <div class="textData">Category Wise Revenue</div>
    private final By categoryWiseRevenueCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Category Wise Revenue']]"
    );

    // Steps 18–22 – Ant Design Segmented filter buttons (different from ant-btn)
    // HTML: <div class="ant-segmented-item-label" title="Today">Today</div>
    private final By segmentedTodayBtn = By.xpath(
        "//div[contains(@class,'ant-segmented-item-label')][@title='Today']"
    );
    private final By segmentedWeeklyBtn = By.xpath(
        "//div[contains(@class,'ant-segmented-item-label')][@title='Weekly']"
    );
    private final By segmentedMonthlyBtn = By.xpath(
        "//div[contains(@class,'ant-segmented-item-label')][@title='Monthly']"
    );
    private final By segmentedYearlyBtn = By.xpath(
        "//div[contains(@class,'ant-segmented-item-label')][@title='Yearly']"
    );
    private final By segmentedDateRangeBtn = By.xpath(
        "//div[contains(@class,'ant-segmented-item-label')][@title='Date Range']"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public CeoReportPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click CEO Report menu link
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCeoReportMenu() {
        System.out.println("[CeoReportPage] Step 1 → Clicking 'CEO Report' menu...");
        WebElement element = findCeoMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[CeoReportPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findCeoMenuElement() {
        // Pass 1 — primary: span.ant-menu-title-content > a[href='/ceo-report']
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(ceoReportMenuLink));
        } catch (Exception ignored) {
            System.out.println("[CeoReportPage] Primary menu locator failed — trying href-only...");
        }
        // Pass 2 — href-only (collapsed/icon sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(ceoReportMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[CeoReportPage] href-only locator failed — trying sidebar expand...");
        }
        // Pass 3 — expand collapsed sidebar, retry
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(ceoReportMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(ceoReportMenuLinkHref));
    }

    private void tryExpandSidebar() {
        try {
            By siderTrigger = By.cssSelector(
                ".ant-layout-sider-trigger, " +
                ".ant-layout-sider .anticon-menu-fold, " +
                ".ant-layout-sider .anticon-menu-unfold"
            );
            List<WebElement> triggers = driver.findElements(siderTrigger);
            if (!triggers.isEmpty()) {
                triggers.get(0).click();
                System.out.println("[CeoReportPage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[CeoReportPage] No sidebar toggle found — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[CeoReportPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Sales Report Partner card
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesReportPartnerCard() {
        System.out.println("[CeoReportPage] Step 2 → Clicking 'Sales Report Partner' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(salesReportPartnerCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[CeoReportPage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Click Today button (Sales Report Partner)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesToday() {
        System.out.println("[CeoReportPage] Step 3 → Clicking 'Today' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(salesTodayBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 3 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Click Weekly button (Sales Report Partner)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesWeekly() {
        System.out.println("[CeoReportPage] Step 4 → Clicking 'Weekly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(salesWeeklyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 4 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Click Monthly button (Sales Report Partner)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesMonthly() {
        System.out.println("[CeoReportPage] Step 5 → Clicking 'Monthly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(salesMonthlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 5 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 – Click Yearly button (Sales Report Partner)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesYearly() {
        System.out.println("[CeoReportPage] Step 6 → Clicking 'Yearly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(salesYearlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 6 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 – Click Date Range button + fill calendar picker
    //           (Sales Report Partner)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickSalesDateRangeAndFill() {
        System.out.println("[CeoReportPage] Step 7 → Clicking 'Date Range' button...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(salesDateRangeBtn));
        scrollAndClick(btn);
        sleep(2000);
        fillDateRangePicker(START_DATE, END_DATE);
        System.out.println("[CeoReportPage] Step 7 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 – Navigate back from Sales Report Partner
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage navigateBackFromSalesReport() {
        System.out.println("[CeoReportPage] Step 8 → Navigating back from Sales Report Partner...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[CeoReportPage] Step 8 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 – Click Instructor Report card
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickInstructorReportCard() {
        System.out.println("[CeoReportPage] Step 9 → Clicking 'Instructor Report' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(instructorReportCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[CeoReportPage] Step 9 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 10 – Click Team Member Wise Sale card
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamMemberWiseSaleCard() {
        System.out.println("[CeoReportPage] Step 10 → Clicking 'Team Member Wise Sale' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(teamMemberWiseSaleCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[CeoReportPage] Step 10 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 11 – Click Today button (Team Member Wise Sale)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamToday() {
        System.out.println("[CeoReportPage] Step 11 → Clicking 'Today' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(teamTodayBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 11 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 12 – Click Weekly button (Team Member Wise Sale)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamWeekly() {
        System.out.println("[CeoReportPage] Step 12 → Clicking 'Weekly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(teamWeeklyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 12 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 13 – Click Monthly button (Team Member Wise Sale)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamMonthly() {
        System.out.println("[CeoReportPage] Step 13 → Clicking 'Monthly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(teamMonthlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 13 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 14 – Click Yearly button (Team Member Wise Sale)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamYearly() {
        System.out.println("[CeoReportPage] Step 14 → Clicking 'Yearly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(teamYearlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 14 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 15 – Click Date Range button + fill calendar picker
    //            (Team Member Wise Sale)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickTeamDateRangeAndFill() {
        System.out.println("[CeoReportPage] Step 15 → Clicking 'Date Range' button...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(teamDateRangeBtn));
        scrollAndClick(btn);
        sleep(2000);
        fillDateRangePicker(START_DATE, END_DATE);
        System.out.println("[CeoReportPage] Step 15 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 16 – Navigate back from Team Member Wise Sale
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage navigateBackFromTeamMemberWiseSale() {
        System.out.println("[CeoReportPage] Step 16 → Navigating back from Team Member Wise Sale...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[CeoReportPage] Step 16 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 17 – Click Category Wise Revenue card
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryWiseRevenueCard() {
        System.out.println("[CeoReportPage] Step 17 → Clicking 'Category Wise Revenue' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(categoryWiseRevenueCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[CeoReportPage] Step 17 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 18 – Click Today segmented button (Category Wise Revenue)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryToday() {
        System.out.println("[CeoReportPage] Step 18 → Clicking segmented 'Today' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(segmentedTodayBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 18 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 19 – Click Weekly segmented button (Category Wise Revenue)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryWeekly() {
        System.out.println("[CeoReportPage] Step 19 → Clicking segmented 'Weekly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(segmentedWeeklyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 19 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 20 – Click Monthly segmented button (Category Wise Revenue)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryMonthly() {
        System.out.println("[CeoReportPage] Step 20 → Clicking segmented 'Monthly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(segmentedMonthlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 20 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 21 – Click Yearly segmented button (Category Wise Revenue)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryYearly() {
        System.out.println("[CeoReportPage] Step 21 → Clicking segmented 'Yearly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(segmentedYearlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[CeoReportPage] Step 21 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 22 – Click Date Range segmented button + fill calendar picker
    //            (Category Wise Revenue)
    // ══════════════════════════════════════════════════════════════════════════

    public CeoReportPage clickCategoryDateRangeAndFill() {
        System.out.println("[CeoReportPage] Step 22 → Clicking segmented 'Date Range' button...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(segmentedDateRangeBtn));
        scrollAndClick(btn);
        sleep(2000);
        fillDateRangePicker(START_DATE, END_DATE);
        System.out.println("[CeoReportPage] Step 22 → PASSED ✔");
        return this;
    }

    // ── Shared Date Range picker helper ──────────────────────────────────────

    /**
     * Fills the Ant Design range picker that appears after clicking Date Range.
     * Works for all three sections (Sales, Team Member, Category Wise Revenue)
     * since they all use the same ant-picker-range structure:
     *   <input placeholder="Start date" date-range="start">
     *   <input placeholder="End date"   date-range="end">
     */
    private void fillDateRangePicker(String startDate, String endDate) {
        System.out.println("[CeoReportPage] → Filling date range: " + startDate + " to " + endDate);

        WebElement startInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(startDateInput)
        );
        startInput.click();
        clearAndType(startInput, startDate);
        sleep(500);

        WebElement endInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(endDateInput)
        );
        endInput.click();
        clearAndType(endInput, endDate);
        endInput.sendKeys(Keys.ENTER);
        sleep(1500);

        System.out.println("[CeoReportPage] → Date range filled ✔");
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
        element.click();
    }

    private void clearAndType(WebElement input, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input);
        input.sendKeys(value);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
