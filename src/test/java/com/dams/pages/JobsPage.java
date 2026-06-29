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
 * Page Object Model for the Jobs module.
 *
 * Flow:
 *  Step 1 – Click Jobs sidebar menu  (/jobs)
 *  Step 2 – Click Applied Job card
 *  Step 3 – Search "Test" in Applied Job search box  (placeholder="Search job")
 *  Step 4 – Navigate back from Applied Job
 *  Step 5 – Click Job Post card
 *  Step 6 – Search "Fortis Hospital" in Job Post search box
 *  Step 7 – Click Add Job button, fill form:
 *             • Company/Hospital  → "Fortis"        (plain text input)
 *             • Job Title         → "Devops"        (plain text input)
 *             • Job Location      → "Noida"         (plain text input)
 *             • Job Type          → "Full Time"     (Ant Select dropdown)
 *             • Job Experience    → "2"             (plain text input)
 *             • Salary Type       → "Monthly"       (Ant Select dropdown)
 *             • Salary Range      → "500-1000"      (plain text input)
 *             • Show Salary       → "Yes"           (Ant Select dropdown)
 *             • Job Highlights    → "selenium"      (textarea)
 *             • Key Accountabilities → "selenium"   (textarea)
 *  Step 9 – Click Post Job submit button
 */
