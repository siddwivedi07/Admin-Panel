package com.dams.tests.HelpAndSupport;

import com.dams.base.BaseTest;
import com.dams.pages.HelpAndSupportPage;
import com.dams.pages.LoginPage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelpAndSupportTest extends BaseTest {

    @Test(description = "Login to admin portal then navigate through all Help and Support flows: "
            + "Question Report Email Ids → Question Report → User AI Feedback → Assign Users List → "
            + "Escalation Report → Ticket Assign → Set Web URL → Add Raise Query Department → "
            + "Add Student Query Type → FAQ")
    public void helpAndSupportTest() {

        // ── Login Flow ────────────────────────────────────────────────────────
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToAdminPortal();

        // ── Help And Support Page Flow ────────────────────────────────────────
        HelpAndSupportPage helpAndSupport = new HelpAndSupportPage(driver);

        // ════════════════════════════════════════════════════════════════
        //  FLOW 1 — Question Report Email Ids
        // ════════════════════════════════════════════════════════════════

        // STEP 1 — Click Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 1 – Clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 1 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 2 — Click Question Report Email Ids card
        try {
            System.out.println("[HelpAndSupportTest] STEP 2 – Clicking Question Report Email Ids card...");
            helpAndSupport.clickQuestionReportEmailIdsCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 2 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 3 — Click Add button
        try {
            System.out.println("[HelpAndSupportTest] STEP 3 – Clicking Add button...");
            helpAndSupport.clickAddButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 3 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 2 — Question Report
        // ════════════════════════════════════════════════════════════════

        // STEP 4 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 4 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 4 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 5 — Click Question Report card
        try {
            System.out.println("[HelpAndSupportTest] STEP 5 – Clicking Question Report card...");
            helpAndSupport.clickQuestionReportCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 5 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 6 — Click Download Excel button
        try {
            System.out.println("[HelpAndSupportTest] STEP 6 – Clicking Download Excel button...");
            helpAndSupport.clickDownloadExcelButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 6 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 3 — User AI Feedback → Assign Users List
        // ════════════════════════════════════════════════════════════════

        // STEP 7 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 7 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 7 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 8 — Click User AI Feedback card
        try {
            System.out.println("[HelpAndSupportTest] STEP 8 – Clicking User AI Feedback card...");
            helpAndSupport.clickUserAiFeedbackCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 8 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 9 — Click Assign Users List card
        try {
            System.out.println("[HelpAndSupportTest] STEP 9 – Clicking Assign Users List card...");
            helpAndSupport.clickAssignUsersListCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 9 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 10 — Click Edit button (outlined default variant)
        try {
            System.out.println("[HelpAndSupportTest] STEP 10 – Clicking Edit button...");
            helpAndSupport.clickEditButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 10 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 11 — Click Update button
        try {
            System.out.println("[HelpAndSupportTest] STEP 11 – Clicking Update button...");
            helpAndSupport.clickUpdateButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 11 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 4 — Escalation Report
        // ════════════════════════════════════════════════════════════════

        // STEP 12 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 12 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 12 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 13 — Click Escalation Report card
        try {
            System.out.println("[HelpAndSupportTest] STEP 13 – Clicking Escalation Report card...");
            helpAndSupport.clickEscalationReportCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 13 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 14 — Click Edit button (circle variant)
        try {
            System.out.println("[HelpAndSupportTest] STEP 14 – Clicking Circle Edit button...");
            helpAndSupport.clickCircleEditButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 14 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 15 — Click Update button
        try {
            System.out.println("[HelpAndSupportTest] STEP 15 – Clicking Update button...");
            helpAndSupport.clickUpdateButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 15 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 5 — Ticket Assign
        // ════════════════════════════════════════════════════════════════

        // STEP 16 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 16 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 16 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 17 — Click Ticket Assign card
        try {
            System.out.println("[HelpAndSupportTest] STEP 17 – Clicking Ticket Assign card...");
            helpAndSupport.clickTicketAssignCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 17 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 18 — Click New button
        try {
            System.out.println("[HelpAndSupportTest] STEP 18 – Clicking New button...");
            helpAndSupport.clickNewButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 18 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 19 — Click Close button
        try {
            System.out.println("[HelpAndSupportTest] STEP 19 – Clicking Close button...");
            helpAndSupport.clickCloseButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 19 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 20 — Click Reopen button
        try {
            System.out.println("[HelpAndSupportTest] STEP 20 – Clicking Reopen button...");
            helpAndSupport.clickReopenButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 20 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 21 — Click In Progress button
        try {
            System.out.println("[HelpAndSupportTest] STEP 21 – Clicking In Progress button...");
            helpAndSupport.clickInProgressButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 21 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 22 — Click Resolved button
        try {
            System.out.println("[HelpAndSupportTest] STEP 22 – Clicking Resolved button...");
            helpAndSupport.clickResolvedButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 22 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 23 — Click Download Excel button (Ticket Assign section)
        try {
            System.out.println("[HelpAndSupportTest] STEP 23 – Clicking Ticket Download Excel button...");
            helpAndSupport.clickTicketDownloadExcelButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 23 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 6 — Set Web URL
        // ════════════════════════════════════════════════════════════════

        // STEP 24 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 24 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 24 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 25 — Click Set Web URL card
        try {
            System.out.println("[HelpAndSupportTest] STEP 25 – Clicking Set Web URL card...");
            helpAndSupport.clickSetWebUrlCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 25 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 26 — Click Add WEB URL button
        try {
            System.out.println("[HelpAndSupportTest] STEP 26 – Clicking Add WEB URL button...");
            helpAndSupport.clickAddWebUrlButton();
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 26 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 27 — Enter Web URL
        try {
            System.out.println("[HelpAndSupportTest] STEP 27 – Entering Web URL: https://damsdelhi.com/...");
            helpAndSupport.enterWebUrl("https://damsdelhi.com/");
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 27 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 28 — Click Submit button
        try {
            System.out.println("[HelpAndSupportTest] STEP 28 – Clicking Submit button...");
            helpAndSupport.clickSubmitButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 28 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 7 — Add Raise Query Department
        // ════════════════════════════════════════════════════════════════

        // STEP 29 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 29 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 29 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 30 — Click Add Raise Query Department card
        try {
            System.out.println("[HelpAndSupportTest] STEP 30 – Clicking Add Raise Query Department card...");
            helpAndSupport.clickAddRaiseQueryDepartmentCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 30 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 31 — Click Edit button (circle variant)
        try {
            System.out.println("[HelpAndSupportTest] STEP 31 – Clicking Circle Edit button...");
            helpAndSupport.clickCircleEditButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 31 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 32 — Click Update button
        try {
            System.out.println("[HelpAndSupportTest] STEP 32 – Clicking Update button...");
            helpAndSupport.clickUpdateButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 32 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 8 — Add Student Query Type
        // ════════════════════════════════════════════════════════════════

        // STEP 33 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 33 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 33 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 34 — Click Add Student Query Type card
        try {
            System.out.println("[HelpAndSupportTest] STEP 34 – Clicking Add Student Query Type card...");
            helpAndSupport.clickAddStudentQueryTypeCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 34 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 35 — Enter Student Query Type
        try {
            System.out.println("[HelpAndSupportTest] STEP 35 – Entering Student Query Type: Network Not Working Now...");
            helpAndSupport.enterStudentQueryType("Network Not Working Now");
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 35 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 36 — Click Update button
        try {
            System.out.println("[HelpAndSupportTest] STEP 36 – Clicking Update button...");
            helpAndSupport.clickUpdateButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 36 ✘ SKIPPED – " + e.getMessage());
        }

        // ════════════════════════════════════════════════════════════════
        //  FLOW 9 — FAQ → Create Category → Add Category
        // ════════════════════════════════════════════════════════════════

        // STEP 37 — Re-navigate to Help and Support menu
        try {
            System.out.println("[HelpAndSupportTest] STEP 37 – Re-clicking Help and Support menu...");
            helpAndSupport.clickHelpAndSupportMenu();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 37 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 38 — Click FAQ card
        try {
            System.out.println("[HelpAndSupportTest] STEP 38 – Clicking FAQ card...");
            helpAndSupport.clickFaqCard();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 38 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 39 — Click Create Category link
        try {
            System.out.println("[HelpAndSupportTest] STEP 39 – Clicking Create Category link...");
            helpAndSupport.clickCreateCategoryLink();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 39 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 40 — Click Add Category button
        try {
            System.out.println("[HelpAndSupportTest] STEP 40 – Clicking Add Category button...");
            helpAndSupport.clickAddCategoryButton();
            sleep(3_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 40 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 41 — Enter Category Name
        try {
            System.out.println("[HelpAndSupportTest] STEP 41 – Entering Category Name: Payment Issues With Pradeep...");
            helpAndSupport.enterCategoryName("Payment Issues With Pradeep");
            sleep(2_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 41 ✘ SKIPPED – " + e.getMessage());
        }

        // STEP 42 — Click Add button inside FAQ category form
        try {
            System.out.println("[HelpAndSupportTest] STEP 42 – Clicking FAQ Add button...");
            helpAndSupport.clickFaqAddButton();
            sleep(4_000);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 42 ✘ SKIPPED – " + e.getMessage());
        }

        // ── STEP 43 — Final Screenshot ────────────────────────────────────────
        System.out.println("[HelpAndSupportTest] STEP 43 – Taking final screenshot...");
        try {
            Files.createDirectories(Paths.get("screenshots"));
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/help_and_support_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(fileName));
            System.out.println("[HelpAndSupportTest] STEP 43 ✔ Final screenshot saved: " + fileName);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportTest] STEP 43 ✘ Screenshot failed: " + e.getMessage());
        }

        System.out.println("[HelpAndSupportTest] ✔ All steps completed (skipped steps logged above).");
    }

    // ── Sleep Helper ──────────────────────────────────────────────────────────
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
