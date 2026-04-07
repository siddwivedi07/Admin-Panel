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
 * Page Object Model for the Radio Cases module.
 *
 * Covers:
 *  Step  1  – Radio Cases sidebar menu  (/radio-cases)
 *  Step  2  – Radio Modality List card
 *  Step  3  – Search by Modality ID or Name → "Pradeep"
 *  Step  4  – Add Modality button → popup
 *  Step  5  – Fill Modality Name → "Pradeep" → Submit
 *  Step  6  – Navigate back from Radio Modality List
 *  Step  7  – Radio Category List card
 *  Step  8  – Search by Category ID or Name → "88"
 *  Step  9  – Edit button → edit category name "MentalHealthIssues" → Update
 *  Step 10  – Delete button (first row)
 *  Step 11  – Add Category button → fill "MentalHealthIssues" → Submit
 *  Step 12  – Navigate back from Radio Category List
 *  Step 13  – Add Case card
 *  Step 14  – Search by Case ID or Name → "65"
 *  Step 15  – Add button (open Add Case form)
 *  Step 16  – Fill Add Case form:
 *               • Case Title     → "Acute Necrotizing Encephalopathy in a Young Adult"
 *               • Modality       → "Pradeep"  (Ant Select, id=modality)
 *               • Body Part      → "testng body"
 *               • Difficulty     → "Beginner" (Ant Select)
 *               • Category       → "MentalHealthIssues" (Ant Select)
 *               • Pacsbin URL    → "https://www.pacsbin.com/c/ZkhD47HHtn"
 *               • Diagnosis      → "It is correct"
 *               • Key Findings   → "it is correct"
 *             → Save Case
 */
