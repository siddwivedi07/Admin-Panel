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
 * Page Object for the Applied Job sub-section of the Jobs module.
 *
 * Flow:
 *  Step 1 – Click Jobs sidebar menu  (/jobs)
 *  Step 2 – Click Applied Job card
 *  Step 3 – Search in Applied Job search box
 *  Step 4 – Navigate back from Applied Job
 */
public class AppliedJobPage {

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
    private final By appliedJobCard = By.xpath(
        "//div[contains(@class,'ant-card-body')]" +
        "[.//div[contains(@class,'textData') and " +
        "normalize-space(.)='Applied Job']]"
    );

    // ── Step 3 – Applied Job search box ──────────────────────────────────────
    private final By appliedJobSearchPrimary = By.xpath(
        "//*[contains(@class,'ant-input-affix-wrapper')]" +
        "//input[contains(@class,'ant-input') and @placeholder='Search job']"
    );
    private final By appliedJobSearchFallback1 = By.xpath(
        "//input[@placeholder='Search job' and contains(@class,'ant-input')]"
    );
    private final By appliedJobSearchFallback2 = By.xpath(
        "//input[@placeholder='Search job']"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public AppliedJobPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  navigateToAppliedJobs — full entry flow (menu → card)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the Jobs sidebar menu link, then clicks the Applied Job card.
     * Called as the single entry point from JobTest.
     */
    public AppliedJobPage navigateToAppliedJobs() {
        clickJobsMenu();
        sleep(2000);
        clickAppliedJobCard();
        sleep(4000);
        return this;
    }

    // ── Step 1 ────────────────────────────────────────────────────────────────

    public AppliedJobPage clickJobsMenu() {
        System.out.println("[AppliedJobPage] Step 1 → Clicking 'Jobs' menu...");
        WebElement element = findJobsMenuElement();
        scrollAndClick(element);
        sleep(2000);
        System.out.println("[AppliedJobPage] Step 1 → PASSED ✔");
        return this;
    }

    private WebElement findJobsMenuElement() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLink));
        } catch (Exception ignored) {
            System.out.println("[AppliedJobPage] Primary menu locator failed — trying href-only...");
        }
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLinkHref));
        } catch (Exception ignored) {
            System.out.println("[AppliedJobPage] href-only locator failed — expanding sidebar...");
        }
        tryExpandSidebar();
        sleep(1500);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLink));
        } catch (Exception ignored) { }
        return wait.until(ExpectedConditions.elementToBeClickable(jobsMenuLinkHref));
    }

    // ── Step 2 ────────────────────────────────────────────────────────────────

    public AppliedJobPage clickAppliedJobCard() {
        System.out.println("[AppliedJobPage] Step 2 → Clicking 'Applied Job' card...");
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(appliedJobCard));
        scrollAndClick(element);
        sleep(4000);
        System.out.println("[AppliedJobPage] Step 2 → PASSED ✔");
        return this;
    }

    // ── Step 3 ────────────────────────────────────────────────────────────────

    public AppliedJobPage searchInAppliedJob(String searchText) {
        System.out.println("[AppliedJobPage] Step 3 → Searching: " + searchText);
        WebElement input = findAppliedJobSearchInput();
        scrollAndClick(input);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.clear();
        input.sendKeys(searchText);
        sleep(2000);
        System.out.println("[AppliedJobPage] Step 3 → '" + searchText + "' entered ✔");
        return this;
    }

    private WebElement findAppliedJobSearchInput() {
        try {
            WebElement el = wait.until(
                ExpectedConditions.elementToBeClickable(appliedJobSearchPrimary));
            System.out.println("[AppliedJobPage] Search input found via primary locator ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[AppliedJobPage] Primary search locator timed out — trying fallback 1...");
        }
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(appliedJobSearchFallback1));
            System.out.println("[AppliedJobPage] Search input found via fallback-1 ✔");
            return el;
        } catch (Exception ignored) {
            System.out.println("[AppliedJobPage] Fallback-1 timed out — trying fallback 2...");
        }
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.elementToBeClickable(appliedJobSearchFallback2));
        System.out.println("[AppliedJobPage] Search input found via fallback-2 ✔");
        return el;
    }

    // ── Step 4 ────────────────────────────────────────────────────────────────

    public AppliedJobPage navigateBack() {
        System.out.println("[AppliedJobPage] Step 4 → Navigating back from Applied Job...");
        driver.navigate().back();
        sleep(3000);
        System.out.println("[AppliedJobPage] Step 4 → PASSED ✔");
        return this;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void tryExpandSidebar() {
        try {
            By trigger = By.cssSelector(
                ".ant-layout-sider-trigger," +
                ".ant-layout-sider .anticon-menu-fold," +
                ".ant-layout-sider .anticon-menu-unfold");
            List<WebElement> triggers = driver.findElements(trigger);
            if (!triggers.isEmpty()) triggers.get(0).click();
        } catch (Exception e) {
            System.out.println("[AppliedJobPage] Could not expand sidebar: " + e.getMessage());
        }
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
