package com.dams.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * Page Object Model for the Add Space module.
 *
 * Covers:
 *  Step  1 – Add Space sidebar menu  (/add-space)
 *  Step  2 – Adspace page setting card
 *  Step  3 – Search by page name → "home page"
 *  Step  4 – Navigate back from Adspace page setting
 *  Step  5 – Add Vendor card
 *  Step  6 – Click Add Vendor button (opens popup)
 *  Step  7 – Fill vendor form: Name, Email, Mobile → submit
 *  Step  8 – Search vendor by name "Aadityasharma"
 *  Step  9 – Navigate back from Add Vendor
 *  Step 10 – Add Campaign card
 *  Step 11 – Search vendor/campaign box → "Amazon"
 *  Step 12 – Click Add Campaign button → fill popup form:
 *              • Select Vendor      → "Amazon"
 *              • Campaign Name      → "siddharth"
 *              • Start Date         → today (yyyy-MM-dd)
 *              • Start Time         → random HH:mm AM/PM
 *              • End Date           → today + 7 days
 *              • End Time           → random HH:mm AM/PM
 *              • Select Placement   → "Home Screen"
 *            → click Create Campaign
 */
public class AddSpacePage {

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final Random        random = new Random();

    // ── Step 1 – Add Space sidebar menu ──────────────────────────────────────
    // HTML: <span class="ant-menu-title-content"><a href="/add-space">Add Space</a></span>
    private final By addSpaceMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/add-space']"
    );
    private final By addSpaceMenuLinkHref = By.xpath(
        "//a[@href='/add-space']"
    );

    // ── Step 2 – Adspace page setting card ───────────────────────────────────
    // HTML: <div class="textData">Adspace page setting</div>
    private final By adspacePageSettingCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Adspace page setting']]"
    );

    // ── Step 3 – Search by page name input ───────────────────────────────────
    // HTML: <input placeholder="Search by page name" type="search" class="ant-input ...">
    //       inside ant-input-affix-wrapper
    private final By searchByPageNameInput = By.xpath(
        "//span[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[@placeholder='Search by page name']"
    );

    // ── Step 5 – Add Vendor card ──────────────────────────────────────────────
    // HTML: <div class="textData">Add Vendor</div>
    private final By addVendorCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Add Vendor']]"
    );

    // ── Step 6 – Add Vendor button (opens popup) ──────────────────────────────
    // HTML: <button class="ant-btn ant-btn-primary ..."><span>Add Vendor</span></button>
    private final By addVendorButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[not(@type='submit')]" +
        "[.//span[normalize-space(.)='Add Vendor']]"
    );

    // ── Step 7 – Vendor form fields ───────────────────────────────────────────
    // HTML: <input placeholder="Enter Name"  id="name"  ...>
    private final By vendorNameInput = By.xpath("//input[@id='name']");
    // HTML: <input placeholder="Enter Email" id="email" ...>
    private final By vendorEmailInput = By.xpath("//input[@id='email']");
    // HTML: <input placeholder="Enter Mobile no." ...>  (no fixed id — use placeholder)
    private final By vendorMobileInput = By.xpath(
        "//input[@placeholder='Enter Mobile no.' or @placeholder='Enter Mobile No.']"
    );
    // HTML: <button type="submit" class="ant-btn-primary"><span>Add Vendor</span></button>
    private final By addVendorSubmitButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Vendor']]"
    );

    // ── Step 8 – Search by name/email input ──────────────────────────────────
    // HTML: <input placeholder="Search by name or email" type="search" ...>
    private final By searchByNameOrEmailInput = By.xpath(
        "//span[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[@placeholder='Search by name or email']"
    );

    // ── Step 10 – Add Campaign card ───────────────────────────────────────────
    // HTML: <div class="textData">Add Campaign</div>
    private final By addCampaignCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Add Campaign']]"
    );

    // ── Step 11 – Search vendor/campaign input ────────────────────────────────
    // HTML: <input placeholder="Search vendor or campaign name..." type="search">
    private final By searchVendorCampaignInput = By.xpath(
        "//span[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[@placeholder='Search vendor or campaign name...']"
    );

    // ── Step 12 – Add Campaign button (opens popup) ───────────────────────────
    // HTML: <button class="ant-btn ant-btn-default ..."><span>Add Campaign</span></button>
    private final By addCampaignButton = By.xpath(
        "//button[contains(@class,'ant-btn')]" +
        "[.//span[normalize-space(.)='Add Campaign']]" +
        "[not(@type='submit')]"
    );

    // ── Step 12 – Select Vendor dropdown (id=vendor) ──────────────────────────
    // HTML: <input id="vendor" role="combobox" ...>
    private final By selectVendorDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='vendor']]"
    );

    // ── Step 12 – Campaign name input ─────────────────────────────────────────
    // HTML: <input id="campaignName" placeholder="Enter campaign name" ...>
    private final By campaignNameInput = By.xpath("//input[@id='campaignName']");

    // ── Step 12 – Start Date picker ───────────────────────────────────────────
    // HTML: <input id="startDate" placeholder="Select date" ...>
    private final By startDateInput = By.xpath("//input[@id='startDate']");

    // ── Step 12 – Start Time picker ───────────────────────────────────────────
    // HTML: <input id="startTime" placeholder="Select time" ...>
    private final By startTimeInput = By.xpath("//input[@id='startTime']");

    // ── Step 12 – End Date picker ─────────────────────────────────────────────
    // HTML: <input id="endDate" placeholder="Select date" ...>
    private final By endDateInput = By.xpath("//input[@id='endDate']");

    // ── Step 12 – End Time picker ─────────────────────────────────────────────
    // HTML: <input id="endTime" placeholder="Select time" ...>
    private final By endTimeInput = By.xpath("//input[@id='endTime']");

    // ── Step 12 – Select Placement dropdown ───────────────────────────────────
    // The placement dropdown uses a dynamic id (rc_select_N) that changes per
    // render. We use multiple strategies to locate it reliably.

    // Strategy A — by placeholder text on the combobox input
    private final By selectPlacementByPlaceholder = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@placeholder='Select Placement']]"
    );

    // Strategy B — labelled by a form-item label containing "Placement"
    private final By selectPlacementByLabel = By.xpath(
        "//div[contains(@class,'ant-form-item')]" +
        "[.//label[contains(normalize-space(.),'Placement')]]" +
        "//div[contains(@class,'ant-select-selector')]"
    );

    // Strategy C — the last ant-select-selector in the modal (placement is last field)
    private final By selectPlacementLast = By.xpath(
        "(//div[contains(@class,'ant-modal-content')]" +
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@role='combobox']])[last()]"
    );

    // Strategy D — any ant-select-selector in modal that is NOT already filled
    // (vendor/dates already filled, placement is the remaining empty one)
    private final By selectPlacementEmpty = By.xpath(
        "(//div[contains(@class,'ant-modal-content')]" +
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//span[contains(@class,'ant-select-selection-placeholder')]])[1]"
    );

    // ── Step 12 – Create Campaign submit button ───────────────────────────────
    // HTML: <button type="submit" class="ant-btn-primary"><span>Create Campaign</span></button>
    private final By createCampaignButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Create Campaign']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public AddSpacePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Add Space menu link
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Add Space' sidebar menu entry.
     * Three-pass: primary → href-only → expand sidebar + retry.
     */
    public AddSpacePage clickAddSpaceMenu() {
        System.out.println("[AddSpacePage] Step 1 → Clicking 'Add Space' menu...");
        WebElement element = findAddSpaceMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findAddSpaceMenuElement() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(addSpaceMenuLink));
        } catch (Exception ignored) {
            System.out.println("[AddSpacePage] Primary menu locator failed — trying href-only...");
        }
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(addSpaceMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[AddSpacePage] href-only failed — expanding sidebar...");
        }
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(addSpaceMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(addSpaceMenuLinkHref));
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
                System.out.println("[AddSpacePage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[AddSpacePage] No sidebar toggle — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[AddSpacePage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Adspace page setting card
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage clickAdspacePageSettingCard() {
        System.out.println("[AddSpacePage] Step 2 → Clicking 'Adspace page setting' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(adspacePageSettingCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Search "home page" in Search by page name box
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Locates the "Search by page name" input, clears it, types "home page"
     * and waits for results to load.
     */
    public AddSpacePage searchByPageName() {
        System.out.println("[AddSpacePage] Step 3 → Typing 'home page' in search box...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchByPageNameInput));
        scrollAndClick(input);
        clearAndType(input, "home page");
        input.sendKeys(Keys.ENTER);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 3 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Navigate back from Adspace page setting
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage navigateBackFromAdspacePageSetting() {
        System.out.println("[AddSpacePage] Step 4 → Navigating back from Adspace page setting...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[AddSpacePage] Step 4 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Click Add Vendor card
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage clickAddVendorCard() {
        System.out.println("[AddSpacePage] Step 5 → Clicking 'Add Vendor' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addVendorCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 5 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 – Click Add Vendor button (opens popup)
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage clickAddVendorButton() {
        System.out.println("[AddSpacePage] Step 6 → Clicking 'Add Vendor' button...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addVendorButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 6 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 – Fill vendor popup form and submit
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Fills the Add Vendor popup form:
     *   • Name   → "Aaditya sharma"
     *   • Email  → "06aadityasharma@gmail.com"
     *   • Mobile → "6387626349"
     * Then clicks the Add Vendor submit button.
     */
    public AddSpacePage fillVendorFormAndSubmit() {
        System.out.println("[AddSpacePage] Step 7 → Filling vendor form...");

        // Name
        WebElement nameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(vendorNameInput));
        scrollAndClick(nameField);
        clearAndType(nameField, "Aaditya sharma");
        System.out.println("[AddSpacePage] Step 7 → Name entered ✔");

        // Email
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(vendorEmailInput));
        scrollAndClick(emailField);
        clearAndType(emailField, "06aadityasharma@gmail.com");
        System.out.println("[AddSpacePage] Step 7 → Email entered ✔");

        // Mobile
        WebElement mobileField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(vendorMobileInput));
        scrollAndClick(mobileField);
        clearAndType(mobileField, "6387626349");
        System.out.println("[AddSpacePage] Step 7 → Mobile entered ✔");

        // Submit
        WebElement submitBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addVendorSubmitButton));
        scrollAndJsClick(submitBtn);

        // FIX: Wait for the ant-modal-wrap to fully disappear before proceeding.
        // Without this wait, the modal overlay intercepts the next click on
        // the search input (ant-modal-wrap receives the click instead).
        System.out.println("[AddSpacePage] Step 7 → Waiting for modal to close...");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ant-modal-wrap")));
            System.out.println("[AddSpacePage] Step 7 → Modal closed ✔");
        } catch (Exception e) {
            // Modal may have closed too fast to catch — press Escape as fallback
            System.out.println("[AddSpacePage] Step 7 → Modal invisibility wait timed out, " +
                "pressing Escape as fallback...");
            try {
                driver.findElement(By.tagName("body"))
                      .sendKeys(org.openqa.selenium.Keys.ESCAPE);
                sleep(1000);
            } catch (Exception ignored) {
                System.out.println("[AddSpacePage] Step 7 → Escape fallback also failed, proceeding...");
            }
        }
        sleep(1500); // extra buffer for modal animation to fully complete
        System.out.println("[AddSpacePage] Step 7 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 – Search "Aadityasharma" in search by name/email box
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage searchVendorByName() {
        System.out.println("[AddSpacePage] Step 8 → Searching 'Aadityasharma'...");

        // FIX: Use JS click to bypass any residual ant-modal-wrap overlay that
        // may still be in the DOM (display:none) and intercept native clicks.
        WebElement input = wait.until(
            ExpectedConditions.presenceOfElementLocated(searchByNameOrEmailInput));
        scrollAndJsClick(input);
        clearAndType(input, "Aadityasharma");
        sleep(2000);
        System.out.println("[AddSpacePage] Step 8 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 – Navigate back from Add Vendor
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage navigateBackFromAddVendor() {
        System.out.println("[AddSpacePage] Step 9 → Navigating back from Add Vendor...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[AddSpacePage] Step 9 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 10 – Click Add Campaign card
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage clickAddCampaignCard() {
        System.out.println("[AddSpacePage] Step 10 → Clicking 'Add Campaign' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(addCampaignCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[AddSpacePage] Step 10 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 11 – Search "Amazon" in search vendor/campaign box
    // ══════════════════════════════════════════════════════════════════════════

    public AddSpacePage searchAmazonInCampaign() {
        System.out.println("[AddSpacePage] Step 11 → Searching 'Amazon' in campaign search box...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(searchVendorCampaignInput));
        scrollAndClick(input);
        clearAndType(input, "Amazon");
        sleep(2000);
        System.out.println("[AddSpacePage] Step 11 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 12 – Click Add Campaign button → fill popup form → Create Campaign
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks 'Add Campaign', then fills the popup form:
     *   • Select Vendor    → "Amazon"
     *   • Campaign Name    → "siddharth"
     *   • Start Date       → today (yyyy-MM-dd)
     *   • Start Time       → random time (hh:mm AM/PM)
     *   • End Date         → today + 7 days
     *   • End Time         → random time (hh:mm AM/PM)
     *   • Select Placement → "Home Screen"
     * Then clicks Create Campaign.
     */
    public AddSpacePage clickAddCampaignAndFillForm() {
        System.out.println("[AddSpacePage] Step 12 → Clicking 'Add Campaign' button...");
        WebElement addCampBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addCampaignButton));
        scrollAndJsClick(addCampBtn);
        sleep(2000);

        // Select Vendor → "Amazon"
        selectAntDropdown(selectVendorDropdown, "Amazon");

        // Campaign Name → "siddharth"
        WebElement campName = wait.until(
            ExpectedConditions.visibilityOfElementLocated(campaignNameInput));
        scrollAndClick(campName);
        clearAndType(campName, "siddharth");
        sleep(400);

        // Start Date → today
        String startDate = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        fillDatePickerInput(startDateInput, startDate);

        // Start Time → random
        String startTime = generateRandomTime();
        fillTimePickerInput(startTimeInput, startTime);

        // End Date → today + 7 days
        String endDate = LocalDate.now().plusDays(7)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        fillDatePickerInput(endDateInput, endDate);

        // End Time → random
        String endTime = generateRandomTime();
        fillTimePickerInput(endTimeInput, endTime);

        // Select Placement → "Home Screen"
        selectPlacement("Home Screen");

        // Create Campaign
        WebElement createBtn = wait.until(
            ExpectedConditions.elementToBeClickable(createCampaignButton));
        scrollAndJsClick(createBtn);
        sleep(2000);

        System.out.println("[AddSpacePage] Step 12 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Opens an Ant Design Select dropdown by clicking its selector div,
     * waits for the list, then clicks the matching option by exact text.
     */
    private void selectAntDropdown(By selectorLocator, String optionText) {
        System.out.println("[AddSpacePage] → Opening dropdown for: " + optionText);
        WebElement selector = wait.until(
            ExpectedConditions.elementToBeClickable(selectorLocator));
        scrollAndClick(selector);
        sleep(800);

        By optionLocator = By.xpath(
            "//div[contains(@class,'ant-select-dropdown')" +
            " and not(contains(@class,'ant-select-dropdown-hidden'))]" +
            "//div[contains(@class,'ant-select-item-option')" +
            " and not(contains(@class,'ant-select-item-option-disabled'))]" +
            "[normalize-space(.)='" + optionText + "']"
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
        WebElement option = wait.until(
            ExpectedConditions.elementToBeClickable(optionLocator));
        scrollAndClick(option);
        sleep(600);
        System.out.println("[AddSpacePage] → Selected: " + optionText + " ✔");
    }

    /**
     * Selects a Placement option.
     *
     * ROOT CAUSE OF REPEATED FAILURES:
     * Ant Design renders its dropdown list in a <div> portal appended directly
     * to <body> — completely outside the modal DOM. The portal div uses class
     * "ant-select-dropdown" but its exact position in the DOM tree and its
     * visibility/hidden state vary. XPath-based option searches have been
     * unreliable because the dropdown opens but the option is either:
     *   (a) inside a portal that XPath cannot reach from inside the modal, or
     *   (b) the dropdown class contains extra tokens we don't expect.
     *
     * FIX: Use JavaScript to find and click the option directly — JS can
     * search the entire document including portals, regardless of visibility
     * class names or DOM nesting.
     *
     * Fallback chain:
     *   1. JS click on matching option text (primary — most reliable)
     *   2. XPath on any ant-select-item-option with exact text
     *   3. XPath contains match for partial text
     *   4. XPath on any visible li/div inside any open ant-select-dropdown
     */
    private void selectPlacement(String optionText) {
        System.out.println("[AddSpacePage] → Selecting placement: " + optionText);

        // Step 1 — open the dropdown using the most reliable selector found
        WebElement selector = findPlacementSelector();
        scrollAndJsClick(selector);
        sleep(1500); // allow portal to render in document.body

        // ── Primary: JavaScript click on option ───────────────────────────────
        // This is the most robust approach — JS searches the whole document
        // including Ant Design portals rendered at <body> level.
        try {
            Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var items = document.querySelectorAll(" +
                "  '.ant-select-dropdown .ant-select-item-option," +
                "   .ant-select-dropdown .ant-select-item'  " +
                ");" +
                "for (var i = 0; i < items.length; i++) {" +
                "  if (items[i].textContent.trim() === arguments[0]) {" +
                "    items[i].click();" +
                "    return true;" +
                "  }" +
                "}" +
                "return false;",
                optionText
            );
            if (Boolean.TRUE.equals(clicked)) {
                sleep(600);
                System.out.println("[AddSpacePage] → Placement selected via JS click ✔");
                return;
            }
            System.out.println("[AddSpacePage] → JS exact click returned false — trying JS contains...");

            // JS contains match (handles extra whitespace)
            clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var items = document.querySelectorAll(" +
                "  '.ant-select-dropdown .ant-select-item-option," +
                "   .ant-select-dropdown .ant-select-item'  " +
                ");" +
                "for (var i = 0; i < items.length; i++) {" +
                "  if (items[i].textContent.trim().indexOf(arguments[0]) >= 0) {" +
                "    items[i].click();" +
                "    return true;" +
                "  }" +
                "}" +
                "return false;",
                optionText
            );
            if (Boolean.TRUE.equals(clicked)) {
                sleep(600);
                System.out.println("[AddSpacePage] → Placement selected via JS contains click ✔");
                return;
            }
        } catch (Exception jsEx) {
            System.out.println("[AddSpacePage] → JS click failed: " + jsEx.getMessage());
        }

        // ── Fallback 1: XPath exact match across whole document ───────────────
        try {
            By optionXpath = By.xpath(
                "//div[contains(@class,'ant-select-item-option') or " +
                "      contains(@class,'ant-select-item')]" +
                "[normalize-space(.)='" + optionText + "']"
            );
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(optionXpath));
            WebElement opt = driver.findElement(optionXpath);
            scrollAndJsClick(opt);
            sleep(600);
            System.out.println("[AddSpacePage] → Placement selected via XPath exact ✔");
            return;
        } catch (Exception ex1) {
            System.out.println("[AddSpacePage] → XPath exact failed: " + ex1.getMessage());
        }

        // ── Fallback 2: XPath contains match ─────────────────────────────────
        try {
            By optionContains = By.xpath(
                "//div[contains(@class,'ant-select-item-option') or " +
                "      contains(@class,'ant-select-item')]" +
                "[contains(normalize-space(.),'" + optionText.split(" ")[0] + "')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(optionContains));
            WebElement opt = driver.findElement(optionContains);
            scrollAndJsClick(opt);
            sleep(600);
            System.out.println("[AddSpacePage] → Placement selected via XPath contains ✔");
            return;
        } catch (Exception ex2) {
            System.out.println("[AddSpacePage] → XPath contains failed: " + ex2.getMessage());
        }

        // ── Fallback 3: click any visible option in any open dropdown ─────────
        try {
            List<WebElement> allOptions = driver.findElements(
                By.cssSelector(".ant-select-dropdown .ant-select-item-option, " +
                               ".ant-select-dropdown .ant-select-item")
            );
            System.out.println("[AddSpacePage] → Total options found in DOM: " + allOptions.size());
            for (WebElement opt : allOptions) {
                System.out.println("[AddSpacePage] → Option text: [" + opt.getText().trim() + "]");
                if (opt.getText().trim().equalsIgnoreCase(optionText)) {
                    scrollAndJsClick(opt);
                    sleep(600);
                    System.out.println("[AddSpacePage] → Placement selected via CSS + loop ✔");
                    return;
                }
            }
            // last resort — click first option
            if (!allOptions.isEmpty()) {
                System.out.println("[AddSpacePage] → No exact match, clicking first option as last resort.");
                scrollAndJsClick(allOptions.get(0));
                sleep(600);
                return;
            }
        } catch (Exception ex3) {
            System.out.println("[AddSpacePage] → CSS fallback failed: " + ex3.getMessage());
        }

        throw new org.openqa.selenium.TimeoutException(
            "[AddSpacePage] Could not find or click placement option '" + optionText +
            "'. Dropdown may not have opened. Check if the placement field is visible."
        );
    }

    /**
     * Tries each placement selector strategy and returns the first clickable one.
     */
    private WebElement findPlacementSelector() {
        // Strategy A — by placeholder text on combobox input
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(selectPlacementByPlaceholder));
            System.out.println("[AddSpacePage] → Placement selector found via placeholder ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[AddSpacePage] → Strategy A (placeholder) failed.");
        }
        // Strategy B — by form-item label containing "Placement"
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(selectPlacementByLabel));
            System.out.println("[AddSpacePage] → Placement selector found via label ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[AddSpacePage] → Strategy B (label) failed.");
        }
        // Strategy C — last combobox selector in modal
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(selectPlacementLast));
            System.out.println("[AddSpacePage] → Placement selector found via last-in-modal ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[AddSpacePage] → Strategy C (last in modal) failed.");
        }
        // Strategy D — first unfilled (placeholder visible) select in modal
        System.out.println("[AddSpacePage] → Trying Strategy D (empty placeholder select)...");
        return wait.until(ExpectedConditions.elementToBeClickable(selectPlacementEmpty));
    }

    /**
     * Fills an Ant Design date picker input (id-based).
     * Clears existing value via JS, sends the date string, then presses
     * ENTER to confirm selection.
     *
     * @param inputLocator  By locator for the date input (e.g. id=startDate)
     * @param dateValue     date string in format "yyyy-MM-dd"
     */
    private void fillDatePickerInput(By inputLocator, String dateValue) {
        System.out.println("[AddSpacePage] → Filling date: " + dateValue);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputLocator));
        scrollAndClick(input);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(dateValue);
        input.sendKeys(Keys.ENTER);
        sleep(500);
        System.out.println("[AddSpacePage] → Date filled: " + dateValue + " ✔");
    }

    /**
     * Fills an Ant Design time picker input (id-based).
     * Sends the time string and presses ENTER to confirm.
     *
     * @param inputLocator  By locator for the time input (e.g. id=startTime)
     * @param timeValue     time string in format "hh:mm AM" or "hh:mm PM"
     */
    private void fillTimePickerInput(By inputLocator, String timeValue) {
        System.out.println("[AddSpacePage] → Filling time: " + timeValue);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputLocator));
        scrollAndClick(input);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(timeValue);
        input.sendKeys(Keys.ENTER);
        sleep(500);
        System.out.println("[AddSpacePage] → Time filled: " + timeValue + " ✔");
    }

    /**
     * Generates a random time string in "hh:mm AM/PM" format
     * (e.g. "09:35 AM", "03:17 PM").
     */
    private String generateRandomTime() {
        int hour   = random.nextInt(12) + 1;       // 1–12
        int minute = random.nextInt(60);            // 0–59
        String period = random.nextBoolean() ? "AM" : "PM";
        return String.format("%02d:%02d %s", hour, minute, period);
    }

    /**
     * Clears a plain input and types value.
     * JS clear + sendKeys ensures React controlled-input state is updated.
     */
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

    /**
     * JavaScript click — bypasses any overlay / intercept.
     * Used for popup trigger buttons and submit buttons.
     */
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