public class JobsPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Step 1 – Jobs sidebar menu ────────────────────────────────────────────
    private final By jobsMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/jobs']"
    );
    private final By jobsMenuLinkHref = By.xpath(
        "//a[@href='/jobs']"
    );

    // ── Step 2 – Applied Job card ─────────────────────────────────────────────
    // HTML: <div class="textData">Applied Job</div>
    private final By appliedJobCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Applied Job']]"
    );

    // ── Step 3 – Applied Job search box ──────────────────────────────────────
    // HTML: <input placeholder="Search job" class="ant-input ..." type="text">
    // Primary: any wrapper + ant-input with placeholder="Search job"
    private final By appliedJobSearchPrimary = By.xpath(
        "//*[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[contains(@class,'ant-input') and @placeholder='Search job']"
    );
    // Fallback 1: ant-input with placeholder, no wrapper constraint
    private final By appliedJobSearchFallback1 = By.xpath(
        "//input[@placeholder='Search job' and contains(@class,'ant-input')]"
    );
    // Fallback 2: any input with placeholder="Search job"
    private final By appliedJobSearchFallback2 = By.xpath(
        "//input[@placeholder='Search job']"
    );

    // ── Step 5 – Job Post card ────────────────────────────────────────────────
    // HTML: <div class="textData">Job Post</div>
    private final By jobPostCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Job Post']]"
    );

    // ── Step 6 – Job Post search box ─────────────────────────────────────────
    // HTML: <input placeholder="Search job" class="ant-input ..." type="text">
    private final By jobPostSearchInput = By.xpath(
        "//input[@placeholder='Search job' and contains(@class,'ant-input')]"
    );

    // ── Step 7 – Add Job button ───────────────────────────────────────────────
    // HTML: <button class="ant-btn ant-btn-primary ..."><span>Add Job</span></button>
    private final By addJobButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Job']]"
    );

    // ── Step 7 – Form: Company/Hospital plain text input ─────────────────────
    // HTML: <input placeholder="Enter company / hospital name" id="company_or_hospital_name" ...>
    private final By companyHospitalInput = By.xpath(
        "//input[@id='company_or_hospital_name']"
    );

    // ── Step 7 – Form: Job Title plain text input ─────────────────────────────
    // HTML: <input placeholder="Enter job title" id="job_title" ...>
    private final By jobTitleInput = By.xpath(
        "//input[@id='job_title']"
    );

    // ── Step 7 – Form: Job Location plain text input ──────────────────────────
    // HTML: <input placeholder="Enter location" id="job_location" ...>
    private final By jobLocationInput = By.xpath(
        "//input[@id='job_location']"
    );

    // ── Step 7 – Form: Job Type Ant Select dropdown ───────────────────────────
    // HTML: <div class="ant-select-selector">...<input id="job_type" ...>...</div>
    private final By jobTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='job_type']]"
    );

    // ── Step 7 – Form: Job Experience plain text input ────────────────────────
    // HTML: <input placeholder="e.g. 1-3 years" id="job_experience" ...>
    private final By jobExperienceInput = By.xpath(
        "//input[@id='job_experience']"
    );

    // ── Step 7 – Form: Salary Type Ant Select dropdown ───────────────────────
    // HTML: <div class="ant-select-selector">...<input id="salary_type" ...>...</div>
    private final By salaryTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='salary_type']]"
    );

    // ── Step 7 – Form: Salary Range plain text input ──────────────────────────
    // HTML: <input placeholder="e.g. 60000 - 80000" id="salary_range" ...>
    private final By salaryRangeInput = By.xpath(
        "//input[@id='salary_range']"
    );

    // ── Step 7 – Form: Show Salary Ant Select dropdown ───────────────────────
    // HTML: <div class="ant-select-selector">...<input id="show_salary" ...>...</div>
    private final By showSalaryDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='show_salary']]"
    );

    // ── Step 7 – Form: Job Highlights textarea ───────────────────────────────
    // HTML: <textarea placeholder="Enter job highlights (one per line)" ...>
    private final By jobHighlightsTextarea = By.xpath(
        "//textarea[@placeholder='Enter job highlights (one per line)']"
    );

    // ── Step 7 – Form: Key Accountabilities textarea ─────────────────────────
    // HTML: <textarea placeholder="Enter key accountabilities (one per line)" ...>
    private final By keyAccountabilitiesTextarea = By.xpath(
        "//textarea[@placeholder='Enter key accountabilities (one per line)']"
    );

    // ── Step 9 – Post Job submit button ──────────────────────────────────────
    // HTML: <button type="submit" class="ant-btn ant-btn-primary ..."><span>Post Job</span></button>
    private final By postJobButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Post Job']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public JobsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Jobs menu link
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage clickJobsMenu() {
        System.out.println("[JobsPage] Step 1 → Clicking 'Jobs' menu...");
        WebElement element = findJobsMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[JobsPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findJobsMenuElement() {
        // Pass 1 — primary
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLink));
        } catch (Exception ignored) {
            System.out.println("[JobsPage] Primary menu locator failed — trying href-only...");
        }
        // Pass 2 — href-only (collapsed sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[JobsPage] href-only locator failed — trying sidebar expand...");
        }
        // Pass 3 — expand sidebar then retry
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLinkHref));
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
                System.out.println("[JobsPage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[JobsPage] No sidebar toggle — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[JobsPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Applied Job card
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage clickAppliedJobCard() {
        System.out.println("[JobsPage] Step 2 → Clicking 'Applied Job' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(appliedJobCard));
        scrollAndClick(element);
        // Extra wait: give the Applied Job page time to fully render including the search box
        sleep(4000);
        System.out.println("[JobsPage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Search "Test" in Applied Job search box
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Finds the search input (placeholder="Search job") using three
     * progressively broader strategies, clears it, then types "Test".
     */
    public JobsPage searchTestInAppliedJob() {
        System.out.println("[JobsPage] Step 3 → Typing 'Test' in Applied Job search box...");
        WebElement input = findAppliedJobSearchInput();
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys("Test");
        sleep(2000);
        System.out.println("[JobsPage] Step 3 → 'Test' entered ✔");
        return this;
    }

    private WebElement findAppliedJobSearchInput() {
        // Strategy 1 – //* wrapper (span or div), ant-input, placeholder=Search job
        try {
            WebElement el = wait.until(
                ExpectedConditions.elementToBeClickable(appliedJobSearchPrimary));
            System.out.println("[JobsPage] Search input found via primary locator ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[JobsPage] Primary search locator timed out — trying fallback 1...");
        }
        // Strategy 2 – ant-input class + placeholder, no wrapper
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(appliedJobSearchFallback1));
            System.out.println("[JobsPage] Search input found via fallback-1 locator ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[JobsPage] Fallback-1 search locator timed out — trying fallback 2...");
        }
        // Strategy 3 – broadest: any input[placeholder='Search job']
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.elementToBeClickable(appliedJobSearchFallback2));
        System.out.println("[JobsPage] Search input found via fallback-2 locator ✔");
        return el;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Navigate back from Applied Job
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage navigateBackFromAppliedJob() {
        System.out.println("[JobsPage] Step 4 → Navigating back from Applied Job...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[JobsPage] Step 4 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Click Job Post card
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage clickJobPostCard() {
        System.out.println("[JobsPage] Step 5 → Clicking 'Job Post' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostCard));
        scrollAndClick(element);
        sleep(3000);
        System.out.println("[JobsPage] Step 5 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 – Search "Fortis Hospital" in Job Post search box
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage searchFortisInJobPost() {
        System.out.println("[JobsPage] Step 6 → Typing 'Fortis Hospital' in Job Post search box...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostSearchInput));
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys("Fortis Hospital");
        sleep(2000);
        System.out.println("[JobsPage] Step 6 → 'Fortis Hospital' entered ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 – Click Add Job button + fill the entire form
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Add Job' button then fills every field:
     *   • Company/Hospital     → "Fortis"     (plain text input)
     *   • Job Title            → "Devops"     (plain text input)
     *   • Job Location         → "Noida"      (plain text input)
     *   • Job Type             → "Full Time"  (Ant Select dropdown)
     *   • Job Experience       → "2"          (plain text input)
     *   • Salary Type          → "Monthly"    (Ant Select dropdown)
     *   • Salary Range         → "500-1000"   (plain text input)
     *   • Show Salary          → "Yes"        (Ant Select dropdown)
     *   • Job Highlights       → "selenium"   (textarea)
     *   • Key Accountabilities → "selenium"   (textarea)
     */
    public JobsPage clickAddJobAndFillForm() {
        System.out.println("[JobsPage] Step 7 → Clicking 'Add Job' button...");
        WebElement addBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addJobButton));
        scrollAndJsClick(addBtn);
        sleep(2000);

        // Company / Hospital — plain text input
        fillInput(companyHospitalInput, "Fortis");

        // Job Title — plain text input
        fillInput(jobTitleInput, "Devops");

        // Job Location — plain text input
        fillInput(jobLocationInput, "Noida");

        // Job Type — Ant Select dropdown
        selectAntDropdown(jobTypeDropdown, "Full Time");

        // Job Experience — plain text input
        fillInput(jobExperienceInput, "2");

        // Salary Type — Ant Select dropdown
        selectAntDropdown(salaryTypeDropdown, "Monthly");

        // Salary Range — plain text input
        fillInput(salaryRangeInput, "500-1000");

        // Show Salary — Ant Select dropdown
        selectAntDropdown(showSalaryDropdown, "Yes");

        // Job Highlights — textarea
        fillTextarea(jobHighlightsTextarea, "selenium");

        // Key Accountabilities — textarea
        fillTextarea(keyAccountabilitiesTextarea, "selenium");

        System.out.println("[JobsPage] Step 7 → Form filled ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 – Click Post Job (submit) button
    // ══════════════════════════════════════════════════════════════════════════

    public JobsPage clickPostJobButton() {
        System.out.println("[JobsPage] Step 9 → Clicking 'Post Job' button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(postJobButton));
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(postJobButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[JobsPage] Step 9 → PASSED ✔");
        return this;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Opens an Ant Design Select dropdown and clicks the matching option.
     * Strategy: click the visible div.ant-select-selector → wait for dropdown
     * to appear → click the matching div.ant-select-item-option.
     */
    private void selectAntDropdown(By selectorLocator, String optionText) {
        System.out.println("[JobsPage] → Opening dropdown for option: " + optionText);
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
        System.out.println("[JobsPage] → Selected: " + optionText + " ✔");
    }

    /**
     * Clears a plain <input> and types the given value.
     * Uses JS to reset value first so React/Ant state is properly cleared.
     */
    private void fillInput(By inputLocator, String value) {
        System.out.println("[JobsPage] → Filling input with: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputLocator));
        scrollAndClick(input);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(value);
        sleep(400);
        System.out.println("[JobsPage] → Entered: " + value + " ✔");
    }

    /**
     * Clears a <textarea> and types the given value.
     */
    private void fillTextarea(By textareaLocator, String value) {
        System.out.println("[JobsPage] → Filling textarea with: " + value);
        WebElement textarea = wait.until(
            ExpectedConditions.elementToBeClickable(textareaLocator));
        scrollAndClick(textarea);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", textarea);
        textarea.sendKeys(Keys.CONTROL + "a");
        textarea.sendKeys(Keys.DELETE);
        textarea.clear();
        textarea.sendKeys(value);
        sleep(400);
        System.out.println("[JobsPage] → Textarea filled: " + value + " ✔");
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    /** JavaScript click — bypasses any overlay / intercept. */
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
