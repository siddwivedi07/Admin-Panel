package com.dams.test;

import com.dams.base.BaseTest;
import com.dams.report.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest.java — Login Test Cases
 *
 * RESPONSIBILITY:
 *   Contains @Test methods that verify the DAMS Admin Panel login flow.
 *   Each test inherits browser setup, login, and teardown from BaseTest.
 *
 * HOW IT WORKS:
 *   By extending BaseTest:
 *     - Browser opens and login completes BEFORE each @Test (via @BeforeMethod)
 *     - Browser closes AFTER each @Test (via @AfterMethod)
 *     - HTML report is initialised/flushed by @BeforeSuite/@AfterSuite
 *
 *   This means every @Test here starts with the user already logged in
 *   and on the DAMS dashboard.
 *
 * WHAT WE ASSERT:
 *   After a successful login+OTP flow, the browser should:
 *   1. Land on a URL containing "devadmin.damsdelhi.com" (still on the right domain)
 *   2. NOT be on the login page anymore (URL should not end at "/" or "/login")
 *   3. Have a non-empty page title
 *
 * ADDING MORE TESTS:
 *   Add new @Test methods to this class.
 *   Each one automatically gets a fresh browser + fresh login.
 */
public class LoginTest extends BaseTest {

    /**
     * TEST 1 — Verify Successful Login and Dashboard Navigation
     *
     * Precondition (handled by @BeforeMethod in BaseTest):
     *   - Browser opened on https://devadmin.damsdelhi.com/
     *   - Email, Password, Login, OTP, Submit — all done
     *
     * What this test verifies:
     *   - After full login, we are on the correct domain (devadmin.damsdelhi.com)
     *   - We have moved past the login page (URL does not end at root "/")
     *   - The page has a title (dashboard loaded, not a blank/error page)
     */
    @Test(description = "Verify user is redirected to dashboard after valid login and OTP")
    public void verifySuccessfulLoginNavigatesToDashboard() {

        // ── Gather current state ─────────────────────────────────────────
        String currentUrl   = loginPage.getCurrentUrl();
        String currentTitle = loginPage.getPageTitle();

        System.out.println("[LoginTest] Current URL:   " + currentUrl);
        System.out.println("[LoginTest] Current Title: " + currentTitle);

        ReportManager.logInfo("Current URL after login: "   + currentUrl);
        ReportManager.logInfo("Current Page Title: "         + currentTitle);

        // ── Assertion 1: Still on the correct domain ─────────────────────
        Assert.assertTrue(
            currentUrl.contains("devadmin.damsdelhi.com"),
            "URL should contain 'devadmin.damsdelhi.com'. Actual: " + currentUrl
        );
        ReportManager.logPass("ASSERTION 1 PASSED — URL contains 'devadmin.damsdelhi.com'");

        // ── Assertion 2: No longer on the login root page ─────────────────
        // After login, URL should include a path beyond just "/"
        Assert.assertFalse(
            currentUrl.endsWith("/") && !currentUrl.contains("dashboard"),
            "URL should navigate beyond the root login page. Actual: " + currentUrl
        );
        ReportManager.logPass("ASSERTION 2 PASSED — Navigated past the login page");

        // ── Assertion 3: Page title is not empty ──────────────────────────
        Assert.assertNotNull(currentTitle,  "Page title should not be null.");
        Assert.assertFalse(currentTitle.isEmpty(), "Page title should not be empty.");
        ReportManager.logPass("ASSERTION 3 PASSED — Page title is: '" + currentTitle + "'");

        System.out.println("[LoginTest] ✔ verifySuccessfulLoginNavigatesToDashboard — PASSED");
    }

    /**
     * TEST 2 — Verify OTP Field Appeared After Clicking Login
     *
     * This test re-does the login steps individually to verify that:
     *   - After entering credentials and clicking Login, the OTP screen appears
     *
     * NOTE: BaseTest @BeforeMethod already does the FULL login.
     *       So by the time this test runs, we're on the dashboard.
     *       This test verifies the post-login state only.
     *
     *       To test mid-flow OTP visibility, you'd need a separate flow that
     *       navigates back to login — shown here as a URL-based assertion.
     */
    @Test(description = "Verify dashboard URL confirms OTP was accepted and login succeeded")
    public void verifyDashboardUrlAfterOtpSubmit() {

        String url = loginPage.getCurrentUrl();
        ReportManager.logInfo("Post-OTP URL: " + url);

        System.out.println("[LoginTest] Post-OTP URL: " + url);

        // After OTP submit, the URL should not be the bare root login URL
        boolean loggedIn = url.contains("devadmin.damsdelhi.com")
                           && !url.equals("https://devadmin.damsdelhi.com/");

        Assert.assertTrue(
            loggedIn,
            "After OTP submit, user should be past the login page. Actual URL: " + url
        );

        ReportManager.logPass("ASSERTION PASSED — OTP accepted, dashboard URL confirmed: " + url);
        System.out.println("[LoginTest] ✔ verifyDashboardUrlAfterOtpSubmit — PASSED");
    }
}

