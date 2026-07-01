package com.dams.tests.Jobs;

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

/**
 * TestNG Test Class for Jobs Module.
 * Package : com.dams.tests.Jobs
 * Suite   : testng.xml → <class name="com.dams.tests.Jobs.JobsTest"/>
 *
 * Flow:
 *   PHASE 1 – Login to admin portal
 *   PHASE 2 – Navigate to Applied Jobs (Jobs menu → Applied Job card)
 *   PHASE 3 – Post a new Job (Job Post card → form fill → submit)
 */
public class JobsTest extends BaseTest {

    @Test(description = "Login → Applied Job → Job Post (end-to-end flow)")
    public void Jobs() {

        // ══════════════════════════════════════════════════════════════════════
        //  PHASE 1 — Login to admin portal
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("[JobsTest] Phase 1 → Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("Jobs", "Phase 1 – Login", true);
        sleep(5_000);

        // ══════════════════════════════════════════════════════════════════════
        //  PHASE 2 — Navigate to Applied Job
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("[JobsTest] Phase 2 → Navigating to Applied Jobs...");
        AppliedJobPage appliedJobPage = new AppliedJobPage(driver);
        appliedJobPage.navigateToAppliedJobs();
        sleep(5_000);
        takeScreenshot("phase2_applied_job");
        ReportManager.logStep("Applied Job", "Phase 2 – Navigate to Applied Jobs — Completed", true);

        // ══════════════════════════════════════════════════════════════════════
        //  PHASE 3 — Post a new Job
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("[JobsTest] Phase 3 → Posting a new Job...");
        JobPostPage jobPostPage = new JobPostPage(driver);
        jobPostPage.postJob();
        sleep(10_000);
        takeScreenshot("phase3_job_posted");
        ReportManager.logStep("Job Post", "Phase 3 – Post New Job — Completed", true);

        System.out.println("[JobsTest] ✅ Jobs test PASSED");
    }

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
            System.err.println("[JobsTest] ✘ Screenshot failed (" + label + "): " + e.getMessage());
            ReportManager.logStep("Screenshot", "Capture failed: " + label, false);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
