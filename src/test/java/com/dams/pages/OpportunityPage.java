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
 * Page Object Model for the Opportunity module.
 *
 * Covers:
 *  Step 1 – Opportunity sidebar menu (/opportunity)
 *  Step 2 – Opportunity Email Configure
 *             • Add Opportunity Email Configure → enter email → Save
 *             • Search by email
 *             • Edit email → update value → Save
 *             • Delete email
 *             • Navigate back from Opportunity Email Configure
 *  Step 3 – Add Opportunity
 *             • Add Opportunity button → fill Category / Title / City / Description → Create
 *             • Search by title, category, city
 *             • Navigate back from Add Opportunity
 *  Step 4 – Opportunity Enquiry List
 *             • Download Excel
 *             • Search by name, email, phone, remark
 *             • Delete entry
 *             • Navigate back from Opportunity Enquiry List
 *  Step 5 – Add Category
 *             • Add Category button → enter Category Name → Save
 *             • Search by category name
 *             • Edit category
 *             • Delete category
 */
public class OpportunityPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Step 1 – Opportunity sidebar menu ────────────────────────────────────
    private final By opportunityMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/opportunity']"
    );
    private final By opportunityMenuLinkHref = By.xpath(
        "//a[@href='/opportunity']"
    );

    // ── Step 2 – Opportunity Email Configure card ────────────────────────────
    private final By opportunityEmailConfigureCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Opportunity Email Configure']]"
    );

    // Add Opportunity Email Configure button
    private final By addOpportunityEmailConfigureButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Opportunity Email Configure']]"
    );

    // Email input inside popup (id="email")
    private final By emailInput = By.xpath("//input[@id='email']");

    // Generic Save button
    private final By saveButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Save']]"
    );

    // Generic Search... input (used in Email Configure & Category list)
    private final By genericSearchInput = By.xpath(
        "//input[@placeholder='Search...']"
    );

    // Edit (pencil) button — row-level action
    private final By editButton = By.xpath(
        "//button[contains(@class,'InfoBtn')]" +
        "[.//span[@aria-label='edit']]"
    );

    // Delete (trash) button — row-level action
    private final By deleteButton = By.xpath(
        "//button[contains(@class,'ant-btn-dangerous')]" +
        "[.//span[@aria-label='delete']]"
    );

    // ── Step 3 – Add Opportunity card ────────────────────────────────────────
    private final By addOpportunityCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Add Opportunity']]"
    );

    // Add Opportunity button (opens the create form)
    private final By addOpportunityButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Opportunity']]"
    );

    // Category select (id="opportunity_cat")
    private final By categorySelectInput = By.xpath(
        "//input[@id='opportunity_cat']"
    );

    // Title input (id="title")
    private final By titleInput = By.xpath("//input[@id='title']");

    // City input (id="city")
    private final By cityInput = By.xpath("//input[@id='city']");

    // Description — Jodit rich text editor (contenteditable)
    private final By descriptionEditor = By.xpath(
        "//div[contains(@class,'jodit-wysiwyg') and @contenteditable='true']"
    );

    // Create button
    private final By createButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Create']]"
    );

    // Search by title, category, city... input
    private final By searchOpportunityInput = By.xpath(
        "//input[@placeholder='Search by title, category, city...']"
    );

    // ── Step 4 – Opportunity Enquiry List card ───────────────────────────────
    private final By opportunityEnquiryListCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Opportunity Enquiry List']]"
    );

    // Download Excel button
    private final By downloadExcelButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Download Excel']]"
    );

    // Search by name, email, phone, remark... input
    private final By searchEnquiryInput = By.xpath(
        "//input[@placeholder='Search by name, email, phone, remark...']"
    );

    // Delete button (text + icon variant used on Enquiry List rows)
    private final By enquiryDeleteButton = By.xpath(
        "//button[contains(@class,'ant-btn-dangerous')]" +
        "[.//span[@aria-label='delete']]" +
        "[.//span[normalize-space(.)='Delete']]"
    );

    // ── Step 5 – Add Category card ───────────────────────────────────────────
    private final By addCategoryCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Add Category']]"
    );

    // Add Category button (opens popup)
    private final By addCategoryButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Category']]"
    );

    // Category Name input (id="categoryName")
    private final By categoryNameInput = By.xpath(
        "//input[@id='categoryName']"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public OpportunityPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Opportunity menu
    // ══════════════════════════════════════════════════════════════════════════

    public OpportunityPage clickOpportunityMenu() {
        System.out.println("[OpportunityPage] Step 1 → Clicking 'Opportunity' menu...");
        WebElement element = findOpportunityMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findOpportunityMenuElement() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(opportunityMenuLink));
        } catch (Exception ignored) {
            System.out.println("[OpportunityPage] Primary menu locator failed — trying href-only...");
        }
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(opportunityMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[OpportunityPage] href-only failed — expanding sidebar...");
        }
        tryExpandSidebar();
        sleep(1500);
        return wait.until(ExpectedConditions.elementToBeClickable(opportunityMenuLinkHref));
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
                System.out.println("[OpportunityPage] Sidebar expand toggle clicked.");
            }
        } catch (Exception e) {
            System.out.println("[OpportunityPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Opportunity Email Configure
    // ══════════════════════════════════════════════════════════════════════════

    public OpportunityPage clickOpportunityEmailConfigureCard() {
        System.out.println("[OpportunityPage] Step 2 → Clicking 'Opportunity Email Configure' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(opportunityEmailConfigureCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] Step 2 → PASSED ✔");
        return this;
    }

    public OpportunityPage clickAddOpportunityEmailConfigureButton() {
        System.out.println("[OpportunityPage] → Clicking 'Add Opportunity Email Configure' button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addOpportunityEmailConfigureButton));
        scrollAndJsClick(element);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage enterEmailAndSave(String email) {
        System.out.println("[OpportunityPage] → Entering email: " + email);
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailInput));
        scrollAndClick(input);
        clearAndType(input, email);

        WebElement save = wait.until(
            ExpectedConditions.elementToBeClickable(saveButton));
        scrollAndJsClick(save);
        waitForModalToClose();
        sleep(1000);
        System.out.println("[OpportunityPage] → Email saved ✔");
        return this;
    }

    public OpportunityPage searchInGenericBox(String value) {
        System.out.println("[OpportunityPage] → Searching: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(genericSearchInput));
        scrollAndClick(input);
        clearAndType(input, value);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage clickEditButton() {
        System.out.println("[OpportunityPage] → Clicking edit button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(editButton));
        scrollAndJsClick(element);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage updateEmailAndSave(String email) {
        System.out.println("[OpportunityPage] → Updating email: " + email);
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailInput));
        scrollAndClick(input);
        clearAndType(input, email);

        WebElement save = wait.until(
            ExpectedConditions.elementToBeClickable(saveButton));
        scrollAndJsClick(save);
        waitForModalToClose();
        sleep(1000);
        System.out.println("[OpportunityPage] → Email updated ✔");
        return this;
    }

    public OpportunityPage clickDeleteButton() {
        System.out.println("[OpportunityPage] → Clicking delete button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(deleteButton));
        scrollAndJsClick(element);
        sleep(1000);
        confirmDeleteIfPromptPresent();
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    /** Ant Design Popconfirm / Modal.confirm sometimes appears before delete executes. */
    private void confirmDeleteIfPromptPresent() {
        try {
            By confirmOk = By.xpath(
                "//div[contains(@class,'ant-popconfirm') or contains(@class,'ant-modal-confirm')]" +
                "//button[contains(@class,'ant-btn-primary') or contains(@class,'ant-btn-dangerous')]"
            );
            WebElement ok = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(confirmOk));
            scrollAndJsClick(ok);
            sleep(800);
        } catch (Exception ignored) {
            // No confirmation prompt appeared — delete likely executed directly.
        }
    }

    public OpportunityPage navigateBackFromOpportunityEmailConfigure() {
        System.out.println("[OpportunityPage] → Navigating back from Opportunity Email Configure...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Add Opportunity
    // ══════════════════════════════════════════════════════════════════════════

    public OpportunityPage clickAddOpportunityCard() {
        System.out.println("[OpportunityPage] Step 3 → Clicking 'Add Opportunity' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addOpportunityCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] Step 3 → PASSED ✔");
        return this;
    }

    public OpportunityPage clickAddOpportunityButton() {
        System.out.println("[OpportunityPage] → Clicking 'Add Opportunity' button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addOpportunityButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage selectCategory(String categoryName) {
        System.out.println("[OpportunityPage] → Selecting category: " + categoryName);
        WebElement selectInput = wait.until(
            ExpectedConditions.elementToBeClickable(categorySelectInput));
        scrollAndClick(selectInput);
        sleep(800);

        By optionLocator = By.xpath(
            "//div[contains(@class,'ant-select-dropdown') and " +
            "not(contains(@class,'ant-select-dropdown-hidden'))]" +
            "//div[contains(@class,'ant-select-item-option')]" +
            "[normalize-space(.)='" + categoryName + "']"
        );
        try {
            WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(optionLocator));
            scrollAndJsClick(option);
        } catch (Exception e) {
            // Fallback: type to filter, then press Enter
            selectInput.sendKeys(categoryName);
            sleep(800);
            selectInput.sendKeys(Keys.ENTER);
        }
        sleep(600);
        System.out.println("[OpportunityPage] → Category selected ✔");
        return this;
    }

    public OpportunityPage enterTitle(String title) {
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(titleInput));
        scrollAndClick(input);
        clearAndType(input, title);
        System.out.println("[OpportunityPage] → Title entered: " + title + " ✔");
        return this;
    }

    public OpportunityPage enterCity(String city) {
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(cityInput));
        scrollAndClick(input);
        clearAndType(input, city);
        System.out.println("[OpportunityPage] → City entered: " + city + " ✔");
        return this;
    }

    public OpportunityPage enterDescription(String description) {
        WebElement editor = wait.until(
            ExpectedConditions.visibilityOfElementLocated(descriptionEditor));
        scrollAndClick(editor);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].innerHTML = '<p>' + arguments[1] + '</p>';" +
            "var evt = new Event('input', { bubbles: true });" +
            "arguments[0].dispatchEvent(evt);",
            editor, description
        );
        sleep(500);
        System.out.println("[OpportunityPage] → Description entered: " + description + " ✔");
        return this;
    }

    public OpportunityPage clickCreateButton() {
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(createButton));
        scrollAndJsClick(element);
        waitForModalToClose();
        sleep(1500);
        System.out.println("[OpportunityPage] → Opportunity created ✔");
        return this;
    }

    public OpportunityPage searchOpportunity(String value) {
        System.out.println("[OpportunityPage] → Searching opportunity: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchOpportunityInput));
        scrollAndClick(input);
        clearAndType(input, value);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage navigateBackFromAddOpportunity() {
        System.out.println("[OpportunityPage] → Navigating back from Add Opportunity...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Opportunity Enquiry List
    // ══════════════════════════════════════════════════════════════════════════

    public OpportunityPage clickOpportunityEnquiryListCard() {
        System.out.println("[OpportunityPage] Step 4 → Clicking 'Opportunity Enquiry List' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(opportunityEnquiryListCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] Step 4 → PASSED ✔");
        return this;
    }

    public OpportunityPage clickDownloadExcel() {
        System.out.println("[OpportunityPage] → Clicking 'Download Excel'...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(downloadExcelButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage searchEnquiry(String value) {
        System.out.println("[OpportunityPage] → Searching enquiry: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchEnquiryInput));
        scrollAndClick(input);
        clearAndType(input, value);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage clickEnquiryDeleteButton() {
        System.out.println("[OpportunityPage] → Clicking enquiry delete button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(enquiryDeleteButton));
        scrollAndJsClick(element);
        sleep(1000);
        confirmDeleteIfPromptPresent();
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage navigateBackFromOpportunityEnquiryList() {
        System.out.println("[OpportunityPage] → Navigating back from Opportunity Enquiry List...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Add Category
    // ══════════════════════════════════════════════════════════════════════════

    public OpportunityPage clickAddCategoryCard() {
        System.out.println("[OpportunityPage] Step 5 → Clicking 'Add Category' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addCategoryCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[OpportunityPage] Step 5 → PASSED ✔");
        return this;
    }

    public OpportunityPage clickAddCategoryButton() {
        System.out.println("[OpportunityPage] → Clicking 'Add Category' button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addCategoryButton));
        scrollAndJsClick(element);
        sleep(1500);
        System.out.println("[OpportunityPage] → PASSED ✔");
        return this;
    }

    public OpportunityPage enterCategoryNameAndSave(String categoryName) {
        System.out.println("[OpportunityPage] → Entering category name: " + categoryName);
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
        scrollAndClick(input);
        clearAndType(input, categoryName);

        WebElement save = wait.until(
            ExpectedConditions.elementToBeClickable(saveButton));
        scrollAndJsClick(save);
        waitForModalToClose();
        sleep(1000);
        System.out.println("[OpportunityPage] → Category saved ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void waitForModalToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ant-modal-wrap")));
        } catch (Exception e) {
            try {
                driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                sleep(800);
            } catch (Exception ignored) {
                // proceed regardless
            }
        }
    }

    private void clearAndType(WebElement input, String value) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(value);
        sleep(300);
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

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
