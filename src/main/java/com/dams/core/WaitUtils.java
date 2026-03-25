package com.dams.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils
 * ─────────────────────────────────────────────────────────────────────────────
 * Centralised explicit-wait helpers so every page object and test class avoids
 * duplicating WebDriverWait boilerplate.
 */
public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        int timeout = ConfigReader.getInstance().getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    // ── Visibility ───────────────────────────────────────────────────────────

    /** Wait until element is visible and return it. */
    public WebElement waitForVisible(By locator) {
        log.debug("Waiting for visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is visible (already resolved WebElement). */
    public WebElement waitForVisible(WebElement element) {
        log.debug("Waiting for visible: {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ── Clickability ─────────────────────────────────────────────────────────

    /** Wait until element is clickable and return it. */
    public WebElement waitForClickable(By locator) {
        log.debug("Waiting for clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForClickable(WebElement element) {
        log.debug("Waiting for clickable element");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ── Presence ─────────────────────────────────────────────────────────────

    /** Wait until element is present in the DOM (may not be visible). */
    public WebElement waitForPresence(By locator) {
        log.debug("Waiting for presence: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ── URL / Title ──────────────────────────────────────────────────────────

    /** Wait until the page URL contains the given substring. */ 
    public void waitForUrlContains(String urlFragment) {
        log.debug("Waiting for URL to contain: {}", urlFragment);
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    // ── Staleness ────────────────────────────────────────────────────────────

    /** Wait until the element is stale (detached from DOM). */
    public void waitForStaleness(WebElement element) {
        wait.until(ExpectedConditions.stalenessOf(element));
    }
}
