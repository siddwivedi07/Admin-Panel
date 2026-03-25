package com.dams.base;

import com.dams.core.DriverManager;
import com.dams.core.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage
 * ─────────────────────────────────────────────────────────────────────────────
 * Parent class for every Page Object.  Provides:
 *   • a shared WebDriver reference (from DriverManager)
 *   • WaitUtils for explicit waits
 *   • common interactions (click, type, getText) with built-in waits
 *   • screenshot capture utility
 *
 * All page objects extend this class and call super() in their constructor.
 */
public abstract class BasePage {

    protected static final Logger log = LogManager.getLogger(BasePage.class);

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ── Interactions ─────────────────────────────────────────────────────────

    /**
     * Click an element after waiting for it to be clickable.
     */
    protected void click(By locator) {
        log.debug("Clicking element: {}", locator);
        wait.waitForClickable(locator).click();
    }

    protected void click(WebElement element) {
        log.debug("Clicking WebElement");
        wait.waitForClickable(element).click();
    }

    /**
     * Clear a field, then type the given text.
     */
    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = wait.waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        log.debug("Typing '{}' into element", text);
        WebElement el = wait.waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(By locator) {
        return wait.waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return wait.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Screenshot ───────────────────────────────────────────────────────────

    /**
     * Capture a screenshot and return the raw PNG bytes.
     * Callers (e.g. ReportManager) are responsible for saving/embedding it.
     */
    public byte[] captureScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
