package com.dams.core;

// ══════════════════════════════════════════════════════════════════════════════
//  DriverManager.java  —  DAMS Driver Layer
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya karta hai?
//     WebDriver instance create, manage aur close karta hai.
//     ThreadLocal use karta hai — parallel tests mein safe hai.
//
//  ✅ Kyu zaruri hai?
//     Browser setup ka logic ek jagah — BaseTest ya koi bhi class
//     seedha getDriver() call kare.
//
//  ✅ Real life example:
//     WebDriver driver = DriverManager.getDriver();
//     driver.get("https://...");
// ══════════════════════════════════════════════════════════════════════════════

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverManager {

    // ThreadLocal — har thread ka apna driver (parallel execution safe)
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // Private constructor — koi instance nahi banega (utility class)
    private DriverManager() {}

    // ──────────────────────────────────────────────────────────────────────────
    //  Driver initialize karo
    // ──────────────────────────────────────────────────────────────────────────
    public static void initDriver() {
        if (driverThread.get() == null) {
            WebDriver driver = createDriver();
            driverThread.set(driver);
            System.out.println("[DRIVER] ✔ ChromeDriver initialized. Headless=" + ConfigReader.isHeadless());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Driver instance lo (null nahi hoga agar initDriver call hua ho)
    // ──────────────────────────────────────────────────────────────────────────
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initDriver();
        }
        return driverThread.get();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Driver band karo + ThreadLocal clean karo (memory leak avoid)
    // ──────────────────────────────────────────────────────────────────────────
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("[DRIVER] ✔ Browser closed, memory cleaned.");
            } catch (Exception e) {
                System.err.println("[DRIVER] ⚠ Error closing driver: " + e.getMessage());
            } finally {
                driverThread.remove();  // ThreadLocal se hata do — memory clean
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Private: ChromeDriver create karna with all options
    // ──────────────────────────────────────────────────────────────────────────
    private static WebDriver createDriver() {
        // WebDriverManager automatically sahi ChromeDriver download karta hai
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = buildChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        // Timeouts set karo
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(ConfigReader.getPageLoadTimeout())
        );
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        driver.manage().window().maximize();

        return driver;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Private: ChromeOptions build karna
    // ──────────────────────────────────────────────────────────────────────────
    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // CI environment ya config se headless true ho to headless mode
        boolean headless = ConfigReader.isHeadless() || isCIEnvironment();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            System.out.println("[DRIVER] Running in HEADLESS mode (CI/config).");
        }

        // Common stable options
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");

        return options;
    }

    // GitHub Actions CI detect karna
    private static boolean isCIEnvironment() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
