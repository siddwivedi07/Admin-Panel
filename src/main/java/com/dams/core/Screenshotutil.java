package com.dams.utils;

// ══════════════════════════════════════════════════════════════════════════════
//  ScreenshotUtil.java  —  DAMS Utility Layer
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya karta hai?
//     Test fail hone par ya manually call karne par screenshot leta hai
//     aur test-output/screenshots/ mein save karta hai.
//
//  ✅ Kyu zaruri hai?
//     Debugging ke liye — failure ka visual proof
//     ExtentReport mein embed hota hai
//
//  ✅ Usage:
//     String path = ScreenshotUtil.capture(driver, "LoginFailed");
// ══════════════════════════════════════════════════════════════════════════════

import com.dams.core.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    // Private constructor — utility class, no instances
    private ScreenshotUtil() {}

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ──────────────────────────────────────────────────────────────────────────
    //  Screenshot lo aur path return karo (ExtentReport embed ke liye)
    // ──────────────────────────────────────────────────────────────────────────
    public static String capture(WebDriver driver, String testName) {
        if (driver == null) {
            System.err.println("[SCREENSHOT] ⚠ Driver is null, cannot capture screenshot.");
            return null;
        }

        // Screenshot directory ensure karo
        String screenshotDir = ConfigReader.getScreenshotPath();
        File dir = new File(screenshotDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // File name: TestName_yyyyMMdd_HHmmss.png
        String timestamp  = LocalDateTime.now().format(FORMATTER);
        String fileName   = testName + "_" + timestamp + ".png";
        String destPath   = screenshotDir + fileName;

        try {
            // Screenshot capture karo
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[SCREENSHOT] ✔ Saved: " + destPath);
            return destPath;  // ExtentReport embed ke liye path return karo

        } catch (IOException e) {
            System.err.println("[SCREENSHOT] ❌ Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Convenience: "FAILED_" prefix ke saath capture
    // ──────────────────────────────────────────────────────────────────────────
    public static String captureOnFailure(WebDriver driver, String testName) {
        return capture(driver, "FAILED_" + testName);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Convenience: "PASSED_" prefix ke saath capture
    // ──────────────────────────────────────────────────────────────────────────
    public static String captureOnPass(WebDriver driver, String testName) {
        return capture(driver, "PASSED_" + testName);
    }
}
