package com.dams.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * DriverManager
 * ─────────────────────────────────────────────────────────────────────────────
 * Thread-safe WebDriver factory.  Uses a ThreadLocal so parallel test suites
 * each get their own driver instance without sharing state.
 *
 * Only Chrome is required for this project; other browsers can be added later.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // ── Initialise ───────────────────────────────────────────────────────────
    public static void initDriver() {
        ConfigReader cfg = ConfigReader.getInstance();
        String browser = cfg.getBrowser().toLowerCase();

        WebDriver driver;

        switch (browser) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = buildChromeOptions(cfg.isHeadless());
                driver = new ChromeDriver(options);
                log.info("ChromeDriver initialised (headless={})", cfg.isHeadless());
            }
            default -> throw new RuntimeException("Unsupported browser: " + browser);
        }

        // Global timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(cfg.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(cfg.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driverThread.set(driver);
    }

    // ── Accessor ─────────────────────────────────────────────────────────────
    public static WebDriver getDriver() {
        WebDriver driver = driverThread.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialised. Call DriverManager.initDriver() first.");
        }
        return driver;
    }

    // ── Teardown ─────────────────────────────────────────────────────────────
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("WebDriver quit and ThreadLocal cleared.");
        }
    }

    // ── Private helpers ──────────────────────────────────────────────────────
    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");   // Selenium 4 headless flag
        }

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--disable-popup-blocking",
                "--ignore-certificate-errors"
        );

        return options;
    }
}
