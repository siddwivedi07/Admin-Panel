package com.dams.core.utils;

import com.dams.core.config.ConfigReader;
import com.dams.driver.driverfactory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils - Provides explicit wait utilities for WebDriver interactions.
 */
public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private static final int TIMEOUT = ConfigReader.getInstance().getExplicitWait();

    private WaitUtils() { /* Utility class */ }

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    /**
     * Wait until the element is visible on the page.
     */
    public static WebElement waitForVisibility(WebElement element) {
        log.debug("Waiting for element to be visible...");
        return getWait(DriverFactory.getDriver())
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait until the element located by locator is visible.
     */
    public static WebElement waitForVisibility(By locator) {
        log.debug("Waiting for locator to be visible: {}", locator);
        return getWait(DriverFactory.getDriver())
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until the element is clickable.
     */
    public static WebElement waitForClickability(WebElement element) {
        log.debug("Waiting for element to be clickable...");
        return getWait(DriverFactory.getDriver())
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until the URL contains a specific substring.
     */
    public static boolean waitForUrlContains(String urlFragment) {
        log.debug("Waiting for URL to contain: {}", urlFragment);
        return getWait(DriverFactory.getDriver())
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Wait until the page title contains a given text.
     */
    public static boolean waitForTitleContains(String titleFragment) {
        log.debug("Waiting for title to contain: {}", titleFragment);
        return getWait(DriverFactory.getDriver())
                .until(ExpectedConditions.titleContains(titleFragment));
    }
}

