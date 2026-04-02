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

    // Step 1 – Generate QR Code sidebar/menu link (primary)
    private final By generateQrCodeMenuLink = By.xpath(
        "//a[@href='/qrcode-generate']" +
        "[.//*[contains(@class,'ant-menu-title-content') and " +
        "normalize-space(.)='Generate QR Code']]"
    );

    // Step 1 – Fallback: match by href alone (collapsed/icon-only sidebar)
    private final By generateQrCodeMenuLinkHref = By.xpath(
        "//a[@href='/qrcode-generate']"
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
    // Real HTML: <button class="ant-btn ... ant-btn-link ..."><span>View</span></button>
    private final By firstViewBtn = By.xpath(
        "(//button[contains(@class,'ant-btn-link')][.//span[normalize-space(.)='View']])[1]"
    );

    // Real HTML: <button class="ant-btn ... ant-btn-link ant-btn-color-link ant-btn-variant-link"><span>Edit</span></button>
    private final By firstEditBtn = By.xpath(
        "(//button[contains(@class,'ant-btn-link')][.//span[normalize-space(.)='Edit']])[1]"
    );

    // Real HTML: <button class="ant-btn ... ant-btn-link ant-btn-dangerous ..."><span>Delete</span></button>
    private final By firstDeleteBtn = By.xpath(
        "(//button[contains(@class,'ant-btn-link') and contains(@class,'ant-btn-dangerous')]" +
        "[.//span[normalize-space(.)='Delete']])[1]"
    );

    // Ant Design table row — used to confirm the table has loaded
    private final By tableRow = By.xpath(
        "//tr[contains(@class,'ant-table-row')]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public GenerateQrCodePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Step 1: Click Generate QR Code menu link ──────────────────────────────

    public GenerateQrCodePage clickGenerateQrCodeMenu() {
        System.out.println("[GenerateQrCodePage] Step 1 → Clicking 'Generate QR Code' menu...");
        WebElement element = findQrMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findQrMenuElement() {
        // Pass 1 — primary
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(generateQrCodeMenuLink));
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodePage] Primary menu locator not clickable — trying href-only...");
        }
        // Pass 2 — href-only (collapsed sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(generateQrCodeMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[GenerateQrCodePage] href-only locator not clickable — trying to expand sidebar...");
        }
        // Pass 3 — expand sidebar, then retry
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(generateQrCodeMenuLink));
        } catch (Exception ignored) {
            // last chance
        }
        return wait.until(ExpectedConditions.elementToBeClickable(generateQrCodeMenuLinkHref));
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
                System.out.println("[GenerateQrCodePage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[GenerateQrCodePage] No sidebar toggle found — sidebar may already be expanded.");
            }
        } catch (Exception e) {
            System.out.println("[GenerateQrCodePage] Could not click sidebar toggle: " + e.getMessage());
        }
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
        System.out.println("[GenerateQrCodePage] Step 9 → Waiting for table to load...");
        waitForTableRows();
        System.out.println("[GenerateQrCodePage] Step 9 → Clicking first 'View' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstViewBtn));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 9 → PASSED ✔");
        return this;
    }

    // ── Step 10: Click first Edit button in the table ────────────────────────

    /**
     * FIX: After navigate().back() from View, the browser returns to the
     * Generate Code CARD page (not the table). The test must call
     * clickGenerateCodeCard() again before this method.
     * This method waits for the table rows to appear, then clicks Edit (once).
     *
     * Real HTML of Edit button:
     * <button type="button"
     *   class="ant-btn css-tjsggz ant-btn-link ant-btn-color-link ant-btn-variant-link">
     *   <span>Edit</span>
     * </button>
     */
    public GenerateQrCodePage clickFirstEditButton() {
        System.out.println("[GenerateQrCodePage] Step 10 → Waiting for table to load...");
        waitForTableRows();
        System.out.println("[GenerateQrCodePage] Step 10 → Clicking first 'Edit' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstEditBtn));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[GenerateQrCodePage] Step 10 → PASSED ✔");
        return this;
    }

    // ── Step 11: Click first Delete button in the table ──────────────────────

    /**
     * FIX: Same reasoning as Step 10 — must be on the table page before calling.
     *
     * Real HTML of Delete button:
     * <button aria-describedby=":rg8:" type="button"
     *   class="ant-btn css-tjsggz ant-btn-link ant-btn-dangerous
     *          ant-btn-color-dangerous ant-btn-variant-link">
     *   <span>Delete</span>
     * </button>
     */
    public GenerateQrCodePage clickFirstDeleteButton() {
        System.out.println("[GenerateQrCodePage] Step 11 → Waiting for table to load...");
        waitForTableRows();
        System.out.println("[GenerateQrCodePage] Step 11 → Clicking first 'Delete' button...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstDeleteBtn));
        scrollAndJsClick(element);
        sleep(1500);
        System.out.println("[GenerateQrCodePage] Step 11 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Waits up to 30 s for at least one ant-table-row to appear.
     * Ensures the table has fully loaded before looking for action buttons.
     */
    private void waitForTableRows() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(tableRow));
            sleep(500); // brief pause for any loading overlay to clear
        } catch (Exception e) {
            System.out.println("[GenerateQrCodePage] Warning: table rows not detected — proceeding anyway.");
        }
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
        element.click();
    }

    /**
     * JS click — bypasses any overlay/intercept issue common with
     * ant-btn-link buttons after table reloads or page transitions.
     */
    private void scrollAndJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
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
