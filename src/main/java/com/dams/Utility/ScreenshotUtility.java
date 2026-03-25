package com.dams.utility;

import com.dams.core.Config;
import com.dams.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtility.java — Screenshot Capture
 *
 * RESPONSIBILITY:
 *   This class has ONE job: capture a screenshot of the current browser state
 *   and save it to disk. It returns the file path so the report can embed it.
 *
 *   It does NOT generate reports, NOT control the driver, NOT run tests.
 *
 * HOW SCREENSHOTS WORK IN SELENIUM:
 *   WebDriver implements the TakesScreenshot interface.
 *   Casting it to TakesScreenshot unlocks the getScreenshotAs() method.
 *   OutputType.FILE tells Selenium to save the image as a temp PNG file.
 *   We then copy that temp file to our desired output directory.
 *
 * NAMING CONVENTION:
 *   Screenshots are named: <testName>_<timestamp>.png
 *   Example: loginTest_20240315143022.png
 *   Timestamp ensures no two screenshots ever overwrite each other.
 *
 * WHERE SAVED:
 *   Directory from config: screenshot.dir = test-output/screenshots
 *   The HTML report links to this path to embed the image inline.
 */
public class ScreenshotUtility {

    // Utility class — no instances needed
    private ScreenshotUtility() {}

    /**
     * Captures a screenshot of the current browser window.
     *
     * @param testName  Name of the test method (used in the filename)
     * @return          Absolute path of the saved PNG file, or null if capture failed
     */
    public static String captureScreenshot(String testName) {
        WebDriver driver;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            System.out.println("[ScreenshotUtility] No active driver — skipping screenshot.");
            return null;
        }

        // Build the output file path: screenshot.dir / testName_timestamp.png
        String timestamp     = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName      = sanitiseName(testName) + "_" + timestamp + ".png";
        String screenshotDir = Config.getInstance().getScreenshotDir();
        Path   outputPath    = Paths.get(screenshotDir, fileName);

        try {
            // Step 1: Ask Selenium to capture the screen as a temporary file
            File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Step 2: Create the output directory if it doesn't already exist
            Files.createDirectories(outputPath.getParent());

            // Step 3: Copy the temp file to our named output path
            Files.copy(tempFile.toPath(), outputPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[ScreenshotUtility] Screenshot saved: " + outputPath.toAbsolutePath());
            return outputPath.toAbsolutePath().toString();

        } catch (IOException e) {
            System.out.println("[ScreenshotUtility] Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Replaces characters that are invalid in file names (spaces, slashes, colons)
     * with underscores so the PNG file saves cleanly on all operating systems.
     */
    private static String sanitiseName(String name) {
        return (name == null || name.isEmpty()) ? "screenshot" : name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
