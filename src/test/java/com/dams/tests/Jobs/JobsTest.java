package com.dams.tests.jobs;

import com.dams.base.BaseTest;
import com.dams.pages.AppliedJobPage;
import com.dams.pages.JobPostPage;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JobsTest extends BaseTest {
    @Test(description = "Login → Applied Job → Job Post (end-to-end flow)")
    public void Jobs() {
        // ══════════════════════════════════════════════════════════════════════
        //  PHASE 1 — Login to admin portal
        // ══════════════════════════════════════════════════════════════════════
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        // ══════════════════════════════════════════════════════════════════════
        //  PHASE 2 — Navigate to Applied Job
        // ══════════════════════════════════════════════════════════════════════
        AppliedJobPage appliedJobPage = new AppliedJobPage(driver);
        appliedJobPage.navigateToAppliedJobs();
        sleep(5_000);
        ReportManager.logStep("Applied Job", "Navigate to Applied Jobs — Completed", true);
        // ══════════════════════════════════════════════════════════════════════
       
    // ── Private helpers ───────────────────────────────────────────────────────
    private void takeScreenshot(String label) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + label + "_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));
            ReportManager.logStep("Screenshot", "Captured: " + label, true, fileName);
        } catch (Exception e) {
            System.err.println("[JobTest] ✘ Screenshot failed (" + label + "): " + e.getMessage());
            ReportManager.logStep("Screenshot", "Capture failed: " + label, false);
        }
    }
    /** Convenience wrapper around Thread.sleep. */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
