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
 * Covers:
 *  Step 1 – Jobs sidebar menu item  (/jobs)
 *  Step 2 – Applied Job card → enter page
 *  Step 3 – Search box → type "Ravi" → screenshot
 *  Step 4 – Navigate back from Applied Job
 *  Step 5 – Job Post card → enter page
 *  Step 6 – Search job box → type "Dentist"
 *  Step 7 – Add Job button → fill form:
 *             • Company/Hospital  → "Max Hospital"
 *             • Job Title         → "Dentist"
 *             • Job Location      → "Delhi"
 *             • Job Type          → "Full Time"
 *             • Job Experience    → "1 - 3 Yr"
 *             • Salary Type       → "Monthly"
 *             • Salary Range      → "30000"
 *  Step 8 – Post Job (submit) button
 */
public class JobPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Step 1 – Jobs sidebar menu ────────────────────────────────────────────
    // HTML: <span class="ant-menu-title-content"><a href="/jobs">Jobs</a></span>
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
    // HTML: <input placeholder="Search..." class="ant-input ..." type="text">
    //       wrapped in ant-input-affix-wrapper with a clear (X) button
    private final By appliedJobSearchInput = By.xpath(
        "//span[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[contains(@class,'ant-input') and @placeholder='Search...']"
    );

    // ── Step 5 – Job Post card ────────────────────────────────────────────────
    // HTML: <div class="textData">Job Post</div>
    private final By jobPostCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Job Post']]"
    );

    // ── Step 6 – Job Post search box ──────────────────────────────────────────
    // HTML: <input placeholder="Search job" class="ant-input ..." type="text">
    //       wrapped in ant-input-affix-wrapper
    private final By jobPostSearchInput = By.xpath(
        "//span[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[contains(@class,'ant-input') and @placeholder='Search job']"
    );

    // ── Step 7 – Add Job button ───────────────────────────────────────────────
    // HTML: <button class="ant-btn ant-btn-primary ..."><span>Add Job</span></button>
    private final By addJobButton = By.xpath(
        "//button[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Add Job']]"
    );

    // ── Step 7 – Company/Hospital dropdown ───────────────────────────────────
    // HTML: <input id="company_or_hospital_name" role="combobox" ...>
    private final By companyHospitalDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='company_or_hospital_name']]"
    );

    // ── Step 7 – Job Title dropdown ───────────────────────────────────────────
    // HTML: <input id="job_title" role="combobox" ...>
    private final By jobTitleDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='job_title']]"
    );

    // ── Step 7 – Job Location input ───────────────────────────────────────────
    // HTML: <input id="job_location" placeholder="Enter location (Delhi, Noida)">
    private final By jobLocationInput = By.xpath(
        "//input[@id='job_location']"
    );

    // ── Step 7 – Job Type dropdown ────────────────────────────────────────────
    // HTML: <input id="job_type" role="combobox" ...>
    private final By jobTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='job_type']]"
    );

    // ── Step 7 – Job Experience dropdown ─────────────────────────────────────
    // HTML: <input id="job_experience" role="combobox" ...>
    private final By jobExperienceDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='job_experience']]"
    );

    // ── Step 7 – Salary Type dropdown ────────────────────────────────────────
    // HTML: <input id="salary_type" role="combobox" ...>
    private final By salaryTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='salary_type']]"
    );

    // ── Step 7 – Salary Range input ───────────────────────────────────────────
    // HTML: <input id="salary_range" placeholder="e.g. 60000 - 80000">
    private final By salaryRangeInput = By.xpath(
        "//input[@id='salary_range']"
    );

    // ── Step 8 – Post Job submit button ──────────────────────────────────────
    // HTML: <button type="submit" class="ant-btn ant-btn-primary ...">
    //         <span>Post Job</span>
    //       </button>
    private final By postJobButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Post Job']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public JobPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Jobs menu link
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Jobs' sidebar menu entry.
     * Three-pass strategy: primary → href-only → expand sidebar + retry.
     */
    public JobPage clickJobsMenu() {
        System.out.println("[JobPage] Step 1 → Clicking 'Jobs' menu...");
        WebElement element = findJobsMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[JobPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findJobsMenuElement() {
        // Pass 1 — primary
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLink));
        } catch (Exception ignored) {
            System.out.println("[JobPage] Primary menu locator failed — trying href-only...");
        }
        // Pass 2 — href-only (collapsed sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[JobPage] href-only locator failed — trying sidebar expand...");
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
                System.out.println("[JobPage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[JobPage] No sidebar toggle — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[JobPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Applied Job card
    // ══════════════════════════════════════════════════════════════════════════

    public JobPage clickAppliedJobCard() {
        System.out.println("[JobPage] Step 2 → Clicking 'Applied Job' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(appliedJobCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[JobPage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Search "Ravi" in Applied Job search box
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Locates the Search input (placeholder="Search...") inside the
     * ant-input-affix-wrapper, clears any existing value, types "Ravi"
     * and waits for the table to reload.
     *
     * Screenshot is taken in JobTest after this returns so it captures
     * the fully loaded results.
     */
    public JobPage searchRaviInAppliedJob() {
        System.out.println("[JobPage] Step 3 → Typing 'Ravi' in Applied Job search box...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(appliedJobSearchInput));
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys("Ravi");
        sleep(2000); // wait for debounced search / table reload
        System.out.println("[JobPage] Step 3 → 'Ravi' entered ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Navigate back from Applied Job
    // ══════════════════════════════════════════════════════════════════════════

    public JobPage navigateBackFromAppliedJob() {
        System.out.println("[JobPage] Step 4 → Navigating back from Applied Job...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[JobPage] Step 4 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 – Click Job Post card
    // ══════════════════════════════════════════════════════════════════════════

    public JobPage clickJobPostCard() {
        System.out.println("[JobPage] Step 5 → Clicking 'Job Post' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[JobPage] Step 5 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 – Search "Dentist" in Job Post search box
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Locates the job search input (placeholder="Search job"), clears it,
     * types "Dentist" and waits for results.
     */
    public JobPage searchDentistInJobPost() {
        System.out.println("[JobPage] Step 6 → Typing 'Dentist' in Job Post search box...");
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostSearchInput));
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys("Dentist");
        sleep(2000); // wait for debounced search / table reload
        System.out.println("[JobPage] Step 6 → 'Dentist' entered ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 – Click Add Job, fill the form
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Add Job' button, then fills every field in the job form:
     *   • Company/Hospital  → "Max Hospital"   (Ant Select, id=company_or_hospital_name)
     *   • Job Title         → "Dentist"         (Ant Select, id=job_title)
     *   • Job Location      → "Delhi"           (plain input, id=job_location)
     *   • Job Type          → "Full Time"       (Ant Select, id=job_type)
     *   • Job Experience    → "1 - 3 Yr"        (Ant Select, id=job_experience)
     *   • Salary Type       → "Monthly"         (Ant Select, id=salary_type)
     *   • Salary Range      → "30000"           (plain input, id=salary_range)
     */
    public JobPage clickAddJobAndFillForm() {
        System.out.println("[JobPage] Step 7 → Clicking 'Add Job' button...");
        WebElement addBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addJobButton));
        scrollAndJsClick(addBtn);
        sleep(2000);

        // Company / Hospital
        selectAntDropdown(companyHospitalDropdown, "Max Hospital");

        // Job Title
        selectAntDropdown(jobTitleDropdown, "Dentist");

        // Job Location
        fillInput(jobLocationInput, "Delhi");

        // Job Type
        selectAntDropdown(jobTypeDropdown, "Full Time");

        // Job Experience
        selectAntDropdown(jobExperienceDropdown, "1 - 3 Yr");

        // Salary Type
        selectAntDropdown(salaryTypeDropdown, "Monthly");

        // Salary Range
        fillInput(salaryRangeInput, "30000");

        System.out.println("[JobPage] Step 7 → Form filled ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 – Click Post Job (submit) button
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Post Job' submit button.
     * Uses JS click to bypass any overlay after the form is filled.
     */
    public JobPage clickPostJobButton() {
        System.out.println("[JobPage] Step 8 → Clicking 'Post Job' button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(postJobButton));
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(postJobButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[JobPage] Step 8 → PASSED ✔");
        return this;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Opens an Ant Design Select dropdown identified by its selector div,
     * waits for the option list, then clicks the option matching optionText.
     *
     * Ant Design Select uses a hidden readonly <input style="opacity:0">.
     * The correct approach is:
     *   1. Click the visible div.ant-select-selector  → opens dropdown
     *   2. Wait for div.ant-select-dropdown to appear
     *   3. Click the matching div.ant-select-item-option
     *
     * @param selectorLocator  By locator that targets the ant-select-selector div
     * @param optionText       exact visible text of the option to select
     */
    private void selectAntDropdown(By selectorLocator, String optionText) {
        System.out.println("[JobPage] → Opening dropdown for option: " + optionText);

        // Click the visible selector area to open the list
        WebElement selector = wait.until(
            ExpectedConditions.elementToBeClickable(selectorLocator));
        scrollAndClick(selector);
        sleep(800);

        // Build locator for the specific option
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
        System.out.println("[JobPage] → Selected: " + optionText + " ✔");
    }

    /**
     * Clears a plain text input field and types the given value.
     * Uses JS to reset value first, then sends keys for proper React/Ant state.
     */
    private void fillInput(By inputLocator, String value) {
        System.out.println("[JobPage] → Filling input with: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputLocator));
        scrollAndClick(input);
        // JS clear + sendKeys ensures React controlled-input state is updated
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(value);
        sleep(400);
        System.out.println("[JobPage] → Entered: " + value + " ✔");
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    /**
     * JavaScript click — bypasses any overlay / intercept.
     * Used for Add Job and Post Job buttons.
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
