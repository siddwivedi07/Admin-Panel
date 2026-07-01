package com.dams.tests.Jobs;

import com.dams.base.BaseTest;
import com.dams.pages.AppliedJobPage;
import com.dams.pages.JobPostPage;
import com.dams.pages.LoginPage;
import com.dams.report.ReportManager;
import org.testng.annotations.Test;

/**
 * TestNG Test Class for Jobs Module.
 * Package : com.dams.tests.Jobs
 * Suite   : testng.xml → <class name="com.dams.tests.Jobs.JobsTest"/>
 *
 * Refactored from the monolithic JobsPage into two focused page objects:
 *   • AppliedJobPage — Jobs menu → Applied Job card → search → back
 *   • JobPostPage    — Job Post card → search → Add Job form → Post Job submit
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
}
