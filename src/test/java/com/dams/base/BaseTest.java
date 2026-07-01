package com.dams.base;

import com.dams.core.config.Config;
import com.dams.core.driver.DriverFactory;
import com.dams.report.ReportManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setup(ITestResult result) {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        driver.get(Config.BASE_URL);
        // 🔥 Start report test tracking
        ReportManager.startTest(result.getMethod().getMethodName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            String screenshotPath = captureScreenshot(result.getName());
            boolean isPass = result.getStatus() == ITestResult.SUCCESS;
            ReportManager.logStep(
                    "Execution",
                    result.getName(),
                    isPass,
                    screenshotPath
            );
        } catch (Exception e) {
            System.out.println("Error capturing screenshot: " + e.getMessage());
        }
        DriverFactory.quitDriver();
    }

    protected String captureScreenshot(String testName) {
        try {
            new File("screenshots").mkdirs();
            String fileName = testName + "_" + System.currentTimeMillis() + ".png";
            String path = "screenshots/" + fileName;
            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (Exception e) {
            return null;
        }
    }
}
