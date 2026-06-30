package com.dams.tests.Opportunity;

import com.dams.base.BaseTest;
import com.dams.pages.LoginPage;
import com.dams.pages.OpportunityPage;
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
 * TestNG Test Class for Opportunity Module.
 * Package : com.dams.tests.Opportunity
 * Suite   : testng.xml → <class name="com.dams.tests.Opportunity.OpportunityTest"/>
 *
 * Single @Test method so all steps run in one browser session.
 *
 * Flow:
 *   TC_01 – Click Opportunity menu link (/opportunity)
 *   TC_02 – Opportunity Email Configure
 *             a) Click card → Add Opportunity Email Configure → enter email → Save
 *             b) Search by email
 *             c) Edit email → update → Save
 *             d) Delete email
 *             e) Navigate back from Opportunity Email Configure
 *   TC_03 – Add Opportunity
 *             a) Click card → Add Opportunity button → fill Category/Title/City/Description → Create
 *             b) Search by title, category, city
 *             c) Navigate back from Add Opportunity
 *   TC_04 – Opportunity Enquiry List
 *             a) Click card → Download Excel
 *             b) Search by name, email, phone, remark
 *             c) Delete entry
 *             d) Navigate back from Opportunity Enquiry List
 *   TC_05 – Add Category
 *             a) Click card → Add Category button → enter name → Save
 *             b) Search by category name
 *             c) Edit category
 *             d) Delete category
 */
public class OpportunityTest extends BaseTest {

    private static final String EMAIL_VALUE = "ashutosh.mago@damsdelhi.com";

