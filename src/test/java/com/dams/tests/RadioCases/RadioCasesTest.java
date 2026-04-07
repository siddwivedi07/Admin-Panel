package com.dams.tests.RadioCases;

import com.dams.pages.RadioCasesPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TestNG test class for the Radio Cases module.
 *
 * Covers all 16 steps defined in RadioCasesPage:
 *  Step  1  – Click Radio Cases sidebar menu
 *  Step  2  – Click Radio Modality List card
 *  Step  3  – Search modality by "Pradeep"
 *  Step  4  – Click Add Modality button
 *  Step  5  – Fill Modality Name "Pradeep" → Submit
 *  Step  6  – Navigate back from Radio Modality List
 *  Step  7  – Click Radio Category List card
 *  Step  8  – Search category by "88"
 *  Step  9  – Edit category → "MentalHealthIssues" → Update
 *  Step 10  – Delete first row category
 *  Step 11  – Add Category "MentalHealthIssues" → Submit
 *  Step 12  – Navigate back from Radio Category List
 *  Step 13  – Click Add Case card
 *  Step 14  – Search case by "65"
 *  Step 15  – Click Add button (open Add Case form)
 *  Step 16  – Fill Add Case form → Save Case
 */
public class RadioCasesTest {

    private WebDriver      driver;
    private RadioCasesPage radioCasesPage;

    // ── Update this URL to match your environment ──────────────────────────────
    private static final String BASE_URL = "https://your-app-url.com";

    // ══════════════════════════════════════════════════════════════════════════
    //  Setup & Teardown
    // ══════════════════════════════════════════════════════════════════════════

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");   // uncomment for headless mode
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.get(BASE_URL);
        radioCasesPage = new RadioCasesPage(driver);
        System.out.println("[RadioCasesTest] Browser launched and navigated to: " + BASE_URL);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("[RadioCasesTest] Browser closed.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 1 – Click Radio Cases sidebar menu
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 1, description = "Step 1: Click Radio Cases menu link in sidebar")
    public void testClickRadioCasesMenu() {
        radioCasesPage.clickRadioCasesMenu();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 2 – Click Radio Modality List card
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 2, description = "Step 2: Click Radio Modality List card",
          dependsOnMethods = "testClickRadioCasesMenu")
    public void testClickRadioModalityListCard() {
        radioCasesPage.clickRadioModalityListCard();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 3 – Search modality by "Pradeep"
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 3, description = "Step 3: Search modality by 'Pradeep'",
          dependsOnMethods = "testClickRadioModalityListCard")
    public void testSearchModality() {
        radioCasesPage.searchModality();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 4 – Click Add Modality button
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 4, description = "Step 4: Click Add Modality button to open popup",
          dependsOnMethods = "testSearchModality")
    public void testClickAddModalityButton() {
        radioCasesPage.clickAddModalityButton();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 5 – Fill Modality Name "Pradeep" → Submit
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 5, description = "Step 5: Fill modality name 'Pradeep' and submit",
          dependsOnMethods = "testClickAddModalityButton")
    public void testFillModalityNameAndSubmit() {
        radioCasesPage.fillModalityNameAndSubmit();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 6 – Navigate back from Radio Modality List
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 6, description = "Step 6: Navigate back from Radio Modality List",
          dependsOnMethods = "testFillModalityNameAndSubmit")
    public void testNavigateBackFromModalityList() {
        radioCasesPage.navigateBackFromModalityList();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 7 – Click Radio Category List card
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 7, description = "Step 7: Click Radio Category List card",
          dependsOnMethods = "testNavigateBackFromModalityList")
    public void testClickRadioCategoryListCard() {
        radioCasesPage.clickRadioCategoryListCard();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 8 – Search category by "88"
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 8, description = "Step 8: Search category by '88'",
          dependsOnMethods = "testClickRadioCategoryListCard")
    public void testSearchCategory() {
        radioCasesPage.searchCategory();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 9 – Edit category → "MentalHealthIssues" → Update
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 9, description = "Step 9: Edit category name to 'MentalHealthIssues' and update",
          dependsOnMethods = "testSearchCategory")
    public void testClickEditAndUpdate() {
        radioCasesPage.clickEditAndUpdate();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 10 – Delete first row category
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 10, description = "Step 10: Click Delete button on first row",
          dependsOnMethods = "testClickEditAndUpdate")
    public void testClickDeleteButton() {
        radioCasesPage.clickDeleteButton();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 11 – Add Category "MentalHealthIssues" → Submit
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 11, description = "Step 11: Add category 'MentalHealthIssues' and submit",
          dependsOnMethods = "testClickDeleteButton")
    public void testClickAddCategoryAndSubmit() {
        radioCasesPage.clickAddCategoryAndSubmit();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 12 – Navigate back from Radio Category List
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 12, description = "Step 12: Navigate back from Radio Category List",
          dependsOnMethods = "testClickAddCategoryAndSubmit")
    public void testNavigateBackFromCategoryList() {
        radioCasesPage.navigateBackFromCategoryList();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 13 – Click Add Case card
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 13, description = "Step 13: Click Add Case card",
          dependsOnMethods = "testNavigateBackFromCategoryList")
    public void testClickAddCaseCard() {
        radioCasesPage.clickAddCaseCard();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 14 – Search case by "65"
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 14, description = "Step 14: Search case by '65'",
          dependsOnMethods = "testClickAddCaseCard")
    public void testSearchCase() {
        radioCasesPage.searchCase();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 15 – Click Add button (open Add Case form)
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 15, description = "Step 15: Click Add button to open Add Case form",
          dependsOnMethods = "testSearchCase")
    public void testClickAddButton() {
        radioCasesPage.clickAddButton();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Step 16 – Fill Add Case form → Save Case
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 16, description = "Step 16: Fill Add Case form and click Save Case",
          dependsOnMethods = "testClickAddButton")
    public void testFillAddCaseFormAndSave() {
        radioCasesPage.fillAddCaseFormAndSave();
    }
}
