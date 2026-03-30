package com.dams.tests.login;

import com.dams.base.BaseTest;
import com.dams.pages.LoginPage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginTest extends BaseTest {

    @Test(description = "Admin login with email, password, and OTP")
    public void adminLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();

        // ── Wait 20s for dashboard to fully load, then screenshot ──
        System.out.println("[LoginTest] Waiting 20s for dashboard to fully load...");
        try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/login_dashboard_" + timestamp + ".png";

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));

            System.out.println("[LoginTest] ✔ Dashboard screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[LoginTest] ✘ Screenshot failed: " + e.getMessage());
        }
    }
}