    @Test(description = "Opportunity – full flow: login → Opportunity Email Configure → "
            + "Add Opportunity → Opportunity Enquiry List → Add Category")
    public void opportunityFullFlowTest() {

        // ── Step 0: Login ────────────────────────────────────────────────────
        System.out.println("[OpportunityTest] Step 0: Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();
        ReportManager.logStep("Opportunity", "Step 0 – Login", true);
        sleep(5_000);

        OpportunityPage page = new OpportunityPage(driver);

        // ── TC_01: Click Opportunity menu link ─────────────────────────────────
        page.clickOpportunityMenu();
        ReportManager.logStep("Opportunity", "TC_01 – Click Opportunity Menu", true);
        sleep(3_000);
        takeScreenshot("tc01_opportunity_menu");

        // ── TC_02: Opportunity Email Configure ──────────────────────────────────
        page.clickOpportunityEmailConfigureCard();
        ReportManager.logStep("Opportunity", "TC_02a – Click Opportunity Email Configure Card", true);
        sleep(3_000);
        takeScreenshot("tc02a_opportunity_email_configure_card");

        page.clickAddOpportunityEmailConfigureButton();
        ReportManager.logStep("Opportunity", "TC_02b – Click Add Opportunity Email Configure", true);
        sleep(2_000);
        takeScreenshot("tc02b_add_email_configure_popup");

        page.enterEmailAndSave(EMAIL_VALUE);
        ReportManager.logStep("Opportunity", "TC_02c – Enter Email and Save", true);
        sleep(2_000);
        takeScreenshot("tc02c_email_saved");

        page.searchInGenericBox(EMAIL_VALUE);
        ReportManager.logStep("Opportunity", "TC_02d – Search Email", true);
        sleep(2_000);
        takeScreenshot("tc02d_search_email");

        page.clickEditButton();
        ReportManager.logStep("Opportunity", "TC_02e – Click Edit Button", true);
        sleep(2_000);
        takeScreenshot("tc02e_edit_email_popup");

        page.updateEmailAndSave(EMAIL_VALUE);
        ReportManager.logStep("Opportunity", "TC_02f – Update Email and Save", true);
        sleep(2_000);
        takeScreenshot("tc02f_email_updated");

        page.clickDeleteButton();
        ReportManager.logStep("Opportunity", "TC_02g – Delete Email", true);
        sleep(2_000);
        takeScreenshot("tc02g_email_deleted");

        page.navigateBackFromOpportunityEmailConfigure();
        ReportManager.logStep("Opportunity", "TC_02h – Navigate Back from Opportunity Email Configure", true);
        sleep(3_000);
        takeScreenshot("tc02h_back_from_email_configure");

        // ── TC_03: Add Opportunity ──────────────────────────────────────────────
        page.clickAddOpportunityCard();
        ReportManager.logStep("Opportunity", "TC_03a – Click Add Opportunity Card", true);
        sleep(3_000);
        takeScreenshot("tc03a_add_opportunity_card");

        page.clickAddOpportunityButton();
        ReportManager.logStep("Opportunity", "TC_03b – Click Add Opportunity Button", true);
        sleep(2_000);
        takeScreenshot("tc03b_add_opportunity_form");

        page.selectCategory("Academic & Education");
        page.enterTitle("selenium");
        page.enterCity("delhi");
        page.enterDescription("selenium");
        ReportManager.logStep("Opportunity", "TC_03c – Fill Opportunity Form", true);
        sleep(1_000);
        takeScreenshot("tc03c_opportunity_form_filled");

        page.clickCreateButton();
        ReportManager.logStep("Opportunity", "TC_03d – Click Create", true);
        sleep(2_000);
        takeScreenshot("tc03d_opportunity_created");

        page.searchOpportunity("QA Opportunity");
        ReportManager.logStep("Opportunity", "TC_03e – Search Opportunity", true);
        sleep(2_000);
        takeScreenshot("tc03e_search_opportunity");

        page.navigateBackFromAddOpportunity();
        ReportManager.logStep("Opportunity", "TC_03f – Navigate Back from Add Opportunity", true);
        sleep(3_000);
        takeScreenshot("tc03f_back_from_add_opportunity");

        // ── TC_04: Opportunity Enquiry List ─────────────────────────────────────
        page.clickOpportunityEnquiryListCard();
        ReportManager.logStep("Opportunity", "TC_04a – Click Opportunity Enquiry List Card", true);
        sleep(3_000);
        takeScreenshot("tc04a_opportunity_enquiry_list_card");

        page.clickDownloadExcel();
        ReportManager.logStep("Opportunity", "TC_04b – Click Download Excel", true);
        sleep(2_000);
        takeScreenshot("tc04b_download_excel");

        page.searchEnquiry("testing");
        ReportManager.logStep("Opportunity", "TC_04c – Search Enquiry", true);
        sleep(2_000);
        takeScreenshot("tc04c_search_enquiry");

        page.clickEnquiryDeleteButton();
        ReportManager.logStep("Opportunity", "TC_04d – Delete Enquiry", true);
        sleep(2_000);
        takeScreenshot("tc04d_enquiry_deleted");

        page.navigateBackFromOpportunityEnquiryList();
        ReportManager.logStep("Opportunity", "TC_04e – Navigate Back from Opportunity Enquiry List", true);
        sleep(3_000);
        takeScreenshot("tc04e_back_from_enquiry_list");

        // ── TC_05: Add Category ──────────────────────────────────────────────────
        page.clickAddCategoryCard();
        ReportManager.logStep("Opportunity", "TC_05a – Click Add Category Card", true);
        sleep(3_000);
        takeScreenshot("tc05a_add_category_card");

        page.clickAddCategoryButton();
        ReportManager.logStep("Opportunity", "TC_05b – Click Add Category Button", true);
        sleep(2_000);
        takeScreenshot("tc05b_add_category_popup");

        page.enterCategoryNameAndSave("Academic & Education");
        ReportManager.logStep("Opportunity", "TC_05c – Enter Category Name and Save", true);
        sleep(2_000);
        takeScreenshot("tc05c_category_saved");

        page.searchInGenericBox("Academic & Education");
        ReportManager.logStep("Opportunity", "TC_05d – Search Category", true);
        sleep(2_000);
        takeScreenshot("tc05d_search_category");

        page.clickEditButton();
        ReportManager.logStep("Opportunity", "TC_05e – Click Edit Category", true);
        sleep(2_000);
        takeScreenshot("tc05e_edit_category");

        page.clickDeleteButton();
        ReportManager.logStep("Opportunity", "TC_05f – Delete Category", true);
        sleep(2_000);
        takeScreenshot("tc05f_category_deleted");

        System.out.println("[OpportunityTest] ✅ opportunityFullFlowTest PASSED");
    }

    // ── Screenshot helper ─────────────────────────────────────────────────────

    private void takeScreenshot(String testName) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));

            System.out.println("[OpportunityTest] ✔ Screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[OpportunityTest] ✘ Screenshot failed: " + e.getMessage());
        }
    }

    // ── Sleep helper ──────────────────────────────────────────────────────────

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
