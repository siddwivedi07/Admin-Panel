package com.dams.driver.driverfactory;

import com.dams.core.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * DriverFactory - Responsible for creating and managing WebDriver instances.
 * Uses ThreadLocal for parallel test execution safety.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverFactory() { /* Prevent instantiation */ }

    /**
     * Initializes and returns a WebDriver instance based on config.
     */
    public static WebDriver initDriver() {
        String browser = config.getBrowser().trim().toLowerCase();
        boolean headless = config.isHeadless();

        log.info("Initializing '{}' browser | Headless: {}", browser, headless);

        WebDriver driver;
        switch (browser) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            case "edge":
                driver = createEdgeDriver(headless);
                break;
            default:
                log.warn("Unknown browser '{}'. Defaulting to Chrome.", browser);
                driver = createChromeDriver(headless);
        }

        applyTimeouts(driver);
        driverThreadLocal.set(driver);
        log.info("WebDriver initialized and stored in ThreadLocal.");
        return driver;
    }

    /**
     * Returns the current thread's WebDriver instance.
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call initDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the driver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("WebDriver quit and removed from ThreadLocal.");
        }
    }

    // ── Private helper methods ──────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }

    private static void applyTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        log.info("Timeouts applied — Implicit: {}s | PageLoad: {}s",
                config.getImplicitWait(), config.getPageLoadTimeout());
    }
}

