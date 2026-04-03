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
 * Page Object Model for the Print Qbank module.
 *
 * Covers:
 *  Step 1 – Print Qbank sidebar menu item  (/print-qbank)
 *           HTML: <span class="ant-menu-title-content">
 *                   <a href="/print-qbank">Print Qbank</a>
 *                 </span>
 *
 *  Step 2 – Select Subject dropdown
 *           HTML: <div class="ant-select-selector">
 *                   <span class="ant-select-selection-wrap">
 *                     <input id="topic_name" role="combobox" ...>
 *                     <span class="ant-select-selection-item"
 *                           title="Test Topic 1">Test Topic 1</span>
 *                   </span>
 *                 </div>
 *
 *  Step 3 – Submit button
 *           HTML: <button type="submit"
 *                         class="ant-btn ... ant-btn-primary
 *                                ant-btn-color-primary ant-btn-variant-solid">
 *                   <span>Submit</span>
 *                 </button>
 */
public class PrintQbank {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────────────────────

    // Step 1 – Print Qbank sidebar menu link (primary)
    // Matches: <span class="ant-menu-title-content">
    //            <a href="/print-qbank">Print Qbank</a>
    //          </span>
    private final By printQbankMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/print-qbank']"
    );

    // Step 1 – Fallback: href only (collapsed / icon-only sidebar)
    private final By printQbankMenuLinkHref = By.xpath(
        "//a[@href='/print-qbank']"
    );

    // Step 2 – Select Subject dropdown wrapper
    // Matches the ant-select-selector div that wraps the combobox input
    // with id="topic_name"
    private final By selectSubjectDropdown = By.xpath(
        "//div[contains(@class,'ant-select-selector')]" +
        "[.//input[@id='topic_name' or @role='combobox']]"
    );

    // Step 2 – Ant Select open trigger (the outer ant-select container)
    // Clicking this opens the dropdown list
    private final By selectSubjectContainer = By.xpath(
        "//div[contains(@class,'ant-select')]" +
        "[.//input[@id='topic_name' or @role='combobox']]"
    );

    // Step 2 – First option in the Ant Design dropdown list
    // After the dropdown opens, pick the first visible option
    private final By firstDropdownOption = By.xpath(
        "(//div[contains(@class,'ant-select-dropdown')]" +
        "//div[contains(@class,'ant-select-item-option')]" +
        "[not(contains(@class,'ant-select-item-option-disabled'))])[1]"
    );

    // Step 3 – Submit button
    // HTML: <button type="submit" class="ant-btn ... ant-btn-primary ...">
    //         <span>Submit</span>
    //       </button>
    private final By submitButton = By.xpath(
        "//button[@type='submit']" +
        "[contains(@class,'ant-btn-primary')]" +
        "[.//span[normalize-space(.)='Submit']]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public PrintQbank(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Print Qbank menu link
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Print Qbank' sidebar menu entry and waits for the page
     * to load.
     *
     * Three-pass strategy:
     *   Pass 1 – primary locator (span.ant-menu-title-content > a[href])
     *   Pass 2 – href-only (handles collapsed / icon-only sidebar)
     *   Pass 3 – expand sidebar trigger, then retry both locators
     */
    public PrintQbank clickPrintQbankMenu() {
        System.out.println("[PrintQbank] Step 1 → Clicking 'Print Qbank' menu...");
        WebElement element = findPrintQbankMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[PrintQbank] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findPrintQbankMenuElement() {
        // Pass 1 — primary
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(printQbankMenuLink));
        } catch (Exception ignored) {
            System.out.println("[PrintQbank] Primary menu locator failed — trying href-only...");
        }
        // Pass 2 — href-only (collapsed sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(printQbankMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[PrintQbank] href-only locator failed — trying sidebar expand...");
        }
        // Pass 3 — expand collapsed sidebar, then retry
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(printQbankMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(printQbankMenuLinkHref));
    }

    /**
     * Clicks the Ant Design sider collapse/expand trigger if it exists.
     * Safe to call when sidebar is already expanded — no-op in that case.
     */
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
                System.out.println("[PrintQbank] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[PrintQbank] No sidebar toggle found — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[PrintQbank] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click Select Subject dropdown and choose first option
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the 'Select Subject' Ant Design Select dropdown and picks the
     * first available (non-disabled) option from the list.
     *
     * Ant Design Select uses a hidden combobox <input readonly> with
     * style="opacity:0" — direct sendKeys does not work. The correct approach
     * is to click the visible selector div which opens the dropdown, then
     * click the desired list item.
     *
     * HTML structure:
     *   <div class="ant-select">              ← outer container (clickable)
     *     <div class="ant-select-selector">   ← visible selected-value area
     *       <span class="ant-select-selection-wrap">
     *         <input id="topic_name" readonly style="opacity:0">
     *         <span class="ant-select-selection-item" title="Test Topic 1">
     *           Test Topic 1
     *         </span>
     *       </span>
     *     </div>
     *   </div>
     *   <div class="ant-select-dropdown">     ← opens after click
     *     <div class="ant-select-item-option">...</div>
     *   </div>
     */
    public PrintQbank selectSubject() {
        System.out.println("[PrintQbank] Step 2 → Opening 'Select Subject' dropdown...");

        // Click the selector div to open the dropdown
        WebElement selector = wait.until(
            ExpectedConditions.elementToBeClickable(selectSubjectDropdown)
        );
        scrollAndClick(selector);
        sleep(1000);

        // Wait for dropdown list to appear
        System.out.println("[PrintQbank] Step 2 → Waiting for dropdown options...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstDropdownOption));

        // Click the first non-disabled option
        WebElement firstOption = wait.until(
            ExpectedConditions.elementToBeClickable(firstDropdownOption)
        );
        System.out.println("[PrintQbank] Step 2 → Selecting first option: "
            + firstOption.getText().trim());
        scrollAndClick(firstOption);
        sleep(1000);

        System.out.println("[PrintQbank] Step 2 → PASSED ✔");
        return this;
    }

    /**
     * Overloaded version — selects a specific subject by its visible text.
     *
     * Usage: page.selectSubject("Test Topic 1");
     *
     * @param subjectText exact visible text of the option to select
     */
    public PrintQbank selectSubject(String subjectText) {
        System.out.println("[PrintQbank] Step 2 → Opening 'Select Subject' dropdown...");

        // Click the selector div to open the dropdown
        WebElement selector = wait.until(
            ExpectedConditions.elementToBeClickable(selectSubjectDropdown)
        );
        scrollAndClick(selector);
        sleep(1000);

        // Build a locator for the specific option text
        By specificOption = By.xpath(
            "//div[contains(@class,'ant-select-dropdown')]" +
            "//div[contains(@class,'ant-select-item-option')]" +
            "[not(contains(@class,'ant-select-item-option-disabled'))]" +
            "[normalize-space(.)='" + subjectText + "']"
        );

        System.out.println("[PrintQbank] Step 2 → Looking for option: " + subjectText);
        wait.until(ExpectedConditions.visibilityOfElementLocated(specificOption));
        WebElement option = wait.until(
            ExpectedConditions.elementToBeClickable(specificOption)
        );
        scrollAndClick(option);
        sleep(1000);

        System.out.println("[PrintQbank] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Click Submit button
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the Submit button to submit the Print Qbank form.
     *
     * HTML: <button type="submit"
     *               class="ant-btn ... ant-btn-primary
     *                      ant-btn-color-primary ant-btn-variant-solid">
     *         <span>Submit</span>
     *       </button>
     *
     * Uses JS click to bypass any overlay that may intercept the submit button
     * after the dropdown selection settles.
     */
    public PrintQbank clickSubmitButton() {
        System.out.println("[PrintQbank] Step 3 → Clicking 'Submit' button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(submitButton)
        );
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[PrintQbank] Step 3 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Scrolls element to centre of viewport, then sends a native Selenium click. */
    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
        element.click();
    }

    /**
     * Scrolls element to centre of viewport, then fires a JavaScript click.
     * Use for buttons where native click may be intercepted by overlays.
     */
    private void scrollAndJsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
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
