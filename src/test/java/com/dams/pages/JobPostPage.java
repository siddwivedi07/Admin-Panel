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
 * Page Object for the Job Post sub-section of the Jobs module.
 *
 * Flow:
 *  Step 5 – Click Job Post card
 *  Step 6 – Search "Fortis Hospital" in Job Post search box
 *  Step 7 – Click Add Job button + fill entire form:
 *             • Company/Hospital     → "Fortis"     (plain text input)
 *             • Job Title            → "Devops"     (plain text input)
 *             • Job Location         → "Noida"      (plain text input)
 *             • Job Type             → "Full Time"  (Ant Select dropdown)
 *             • Job Experience       → "2"          (plain text input)
 *             • Salary Type          → "Monthly"    (Ant Select dropdown)
 *             • Salary Range         → "500-1000"   (plain text input)
 *             • Show Salary          → "Yes"        (Ant Select dropdown)
 *             • Job Highlights       → "selenium"   (textarea)
 *             • Key Accountabilities → "selenium"   (textarea)
 *  Step 9 – Click Post Job (submit) button
 */
public class JobPostPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Step 5 – Job Post card ────────────────────────────────────────────────
    private final By jobPostCard = By.xpath("/html/body/div/div[2]/div/main/div[2]/div[2]");

    // ── Step 6 – Job Post search box ─────────────────────────────────────────
    private final By jobPostSearchInput = By.xpath(
        "//input[@placeholder='Search job' and contains(@class,'ant-input')]"
    );

    // ── Step 7 – Add Job button ───────────────────────────────────────────────
    private final By addJobButton = By.xpath("/html/body/div/div[2]/div/main/div[2]/div[1]/button");

    // ── Step 7 – Form fields ──────────────────────────────────────────────────
    private final By companyHospitalInput = By.xpath(
        "//input[@id='company_or_hospital_name']"
    );
    private final By jobTitleInput = By.xpath(
        "//input[@id='job_title']"
    );
    private final By jobLocationInput = By.xpath(
        "//input[@id='job_location']"
    );
    private final By jobTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='job_type']]"
    );
    private final By jobExperienceInput = By.xpath(
        "//input[@id='job_experience']"
    );
    private final By salaryTypeDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='salary_type']]"
    );
    private final By salaryRangeInput = By.xpath(
        "//input[@id='salary_range']"
    );
    private final By showSalaryDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='show_salary']]"
    );
    private final By jobHighlightsTextarea = By.xpath(
        "//textarea[@placeholder='Enter job highlights (one per line)']"
    );
    private final By keyAccountabilitiesTextarea = By.xpath(
        "//textarea[@placeholder='Enter key accountabilities (one per line)']"
    );

    // ── Step 9 – Post Job submit button ──────────────────────────────────────
    private final By postJobButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Post Job']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public JobPostPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  postJob — full Job Post flow (card → search → add form → submit)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Orchestrates the complete Job Post flow as a single callable method,
     * matching the pattern used in JobTest.postJob().
     */
    public JobPostPage postJob() {
        clickJobPostCard();
        sleep(3000);
        searchInJobPost("Fortis Hospital");
        sleep(2000);
        clickAddJobAndFillForm();
        sleep(2000);
        clickPostJobButton();
        sleep(3000);
        return this;
    }

    // ── Step 5 ────────────────────────────────────────────────────────────────

    public JobPostPage clickJobPostCard() {
        System.out.println("[JobPostPage] Step 5 → Clicking 'Job Post' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostCard));
        scrollAndClick(element);
        sleep(3000);
        System.out.println("[JobPostPage] Step 5 → PASSED ✔");
        return this;
    }

    // ── Step 6 ────────────────────────────────────────────────────────────────

    public JobPostPage searchInJobPost(String searchText) {
        System.out.println("[JobPostPage] Step 6 → Searching: " + searchText);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(jobPostSearchInput));
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(searchText);
        sleep(2000);
        System.out.println("[JobPostPage] Step 6 → '" + searchText + "' entered ✔");
        return this;
    }

    // ── Step 7 ────────────────────────────────────────────────────────────────

    public JobPostPage clickAddJobAndFillForm() {
        System.out.println("[JobPostPage] Step 7 → Clicking 'Add Job' button...");
        WebElement addBtn = wait.until(
            ExpectedConditions.elementToBeClickable(addJobButton));
        scrollAndJsClick(addBtn);
        sleep(2000);

        fillInput(companyHospitalInput,  "Fortis");
        fillInput(jobTitleInput,          "Devops");
        fillInput(jobLocationInput,       "Noida");
        selectAntDropdown(jobTypeDropdown, "Full Time");
        fillInput(jobExperienceInput,     "2");
        selectAntDropdown(salaryTypeDropdown, "Monthly");
        fillInput(salaryRangeInput,       "500-1000");
        selectAntDropdown(showSalaryDropdown, "Yes");
        fillTextarea(jobHighlightsTextarea,       "selenium");
        fillTextarea(keyAccountabilitiesTextarea, "selenium");

        System.out.println("[JobPostPage] Step 7 → Form filled ✔");
        return this;
    }

    // ── Step 9 ────────────────────────────────────────────────────────────────

    public JobPostPage clickPostJobButton() {
        System.out.println("[JobPostPage] Step 9 → Clicking 'Post Job' button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(postJobButton));
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(postJobButton));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[JobPostPage] Step 9 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void selectAntDropdown(By selectorLocator, String optionText) {
        System.out.println("[JobPostPage] → Opening dropdown for: " + optionText);
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
        scrollAndClick(wait.until(ExpectedConditions.elementToBeClickable(optionLocator)));
        sleep(600);
        System.out.println("[JobPostPage] → Selected: " + optionText + " ✔");
    }

    private void fillInput(By inputLocator, String value) {
        System.out.println("[JobPostPage] → Filling input: " + value);
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputLocator));
        scrollAndClick(input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(value);
        sleep(400);
        System.out.println("[JobPostPage] → Entered: " + value + " ✔");
    }

    private void fillTextarea(By textareaLocator, String value) {
        System.out.println("[JobPostPage] → Filling textarea: " + value);
        WebElement textarea = wait.until(
            ExpectedConditions.elementToBeClickable(textareaLocator));
        scrollAndClick(textarea);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", textarea);
        textarea.sendKeys(Keys.CONTROL + "a");
        textarea.sendKeys(Keys.DELETE);
        textarea.clear();
        textarea.sendKeys(value);
        sleep(400);
        System.out.println("[JobPostPage] → Textarea filled: " + value + " ✔");
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    private void scrollAndJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
