package com.dams.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static void initDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        // CRITICAL FOR CI
        if ("true".equals(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        driver.set(new ChromeDriver(options));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    public static WebDriver getDriver() {
        return driver.get();
    }
    public static void quitDriver() {
        getDriver().quit();
        driver.remove();
    }
}
