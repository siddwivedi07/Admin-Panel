package com.dams.tests.GenerateQrCode;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.GenerateQrCodePage;

/**
 * TestNG Test Class for Generate QR Code Module
 * Package : com.dams.tests.GenerateQrCode
 * Suite   : testng.xml  →  <class name="com.dams.tests.GenerateQrCode.GenerateQrCodTest"/>
 */
public class GenerateQrCodTest {

    private WebDriver driver;
    private GenerateQrCodePage generateQrCodePage;

    // ─────────────────────────────────────────────────────────────────────────
    // TODO: Replace with your actual application URL and credentials
    // ─────────────────────────────────────────────────────────────────────────
    private static final String APP_URL      = "https://your-application-url.com";
    private static final String START_DATE   = "2024-01-01";
    private static final String END_DATE     = "2024-12-31";

    // ─────────────────────────────────────────────────────────────────────────
    // Setup – runs once before all tests in this class
    // ─────────────────────────────────────────────────────────────────────────
    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");   // uncomment for headless mode
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.get(APP_URL);

        generateQrCodePage = new GenerateQrCodePage(driver);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 1 – Click "Generate QR Code" in the side menu
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 1,
          description = "Step 1: Click Generate QR Code menu item")
    public void testClickGenerateQrCodeMenu() {
        generateQrCodePage.clickGenerateQrCodeMenu();
        System.out.println("✔ Step 1 PASSED – Generate QR Code menu clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 2 – Click "Partner QR Code Report" card
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 2,
          dependsOnMethods = "testClickGenerateQrCodeMenu",
          description = "Step 2: Click Partner QR Code Report card")
    public void testClickPartnerQrCodeReportCard() {
        generateQrCodePage.clickPartnerQrCodeReportCard();
        System.out.println("✔ Step 2 PASSED – Partner QR Code Report card clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 3 – Click "Today" filter button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 3,
          dependsOnMethods = "testClickPartnerQrCodeReportCard",
          description = "Step 3: Click Today button")
    public void testClickTodayButton() {
        generateQrCodePage.clickTodayButton();
        System.out.println("✔ Step 3 PASSED – Today button clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 4 – Click "Weekly" filter button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 4,
          dependsOnMethods = "testClickTodayButton",
          description = "Step 4: Click Weekly button")
    public void testClickWeeklyButton() {
        generateQrCodePage.clickWeeklyButton();
        System.out.println("✔ Step 4 PASSED – Weekly button clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 5 – Click "Monthly" filter button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 5,
          dependsOnMethods = "testClickWeeklyButton",
          description = "Step 5: Click Monthly button")
    public void testClickMonthlyButton() {
        generateQrCodePage.clickMonthlyButton();
        System.out.println("✔ Step 5 PASSED – Monthly button clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 6 – Click "Yearly" filter button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 6,
          dependsOnMethods = "testClickMonthlyButton",
          description = "Step 6: Click Yearly button")
    public void testClickYearlyButton() {
        generateQrCodePage.clickYearlyButton();
        System.out.println("✔ Step 6 PASSED – Yearly button clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 7 – Click "Date Range" and enter start & end dates
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 7,
          dependsOnMethods = "testClickYearlyButton",
          description = "Step 7: Click Date Range button and enter start/end dates")
    public void testSelectDateRange() {
        generateQrCodePage.selectDateRange(START_DATE, END_DATE);
        System.out.println("✔ Step 7 PASSED – Date Range selected: "
                + START_DATE + " → " + END_DATE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 8 – Navigate back and click "Generate Code" card
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 8,
          dependsOnMethods = "testSelectDateRange",
          description = "Step 8: Navigate back and click Generate Code card")
    public void testClickGenerateCodeCard() {
        // Navigate back to the Generate QR Code landing page
        driver.navigate().back();
        generateQrCodePage.clickGenerateCodeCard();
        System.out.println("✔ Step 8 PASSED – Generate Code card clicked");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 9 – Click first "View" button in the table (only once)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 9,
          dependsOnMethods = "testClickGenerateCodeCard",
          description = "Step 9: Click first View button in Generate Code table")
    public void testClickFirstViewButton() {
        generateQrCodePage.clickFirstViewButton();
        System.out.println("✔ Step 9 PASSED – View button clicked (first row)");
        // Navigate back to table after viewing
        driver.navigate().back();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 10 – Click first "Edit" button in the table (only once)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 10,
          dependsOnMethods = "testClickFirstViewButton",
          description = "Step 10: Click first Edit button in Generate Code table")
    public void testClickFirstEditButton() {
        generateQrCodePage.clickFirstEditButton();
        System.out.println("✔ Step 10 PASSED – Edit button clicked (first row)");
        // Navigate back to table after editing
        driver.navigate().back();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Step 11 – Click first "Delete" button in the table
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 11,
          dependsOnMethods = "testClickFirstEditButton",
          description = "Step 11: Click first Delete button in Generate Code table")
    public void testClickFirstDeleteButton() {
        generateQrCodePage.clickFirstDeleteButton();
        System.out.println("✔ Step 11 PASSED – Delete button clicked (first row)");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Teardown – runs once after all tests in this class
    // ─────────────────────────────────────────────────────────────────────────
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✔ Browser closed – Test suite completed");
        }
    }
}
