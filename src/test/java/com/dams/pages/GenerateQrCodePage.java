package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for Generate QR Code Module
 * Covers: Partner QR Code Report (Today, Weekly, Monthly, Yearly, Date Range)
 *         and Generate Code (View, Edit, Delete actions)
 */
public class GenerateQrCodePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─────────────────────────────────────────────
    // Locators – Step 1: Generate QR Code menu item
    // ─────────────────────────────────────────────
    private final By generateQrCodeMenu = By.cssSelector(
            "li[data-menu-id*='/qrcode-generate'] a"
    );

    // ─────────────────────────────────────────────
    // Locators – Step 2: Partner QR Code Report card
    // ─────────────────────────────────────────────
    private final By partnerQrCodeReportCard = By.xpath(
            "//div[@class='textData' and normalize-space(text())='Partner QR Code Report']"
    );

    // ─────────────────────────────────────────────
    // Locators – Steps 3-7: Filter buttons
    // ─────────────────────────────────────────────
    private final By todayButton = By.xpath(
            "//button[.//span[normalize-space(text())='Today']]"
    );

    private final By weeklyButton = By.xpath(
            "//button[.//span[normalize-space(text())='Weekly']]"
    );

    private final By monthlyButton = By.xpath(
            "//button[.//span[normalize-space(text())='Monthly']]"
    );

    private final By yearlyButton = By.xpath(
            "//button[.//span[normalize-space(text())='Yearly']]"
    );

    private final By dateRangeButton = By.xpath(
            "//button[.//span[normalize-space(text())='Date Range']]"
    );

    // Date Range picker inputs
    private final By startDateInput = By.cssSelector(
            "div.ant-picker-range input[placeholder='Start date']"
    );

    private final By endDateInput = By.cssSelector(
            "div.ant-picker-range input[placeholder='End date']"
    );

    // ─────────────────────────────────────────────
    // Locators – Step 8: Generate Code card
    // ─────────────────────────────────────────────
    private final By generateCodeCard = By.xpath(
            "//div[@class='textData' and normalize-space(text())='Generate Code']"
    );

    // ─────────────────────────────────────────────
    // Locators – Steps 9-11: Table action buttons
    // ─────────────────────────────────────────────
    private final By firstViewButton = By.xpath(
            "(//button[.//span[normalize-space(text())='View']])[1]"
    );

    private final By firstEditButton = By.xpath(
            "(//button[.//span[normalize-space(text())='Edit']])[1]"
    );

    private final By firstDeleteButton = By.xpath(
            "(//button[contains(@class,'ant-btn-dangerous') and .//span[normalize-space(text())='Delete']])[1]"
    );

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────
    public GenerateQrCodePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ─────────────────────────────────────────────
    // Step 1: Click "Generate QR Code" in the menu
    // ─────────────────────────────────────────────
    public void clickGenerateQrCodeMenu() {
        WebElement menu = wait.until(
                ExpectedConditions.elementToBeClickable(generateQrCodeMenu)
        );
        menu.click();
    }

    // ─────────────────────────────────────────────
    // Step 2: Click "Partner QR Code Report" card
    // ─────────────────────────────────────────────
    public void clickPartnerQrCodeReportCard() {
        WebElement card = wait.until(
                ExpectedConditions.elementToBeClickable(partnerQrCodeReportCard)
        );
        card.click();
    }

    // ─────────────────────────────────────────────
    // Step 3: Click "Today" button
    // ─────────────────────────────────────────────
    public void clickTodayButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(todayButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 4: Click "Weekly" button
    // ─────────────────────────────────────────────
    public void clickWeeklyButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(weeklyButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 5: Click "Monthly" button
    // ─────────────────────────────────────────────
    public void clickMonthlyButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(monthlyButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 6: Click "Yearly" button
    // ─────────────────────────────────────────────
    public void clickYearlyButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(yearlyButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 7: Click "Date Range" button and fill dates
    // ─────────────────────────────────────────────
    public void clickDateRangeButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(dateRangeButton)
        );
        btn.click();
    }

    /**
     * Enters a start date in the date-range picker.
     *
     * @param startDate date string, e.g. "2024-01-01"
     */
    public void enterStartDate(String startDate) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(startDateInput)
        );
        input.clear();
        input.sendKeys(startDate);
    }

    /**
     * Enters an end date in the date-range picker.
     *
     * @param endDate date string, e.g. "2024-12-31"
     */
    public void enterEndDate(String endDate) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(endDateInput)
        );
        input.clear();
        input.sendKeys(endDate);
    }

    /**
     * Convenience method: click Date Range and fill both dates.
     *
     * @param startDate start date string
     * @param endDate   end date string
     */
    public void selectDateRange(String startDate, String endDate) {
        clickDateRangeButton();
        enterStartDate(startDate);
        enterEndDate(endDate);
    }

    // ─────────────────────────────────────────────
    // Step 8: Click "Generate Code" card
    // ─────────────────────────────────────────────
    public void clickGenerateCodeCard() {
        WebElement card = wait.until(
                ExpectedConditions.elementToBeClickable(generateCodeCard)
        );
        card.click();
    }

    // ─────────────────────────────────────────────
    // Step 9: Click first "View" button in the table
    // ─────────────────────────────────────────────
    public void clickFirstViewButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(firstViewButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 10: Click first "Edit" button in the table
    // ─────────────────────────────────────────────
    public void clickFirstEditButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(firstEditButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Step 11: Click first "Delete" button in the table
    // ─────────────────────────────────────────────
    public void clickFirstDeleteButton() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(firstDeleteButton)
        );
        btn.click();
    }

    // ─────────────────────────────────────────────
    // Utility: check if an element is visible
    // ─────────────────────────────────────────────
    public boolean isElementVisible(By locator) {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
