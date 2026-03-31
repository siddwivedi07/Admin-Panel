package com.dams.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model (POM) for the Payment EMI section.
 *
 * Covers:
 *  - Payment Emi menu link
 *  - Emi Installment List card
 *  - Filter buttons: Today, Weekly, Monthly, Yearly, Date Range
 *  - Ant Design range date picker (Start Date / End Date)
 */
public class PaymentEmiPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────────────────────

    // Step 1 – Payment Emi sidebar/menu link
    private final By paymentEmiLink = By.xpath(
        "//a[@href='/payment-emi' and normalize-space(.)='Payment Emi']"
    );

    // Step 2 – Emi Installment List card inside ant-card-body
    private final By emiInstallmentListCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and normalize-space(.)='Emi Installment List']]"
    );

    // Step 3-7 – Filter buttons (matched by visible span text)
    private final By todayBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Today']]"
    );
    private final By weeklyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Weekly']]"
    );
    private final By monthlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Monthly']]"
    );
    private final By yearlyBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Yearly']]"
    );
    private final By dateRangeBtn = By.xpath(
        "//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Date Range']]"
    );

    // Step 7 – Ant Design range picker inputs
    private final By startDateInput = By.xpath(
        "//div[contains(@class,'ant-picker-range')]" +
        "//input[@placeholder='Start date' or @date-range='start']"
    );
    private final By endDateInput = By.xpath(
        "//div[contains(@class,'ant-picker-range')]" +
        "//input[@placeholder='End date' or @date-range='end']"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public PaymentEmiPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Step 1: Click Payment Emi menu link ───────────────────────────────────

    public PaymentEmiPage clickPaymentEmiLink() {
        System.out.println("[PaymentEmiPage] Step 1 → Clicking 'Payment Emi' link...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(paymentEmiLink));
        scrollAndClick(element);
        System.out.println("[PaymentEmiPage] Step 1 → PASSED ✔");
        return this;
    }

    // ── Step 2: Click Emi Installment List card ───────────────────────────────

    public PaymentEmiPage clickEmiInstallmentList() {
        System.out.println("[PaymentEmiPage] Step 2 → Clicking 'Emi Installment List' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(emiInstallmentListCard));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[PaymentEmiPage] Step 2 → PASSED ✔");
        return this;
    }

    // ── Step 3: Click Today button ────────────────────────────────────────────

    public PaymentEmiPage clickToday() {
        System.out.println("[PaymentEmiPage] Step 3 → Clicking 'Today' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(todayBtn));
        scrollAndClick(element);
        sleep(1000);
        System.out.println("[PaymentEmiPage] Step 3 → PASSED ✔");
        return this;
    }

    // ── Step 4: Click Weekly button ───────────────────────────────────────────

    public PaymentEmiPage clickWeekly() {
        System.out.println("[PaymentEmiPage] Step 4 → Clicking 'Weekly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(weeklyBtn));
        scrollAndClick(element);
        sleep(1000);
        System.out.println("[PaymentEmiPage] Step 4 → PASSED ✔");
        return this;
    }

    // ── Step 5: Click Monthly button ──────────────────────────────────────────

    public PaymentEmiPage clickMonthly() {
        System.out.println("[PaymentEmiPage] Step 5 → Clicking 'Monthly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(monthlyBtn));
        scrollAndClick(element);
        sleep(1000);
        System.out.println("[PaymentEmiPage] Step 5 → PASSED ✔");
        return this;
    }

    // ── Step 6: Click Yearly button ───────────────────────────────────────────

    public PaymentEmiPage clickYearly() {
        System.out.println("[PaymentEmiPage] Step 6 → Clicking 'Yearly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(yearlyBtn));
        scrollAndClick(element);
        sleep(1000);
        System.out.println("[PaymentEmiPage] Step 6 → PASSED ✔");
        return this;
    }

    // ── Step 7: Click Date Range button and fill picker ───────────────────────

    public PaymentEmiPage clickDateRangeAndFill(String startDate, String endDate) {
        System.out.println("[PaymentEmiPage] Step 7 → Clicking 'Date Range' button...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(dateRangeBtn));
        scrollAndClick(btn);
        System.out.println("[PaymentEmiPage] Step 7 → 'Date Range' clicked. Waiting for picker...");

        sleep(2000);

        System.out.println("[PaymentEmiPage] Step 7 → Entering start date: " + startDate);
        WebElement startInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(startDateInput)
        );
        startInput.click();
        clearAndType(startInput, startDate);
        sleep(500);

        System.out.println("[PaymentEmiPage] Step 7 → Entering end date: " + endDate);
        WebElement endInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(endDateInput)
        );
        endInput.click();
        clearAndType(endInput, endDate);
        endInput.sendKeys(Keys.ENTER);
        sleep(1000);

        System.out.println("[PaymentEmiPage] Step 7 → PASSED ✔");
        return this;
    }

    // ── Visibility / value checks ─────────────────────────────────────────────

    public boolean isEmiInstallmentListVisible() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(emiInstallmentListCard)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTodayButtonVisible() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(todayBtn)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getStartDateValue() {
        try {
            return driver.findElement(startDateInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getEndDateValue() {
        try {
            return driver.findElement(endDateInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
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