public class RadioCasesPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Step 1 – Radio Cases sidebar menu ─────────────────────────────────────
    // HTML: <span class="ant-menu-title-content"><a href="/radio-cases">Radio Cases</a></span>
    private final By radioCasesMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/radio-cases']"
    );
    private final By radioCasesMenuLinkHref = By.xpath(
        "//a[@href='/radio-cases']"
    );

    // ── Step 2 – Radio Modality List card ─────────────────────────────────────
    // HTML: <div class="textData">Radio Modality List</div>
    private final By radioModalityListCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Radio Modality List']]"
    );

    // ── Step 3 – Search by Modality ID or Name input ──────────────────────────
    // HTML: <input placeholder="Search by Modality ID or Name" class="ant-input ...">
    private final By searchModalityInput = By.xpath(
        "//input[@placeholder='Search by Modality ID or Name']"
    );

    // ── Step 4 – Add Modality button ──────────────────────────────────────────
    // HTML: <button class="ant-btn ant-btn-sm ..."><span>Add Modality</span></button>
    //       with a plus-circle anticon
    private final By addModalityButton = By.xpath(
        "//button[contains(@class,'ant-btn')]" +
        "[.//span[normalize-space(.)='Add Modality']]" +
        "[.//*[contains(@class,'anticon-plus-circle')]]"
    );

    // ── Step 5 – Modality Name input (in popup) ───────────────────────────────
    // HTML: <input placeholder="Modality name.." id="case_modality" ...>
    private final By modalityNameInput = By.xpath(
        "//input[@id='case_modality']"
    );

    // ── Step 5 – Submit button (generic — used in modality & category popups) ─
    // HTML: <button type="submit" class="ant-btn-primary"><span>Submit</span></button>
    private final By submitButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Submit']]"
    );

    // ── Step 7 – Radio Category List card ─────────────────────────────────────
    // HTML: <div class="textData">Radio Category List</div>
    private final By radioCategoryListCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Radio Category List']]"
    );

    // ── Step 8 – Search by Category ID or Name input ──────────────────────────
    // HTML: <input placeholder="Search by Category ID or Name" class="ant-input ...">
    private final By searchCategoryInput = By.xpath(
        "//input[@placeholder='Search by Category ID or Name']"
    );

    // ── Step 9 – Edit button (first row in table) ─────────────────────────────
    // HTML: <button class="ant-btn ant-btn-sm ...">
    //         <span class="anticon anticon-edit"/>
    //         <span> Edit</span>
    //       </button>
    private final By editButton = By.xpath(
        "(//button[contains(@class,'ant-btn')]" +
        "[.//*[contains(@class,'anticon-edit')]]" +
        "[.//span[contains(normalize-space(.),'Edit')]])[1]"
    );

    // ── Step 9 – Case category name input (in Edit popup) ────────────────────
    // HTML: <input placeholder="Enter ..." id="case_category_name" ...>
    private final By caseCategoryNameInput = By.xpath(
        "//input[@id='case_category_name']"
    );

    // ── Step 9 – Update button ────────────────────────────────────────────────
    // HTML: <button type="submit" class="ant-btn-primary"><span>Update</span></button>
    private final By updateButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Update']]"
    );

    // ── Step 10 – Delete button (first row in table) ──────────────────────────
    // HTML: <button class="ant-btn ant-btn-sm ant-btn-dangerous ...">
    //         <span>Delete</span>
    //       </button>
    private final By deleteButton = By.xpath(
        "(//button[contains(@class,'ant-btn')]" +
        "[contains(@class,'ant-btn-dangerous')]" +
        "[.//span[normalize-space(.)='Delete']])[1]"
    );

    // ── Step 11 – Add Category button ─────────────────────────────────────────
    // HTML: <button class="ant-btn ant-btn-sm ...">
    //         <span class="anticon-plus-circle"/>
    //         <span>Add Category</span>
    //       </button>
    private final By addCategoryButton = By.xpath(
        "//button[contains(@class,'ant-btn')]" +
        "[.//span[normalize-space(.)='Add Category']]" +
        "[.//*[contains(@class,'anticon-plus-circle')]]"
    );

    // ── Step 11 – Category name input (in popup) ──────────────────────────────
    // HTML: <input placeholder="Category name.." id="case_category" ...>
    private final By categoryNameInput = By.xpath(
        "//input[@id='case_category']"
    );

    // ── Step 13 – Add Case card ───────────────────────────────────────────────
    // HTML: <div class="textData">Add Case</div>
    private final By addCaseCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Add Case']]"
    );

    // ── Step 14 – Search by Case ID or Name input ─────────────────────────────
    // HTML: <input placeholder="Search by Case ID or Name" class="ant-input ...">
    private final By searchCaseInput = By.xpath(
        "//input[@placeholder='Search by Case ID or Name']"
    );

    // ── Step 15 – Add button (opens Add Case form) ────────────────────────────
    // HTML: <button class="ant-btn ..."><span>Add</span></button>
    //       with anticon-plus-circle icon
    private final By addButton = By.xpath(
        "//button[contains(@class,'ant-btn')]" +
        "[.//span[normalize-space(.)='Add']]" +
        "[.//*[contains(@class,'anticon-plus-circle')]]"
    );

    // ── Step 16 – Case Title input ────────────────────────────────────────────
    // HTML: <input placeholder="Enter Case Title" id="case_title" ...>
    private final By caseTitleInput = By.xpath(
        "//input[@id='case_title']"
    );

    // ── Step 16 – Modality dropdown (id=modality) ─────────────────────────────
    // HTML: <input id="modality" role="combobox" ...>
    private final By modalityDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='modality']]"
    );

    // ── Step 16 – Body Part input ─────────────────────────────────────────────
    // HTML: <input placeholder="e.g., Chest, Brain, Abdomen" id="body_part" ...>
    private final By bodyPartInput = By.xpath(
        "//input[@id='body_part']"
    );

    // ── Step 16 – Difficulty dropdown ────────────────────────────────────────
    // HTML: <span class="ant-select-selection-item" title="Beginner">Beginner</span>
    //       The input has no fixed id — locate by sibling selection-item or
    //       by the label "Difficulty" in the form
    private final By difficultyDropdown = By.xpath(
        "//div[contains(@class,'ant-form-item')]" +
        "[.//label[contains(normalize-space(.),'Difficulty')]]" +
        "//div[contains(@class,'ant-select-selector')]"
    );

    // ── Step 16 – Category dropdown ───────────────────────────────────────────
    // HTML: <span class="ant-select-selection-item" title="MentalHealthIssues">
    //       The input has no fixed id — locate by label "Category" in form
    private final By categoryDropdown = By.xpath(
        "//div[contains(@class,'ant-form-item')]" +
        "[.//label[contains(normalize-space(.),'Category')]]" +
        "//div[contains(@class,'ant-select-selector')]"
    );

    // ── Step 16 – Pacsbin URL input ───────────────────────────────────────────
    // HTML: <input placeholder="Enter DICOM file reference / URL / ID"
    //              id="pacsbin_url" ...>
    private final By pacsbinUrlInput = By.xpath(
        "//input[@id='pacsbin_url']"
    );

    // ── Step 16 – Diagnosis textarea ──────────────────────────────────────────
    // HTML: <textarea id="diagnosis" placeholder="The correct diagnosis..." ...>
    private final By diagnosisTextarea = By.xpath(
        "//textarea[@id='diagnosis']"
    );

    // ── Step 16 – Key Findings textarea ───────────────────────────────────────
    // HTML: <textarea id="key_findings" placeholder="Important imaging findings..." ...>
    private final By keyFindingsTextarea = By.xpath(
        "//textarea[@id='key_findings']"
    );

    // ── Step 16 – Save Case submit button ─────────────────────────────────────
    // HTML: <button type="submit" class="ant-btn-primary"><span>Save Case</span></button>
    private final By saveCaseButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Save Case']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public RadioCasesPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Radio Cases menu link
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickRadioCasesMenu() {
        System.out.println("[RadioCasesPage] Step 1 → Clicking 'Radio Cases' menu...");
        WebElement element = findRadioCasesMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findRadioCasesMenuElement() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(radioCasesMenuLink));
        } catch (Exception ignored) {
            System.out.println("[RadioCasesPage] Primary menu locator failed — trying href-only...");
        }
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(radioCasesMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[RadioCasesPage] href-only failed — expanding sidebar...");
        }
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(radioCasesMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(radioCasesMenuLinkHref));
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
                System.out.println("[RadioCasesPage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[RadioCasesPage] No sidebar toggle — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[RadioCasesPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Radio Modality List card
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickRadioModalityListCard() {
        System.out.println("[RadioCasesPage] Step 2 → Clicking 'Radio Modality List' card...");
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(radioModalityListCard));
        scrollAndClick(el);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Search "Pradeep" in Search by Modality ID or Name box
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage searchModality() {
        System.out.println("[RadioCasesPage] Step 3 → Searching 'Pradeep' in modality search...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchModalityInput));
        scrollAndClick(input);
        clearAndType(input, "Pradeep");
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 3 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Click Add Modality button (opens popup)
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickAddModalityButton() {
        System.out.println("[RadioCasesPage] Step 4 → Clicking 'Add Modality' button...");
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(addModalityButton));
        scrollAndJsClick(el);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 4 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Fill Modality Name → "Pradeep" → click Submit
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage fillModalityNameAndSubmit() {
        System.out.println("[RadioCasesPage] Step 5 → Filling modality name 'Pradeep'...");
        WebElement nameInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(modalityNameInput));
        scrollAndClick(nameInput);
        clearAndType(nameInput, "Pradeep");
        sleep(500);

        System.out.println("[RadioCasesPage] Step 5 → Clicking Submit...");
        waitForModalClose_thenClick(submitButton);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 5 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 – Navigate back from Radio Modality List
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage navigateBackFromModalityList() {
        System.out.println("[RadioCasesPage] Step 6 → Navigating back from Radio Modality List...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[RadioCasesPage] Step 6 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 – Click Radio Category List card
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickRadioCategoryListCard() {
        System.out.println("[RadioCasesPage] Step 7 → Clicking 'Radio Category List' card...");
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(radioCategoryListCard));
        scrollAndClick(el);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 7 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 – Search "88" in Search by Category ID or Name box
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage searchCategory() {
        System.out.println("[RadioCasesPage] Step 8 → Searching '88' in category search...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchCategoryInput));
        scrollAndClick(input);
        clearAndType(input, "88");
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 8 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 – Click Edit button → edit to "MentalHealthIssues" → Update
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickEditAndUpdate() {
        System.out.println("[RadioCasesPage] Step 9 → Clicking first 'Edit' button...");
        WebElement editBtn = wait.until(
            ExpectedConditions.elementToBeClickable(editButton));
        scrollAndJsClick(editBtn);
        sleep(2000);

        System.out.println("[RadioCasesPage] Step 9 → Editing category name → 'MentalHealthIssues'...");
        WebElement catInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(caseCategoryNameInput));
        scrollAndClick(catInput);
        clearAndType(catInput, "MentalHealthIssues");
        sleep(500);

        System.out.println("[RadioCasesPage] Step 9 → Clicking Update...");
        WebElement updateBtn = wait.until(
            ExpectedConditions.elementToBeClickable(updateButton));
        scrollAndJsClick(updateBtn);
        waitForModalToClose();
        sleep(1500);
        System.out.println("[RadioCasesPage] Step 9 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 10 – Click Delete button (first row)
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickDeleteButton() {
        System.out.println("[RadioCasesPage] Step 10 → Clicking first 'Delete' button...");
        WebElement delBtn = wait.until(
            ExpectedConditions.elementToBeClickable(deleteButton));
        scrollAndJsClick(delBtn);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 10 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 11 – Click Add Category → fill "MentalHealthIssues" → Submit
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickAddCategoryAndSubmit() {
        System.out.println("[RadioCasesPage] Step 11 → Clicking 'Add Category' button...");
        WebElement addCatBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addCategoryButton));
        scrollAndJsClick(addCatBtn);
        sleep(2000);

        System.out.println("[RadioCasesPage] Step 11 → Filling category name 'MentalHealthIssues'...");
        WebElement catInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
        scrollAndClick(catInput);
        clearAndType(catInput, "MentalHealthIssues");
        sleep(500);

        System.out.println("[RadioCasesPage] Step 11 → Clicking Submit...");
        waitForModalClose_thenClick(submitButton);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 11 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 12 – Navigate back from Radio Category List
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage navigateBackFromCategoryList() {
        System.out.println("[RadioCasesPage] Step 12 → Navigating back from Radio Category List...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[RadioCasesPage] Step 12 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 13 – Click Add Case card
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickAddCaseCard() {
        System.out.println("[RadioCasesPage] Step 13 → Clicking 'Add Case' card...");
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(addCaseCard));
        scrollAndClick(el);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 13 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 14 – Search "65" in Search by Case ID or Name box
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage searchCase() {
        System.out.println("[RadioCasesPage] Step 14 → Searching '65' in case search...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchCaseInput));
        scrollAndClick(input);
        clearAndType(input, "65");
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 14 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 15 – Click Add button (opens Add Case form)
    // ══════════════════════════════════════════════════════════════════════════

    public RadioCasesPage clickAddButton() {
        System.out.println("[RadioCasesPage] Step 15 → Clicking 'Add' button...");
        WebElement el = wait.until(
            ExpectedConditions.elementToBeClickable(addButton));
        scrollAndJsClick(el);
        sleep(2000);
        System.out.println("[RadioCasesPage] Step 15 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 16 – Fill Add Case form and click Save Case
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Fills the complete Add Case form:
     *   • Case Title   → "Acute Necrotizing Encephalopathy in a Young Adult"
     *   • Modality     → "Pradeep"       (Ant Select, id=modality)
     *   • Body Part    → "testng body"
     *   • Difficulty   → "Beginner"      (Ant Select, by label)
     *   • Category     → "MentalHealthIssues" (Ant Select, by label)
     *   • Pacsbin URL  → "https://www.pacsbin.com/c/ZkhD47HHtn"
     *   • Diagnosis    → "It is correct"
     *   • Key Findings → "it is correct"
     * Then clicks Save Case.
     */
    public RadioCasesPage fillAddCaseFormAndSave() {
        System.out.println("[RadioCasesPage] Step 16 → Filling Add Case form...");

        // Case Title
        WebElement titleInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(caseTitleInput));
        scrollAndClick(titleInput);
        clearAndType(titleInput,
            "Acute Necrotizing Encephalopathy in a Young Adult");
        sleep(400);

        // Modality → "Pradeep"
        selectAntDropdown(modalityDropdown, "Pradeep");

        // Body Part
        WebElement bodyInput = wait.until(
            ExpectedConditions.elementToBeClickable(bodyPartInput));
        scrollAndClick(bodyInput);
        clearAndType(bodyInput, "testng body");
        sleep(400);

        // Difficulty → "Beginner"
        selectAntDropdown(difficultyDropdown, "Beginner");

        // Category → "MentalHealthIssues"
        selectAntDropdown(categoryDropdown, "MentalHealthIssues");

        // Pacsbin URL
        WebElement urlInput = wait.until(
            ExpectedConditions.elementToBeClickable(pacsbinUrlInput));
        scrollAndClick(urlInput);
        clearAndType(urlInput, "https://www.pacsbin.com/c/ZkhD47HHtn");
        sleep(400);

        // Diagnosis
        WebElement diagInput = wait.until(
            ExpectedConditions.elementToBeClickable(diagnosisTextarea));
        scrollAndClick(diagInput);
        clearAndType(diagInput, "It is correct");
        sleep(400);

        // Key Findings
        WebElement keyInput = wait.until(
            ExpectedConditions.elementToBeClickable(keyFindingsTextarea));
        scrollAndClick(keyInput);
        clearAndType(keyInput, "it is correct");
        sleep(400);

        // Save Case
        System.out.println("[RadioCasesPage] Step 16 → Clicking 'Save Case'...");
        WebElement saveBtn = wait.until(
            ExpectedConditions.elementToBeClickable(saveCaseButton));
        scrollAndJsClick(saveBtn);
        sleep(2000);

        System.out.println("[RadioCasesPage] Step 16 → PASSED ✔");
        return this;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Opens an Ant Design Select dropdown by clicking its selector div,
     * waits for the list portal to render, then clicks the matching option.
     *
     * Uses JavaScript as the primary click mechanism for both the selector
     * and the option — this bypasses overlay/portal visibility issues.
     *
     * @param selectorLocator  By locator for div.ant-select-selector
     * @param optionText       exact visible text of the option to select
     */
    private void selectAntDropdown(By selectorLocator, String optionText) {
        System.out.println("[RadioCasesPage] → Opening dropdown for: " + optionText);
        WebElement selector = wait.until(
            ExpectedConditions.elementToBeClickable(selectorLocator));
        scrollAndJsClick(selector);
        sleep(800);

        // Primary: JS click on option (bypasses portal visibility issues)
        try {
            Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var items = document.querySelectorAll(" +
                "  '.ant-select-dropdown .ant-select-item-option," +
                "   .ant-select-dropdown .ant-select-item'" +
                ");" +
                "for (var i = 0; i < items.length; i++) {" +
                "  if (items[i].textContent.trim() === arguments[0]) {" +
                "    items[i].click(); return true;" +
                "  }" +
                "}" +
                "return false;",
                optionText
            );
            if (Boolean.TRUE.equals(clicked)) {
                sleep(500);
                System.out.println("[RadioCasesPage] → Selected '" + optionText + "' via JS ✔");
                return;
            }
        } catch (Exception jsEx) {
            System.out.println("[RadioCasesPage] → JS click failed: " + jsEx.getMessage());
        }

        // Fallback: XPath search across whole document
        By optionLocator = By.xpath(
            "//div[contains(@class,'ant-select-item-option') or " +
            "      contains(@class,'ant-select-item')]" +
            "[normalize-space(.)='" + optionText + "']"
        );
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(optionLocator));
            WebElement opt = driver.findElement(optionLocator);
            scrollAndJsClick(opt);
            sleep(500);
            System.out.println("[RadioCasesPage] → Selected '" + optionText + "' via XPath ✔");
        } catch (Exception xpEx) {
            throw new org.openqa.selenium.TimeoutException(
                "[RadioCasesPage] Could not find dropdown option '" + optionText + "'.");
        }
    }

    /**
     * Clicks a submit/update button after waiting for any open modal overlay
     * to fully settle. Uses JS click for reliability.
     */
    private void waitForModalClose_thenClick(By buttonLocator) {
        // Wait for any previous modal to fully close
        waitForModalToClose();
        WebElement btn = wait.until(
            ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollAndJsClick(btn);
    }

    /**
     * Waits for ant-modal-wrap to disappear after a submit action.
     * Safe to call even if no modal is open — silently no-ops on timeout.
     */
    private void waitForModalToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ant-modal-wrap")));
            sleep(500);
        } catch (Exception ignored) {
            // modal may have closed too fast, or wasn't open — continue
            try {
                driver.findElement(By.tagName("body"))
                      .sendKeys(Keys.ESCAPE);
                sleep(800);
            } catch (Exception ignored2) { /* no-op */ }
        }
    }

    /**
     * Clears input and types value.
     * JS clear + sendKeys ensures React controlled-input state is updated.
     */
    private void clearAndType(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", element);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.clear();
        element.sendKeys(value);
        sleep(300);
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    /** JS click — bypasses overlay/intercept issues. */
    private void scrollAndJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", element);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
