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
 * Page Object Model for the Reward Management module.
 *
 * Covers:
 *  Step 1 – Reward sidebar menu item  (/reward_management)
 *  Step 2 – User Points card
 *  Step 3 – Search box → type "Ravi" → screenshot
 *  Step 4 – View Record button (first matching row)
 */
public class RewardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────────────────────

    // Step 1 – Reward sidebar menu link (primary)
    // HTML: <span class="ant-menu-title-content">
    //         <a href="/reward_management">Reward</a>
    //       </span>
    private final By rewardMenuLink = By.xpath(
        "//span[contains(@class,'ant-menu-title-content')]" +
        "/a[@href='/reward_management']"
    );
    // Fallback 1 – href only (collapsed / icon-only sidebar)
    private final By rewardMenuLinkHref = By.xpath(
        "//a[@href='/reward_management']"
    );

    // Step 2 – User Points card
    // HTML: <div class="textData">User Points</div>
    //       inside <div class="ant-card-body">
    private final By userPointsCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='User Points']]"
    );

    // Step 3 – Search input box
    // HTML: <input placeholder="Search..." class="ant-input ..." type="text">
    private final By searchInput = By.xpath(
        "//input[contains(@class,'ant-input') and @placeholder='Search...']"
    );

    // Step 4 – View Record button
    // HTML: <button class="ant-btn ... ant-btn-sm">
    //         <span class="anticon anticon-edit"/>
    //         <span>View Record</span>
    //       </button>
    // Match first button that contains both the edit icon and "View Record" text
    private final By viewRecordBtn = By.xpath(
        "(//button[contains(@class,'ant-btn')]" +
        "[.//span[normalize-space(.)='View Record']]" +
        "[.//*[contains(@class,'anticon-edit')]])[1]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public RewardPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 – Click Reward menu link
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'Reward' sidebar menu entry.
     * Uses primary locator first, then href-only fallback, then sidebar-expand
     * fallback — consistent with other page objects in this project.
     */
    public RewardPage clickRewardMenu() {
        System.out.println("[RewardPage] Step 1 → Clicking 'Reward' menu...");
        WebElement element = findRewardMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[RewardPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findRewardMenuElement() {
        // Pass 1 — primary: span.ant-menu-title-content > a[href='/reward_management']
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(rewardMenuLink));
        } catch (Exception ignored) {
            System.out.println("[RewardPage] Primary menu locator failed — trying href-only...");
        }
        // Pass 2 — href-only (collapsed/icon sidebar)
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(rewardMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[RewardPage] href-only locator failed — trying sidebar expand...");
        }
        // Pass 3 — expand collapsed sidebar, then retry
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(rewardMenuLink));
        } catch (Exception ignored) {
            // last resort
        }
        return wait.until(ExpectedConditions.elementToBeClickable(rewardMenuLinkHref));
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
                System.out.println("[RewardPage] Sidebar expand toggle clicked.");
            } else {
                System.out.println("[RewardPage] No sidebar toggle found — already expanded.");
            }
        } catch (Exception e) {
            System.out.println("[RewardPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 – Click User Points card
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the 'User Points' card on the Reward Management page.
     * HTML structure:
     *   <div class="ant-card-body">
     *     <div class="Imagedata"><img ...></div>
     *     <div class="textData">User Points</div>
     *   </div>
     */
    public RewardPage clickUserPointsCard() {
        System.out.println("[RewardPage] Step 2 → Clicking 'User Points' card...");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(userPointsCard));
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[RewardPage] Step 2 → PASSED ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 – Click Search box, type "Ravi", wait for results
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Locates the Search input, clears any existing value, types "Ravi",
     * then waits for the table to update.
     *
     * HTML: <input placeholder="Search..." class="ant-input ..." type="text">
     *
     * Screenshot is taken by the test class (RewardTest) after this method
     * returns, so that ReportManager can log it correctly.
     */
    public RewardPage searchRavi() {
        System.out.println("[RewardPage] Step 3 → Clicking search box and typing 'Ravi'...");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        scrollAndClick(input);

        // Clear any pre-existing value before typing
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();

        input.sendKeys("Ravi");
        sleep(2000); // allow debounce / table reload to complete
        System.out.println("[RewardPage] Step 3 → Search 'Ravi' entered ✔");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 – Click first View Record button in the table
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the first 'View Record' button found in the table.
     *
     * HTML:
     *   <button class="ant-btn ... ant-btn-sm">
     *     <span class="anticon anticon-edit"><svg ...></svg></span>
     *     <span>View Record</span>
     *   </button>
     *
     * Uses JS click to avoid any overlay/intercept issue with small ant-btn-sm
     * buttons inside table rows.
     */
    public RewardPage clickViewRecordButton() {
        System.out.println("[RewardPage] Step 4 → Waiting for 'View Record' button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(viewRecordBtn));
        sleep(500);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(viewRecordBtn));
        scrollAndJsClick(element);
        sleep(2000);
        System.out.println("[RewardPage] Step 4 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
        element.click();
    }

    /**
     * JS click — bypasses any intercepting overlay.
     * Used for small ant-btn-sm table action buttons.
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
