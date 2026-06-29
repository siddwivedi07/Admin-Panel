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

    @Test(description = "LoginTest")
    public void adminLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
    }
}
