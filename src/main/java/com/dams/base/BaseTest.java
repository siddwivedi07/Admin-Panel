package com.dams.base;

import com.dams.config.ConfigReader;
import com.dams.driver.DriverFactory;
import com.dams.pages.LoginPage;
import com.dams.report.CustomHtmlReporter;
import com.dams.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Listeners(com.dams.base.TestListener.class)
public class BaseTest {

    protected WebDriver    driver;
    protected LoginPage    loginPage;

    @BeforeSuite
    public void initReport() {
        CustomHtmlReporter.init();
    }

    @BeforeMethod
    public void setUp() {
        driver    = DriverFactory.initDriver();
        loginPage = new LoginPage(driver);

        // Step 1 – Open URL
        String url = ConfigReader.getBaseUrl();
        driver.get(url);
        CustomHtmlReporter.log("TC_1", "Init", "STEP 1 – Open URL: " + url, "PASS", "-");

        // Steps 2-5 – Full login flow
        loginPage.login(
            ConfigReader.getUsername(),
            ConfigReader.getPassword(),
            ConfigReader.getOtp()
        );
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String shot = ScreenshotUtils.capture(result.getMethod().getMethodName());
            CustomHtmlReporter.log("TC_ERR", "Failure",
                result.getMethod().getMethodName() + " failed", "FAIL",
                shot != null ? shot : "-");
        }
        DriverFactory.quitDriver();
    }

    @AfterSuite
    public void generateReport() {
        CustomHtmlReporter.generate();
    }
}

