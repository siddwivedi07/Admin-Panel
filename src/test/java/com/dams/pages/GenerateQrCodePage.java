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
 * Page Object Model for the Generate QR Code module.
 *
 * Covers:
 *  - Generate QR Code sidebar menu item
 *  - Partner QR Code Report card
 *  - Filter buttons: Today, Weekly, Monthly, Yearly, Date Range
 *  - Generate Code card
 *  - Table actions: View, Edit, Delete (first row)
 */
public class GenerateQrCodePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────────────────────

    // Step 1 – Generate QR Code sidebar/menu link
    // Mirrors PaymentEmiPage pattern: href + visible text
    private final By generateQrCodeMenuLink = By.xpath(
        "//a[@href='/qrcode-generate' and normalize-space(.)='Generate QR Code']"
    );

    // Step 2 – Partner QR Code Report card
    private final By partnerQrCodeReportCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Partner QR Code Report']]"
    );

    // Steps 3–7 – Filter buttons
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

    // Step 8 – Generate Code card
    private final By generateCodeCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Generate Code']]"
    );

    // Steps 9–11 – Table action buttons (first row only)
    private final By firstViewBtn = By.xpath(
        "(//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='View']])[1]"
    );
    private final By firstEditBtn = By.xpath(
        "(//button[contains(@class,'ant-btn')][.//span[normalize-space(.)='Edit']])[1]"
    );
    private final By firstDeleteBtn = By.xpath(
        "(//button[contains(@class,'ant-btn-dangerous')][.//span[normalize-space(.)='Delete']])[1]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public GenerateQrCodePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ── Step 1: Click Generate QR Code menu link ──────────────────────────────

    public GenerateQrCodePage clickGenerateQrCodeMenu() {
        System.out.println("[GenerateQrCodePage] Step 1 → Clicking 'Generate QR Code' menu...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(generateQrCodeMenuLink));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 1 → PASSED ✔");
        return this;
    }

    // ── Step 2: Click Partner QR Code Report card ─────────────────────────────

    public GenerateQrCodePage clickPartnerQrCodeReportCard() {
        System.out.println("[GenerateQrCodePage] Step 2 → Clicking 'Partner QR Code Report' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(partnerQrCodeReportCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 2 → PASSED ✔");
        return this;
    }

    // ── Step 3: Click Today button ────────────────────────────────────────────

    public GenerateQrCodePage clickToday() {
        System.out.println("[GenerateQrCodePage] Step 3 → Clicking 'Today' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(todayBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 3 → PASSED ✔");
        return this;
    }

    // ── Step 4: Click Weekly button ───────────────────────────────────────────

    public GenerateQrCodePage clickWeekly() {
        System.out.println("[GenerateQrCodePage] Step 4 → Clicking 'Weekly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(weeklyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 4 → PASSED ✔");
        return this;
    }

    // ── Step 5: Click Monthly button ──────────────────────────────────────────

    public GenerateQrCodePage clickMonthly() {
        System.out.println("[GenerateQrCodePage] Step 5 → Clicking 'Monthly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(monthlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 5 → PASSED ✔");
        return this;
    }

    // ── Step 6: Click Yearly button ───────────────────────────────────────────

    public GenerateQrCodePage clickYearly() {
        System.out.println("[GenerateQrCodePage] Step 6 → Clicking 'Yearly' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(yearlyBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 6 → PASSED ✔");
        return this;
    }

    // ── Step 7: Click Date Range button and fill picker ───────────────────────

    public GenerateQrCodePage clickDateRangeAndFill(String startDate, String endDate) {
        System.out.println("[GenerateQrCodePage] Step 7 → Clicking 'Date Range' button...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(dateRangeBtn));
        scrollAndClick(btn);
        sleep(2000);

        System.out.println("[GenerateQrCodePage] Step 7 → Entering start date: " + startDate);
        WebElement startInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(startDateInput)
        );
        startInput.click();
        clearAndType(startInput, startDate);
        sleep(500);

        System.out.println("[GenerateQrCodePage] Step 7 → Entering end date: " + endDate);
        WebElement endInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(endDateInput)
        );
        endInput.click();
        clearAndType(endInput, endDate);
        endInput.sendKeys(Keys.ENTER);
        sleep(1500);

        System.out.println("[GenerateQrCodePage] Step 7 → PASSED ✔");
        return this;
    }

    // ── Step 8: Click Generate Code card ─────────────────────────────────────

    public GenerateQrCodePage clickGenerateCodeCard() {
        System.out.println("[GenerateQrCodePage] Step 8 → Clicking 'Generate Code' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(generateCodeCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 8 → PASSED ✔");
        return this;
    }

    // ── Step 9: Click first View button in the table ──────────────────────────

    public GenerateQrCodePage clickFirstViewButton() {
        System.out.println("[GenerateQrCodePage] Step 9 → Clicking first 'View' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstViewBtn));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 9 → PASSED ✔");
        return this;
    }

    // ── Step 10: Click first Edit button in the table ────────────────────────

    public GenerateQrCodePage clickFirstEditButton() {
        System.out.println("[GenerateQrCodePage] Step 10 → Clicking first 'Edit' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstEditBtn));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 10 → PASSED ✔");
        return this;
    }

    // ── Step 11: Click first Delete button in the table ──────────────────────

    public GenerateQrCodePage clickFirstDeleteButton() {
        System.out.println("[GenerateQrCodePage] Step 11 → Clicking first 'Delete' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstDeleteBtn));
        scrollAndClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 11 → PASSED ✔");
        return this;
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
