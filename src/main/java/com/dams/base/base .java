package com.dams.base;

import com.dams.core.utils.WaitUtils;
import com.dams.driver.driverfactory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base - Base class for all Page Objects.
 * Provides common WebDriver interactions and PageFactory initialization.
 */
public class Base {

    protected static final Logger log = LogManager.getLogger(Base.class);
    protected WebDriver driver;

    /**
     * Constructor: initializes PageFactory elements using the current driver.
     */
    public Base() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
        log.debug("PageFactory initialized for: {}", this.getClass().getSimpleName());
    }

    /**
     * Navigates the browser to the given URL.
     */
    protected void navigateTo(String url) {
        log.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    /**
     * Returns the current page title.
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the current URL.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     */
    protected void click(WebElement element) {
        WaitUtils.waitForClickability(element).click();
        log.debug("Clicked element: {}", element);
    }

    /**
     * Types text into a field after clearing it.
     */
    protected void type(WebElement element, String text) {
        WaitUtils.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
        log.debug("Typed '{}' into element", text);
    }

    /**
     * Scrolls an element into view using JavaScript.
     */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Checks whether an element is displayed on the page.
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            log.warn("Element not found or not displayed.");
            return false;
        }
    }
}

