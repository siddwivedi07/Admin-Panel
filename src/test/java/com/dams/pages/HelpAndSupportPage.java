package com.dams.pages;

import com.dams.report.ReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class HelpAndSupportPage {

    // ── Locators ──────────────────────────────────────────────────────────────

    // STEP 1 — Help and Support menu item in sidebar
    private static final By HELP_SUPPORT_MENU_ITEM =
            By.cssSelector("a[href='/help-support']");

    // STEP 2 — Question Report Email Ids card
    private static final By QUESTION_REPORT_EMAIL_IDS_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Question Report Email Ids']/ancestor::div[@class='ant-card-body']");

    // STEP 3 — Add button (submit type, primary solid)
    // Note: The span contains "Add " with a trailing space, so we use contains() instead of exact normalize-space match
    private static final By ADD_BTN =
            By.xpath("//button[@type='submit' and contains(@class,'ant-btn-primary') and .//span[contains(text(),'Add')]]");

    // STEP 4 — Question Report card
    private static final By QUESTION_REPORT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Question Report']/ancestor::div[@class='ant-card-body']");

    // STEP 5 — Download Excel button (Question Report section)
    private static final By DOWNLOAD_EXCEL_BTN =
            By.xpath("//button[contains(@class,'ant-btn-primary') and .//span[normalize-space(text())='Download Excel']]");

    // STEP 6 — User AI Feedback card
    private static final By USER_AI_FEEDBACK_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='User AI Feedback']/ancestor::div[@class='ant-card-body']");

    // STEP 7 — Assign Users List card
    // Note: uses contains() to handle any casing variation in the text
    private static final By ASSIGN_USERS_LIST_CARD =
            By.xpath("//div[@class='textData' and contains(normalize-space(text()),'Assign users list') or @class='textData' and contains(normalize-space(text()),'Assign Users List')]/ancestor::div[@class='ant-card-body']");

    // STEP 8 — Edit button (outlined, default variant with Edit icon + 'Edit' text)
    private static final By EDIT_BTN =
            By.xpath("(//button[contains(@class,'ant-btn-default') and contains(@class,'ant-btn-variant-outlined') and .//span[normalize-space(text())='Edit']])[1]");

    // STEP 9 — Update button (submit type, primary solid)
    private static final By UPDATE_BTN =
            By.xpath("//button[@type='submit' and contains(@class,'ant-btn-primary') and .//span[normalize-space(text())='Update']]");

    // STEP 10 — Escalation Report card
    private static final By ESCALATION_REPORT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Escalation Report']/ancestor::div[@class='ant-card-body']");

    // STEP 11 — Edit button (circle, outlined variant — used in Escalation Report)
    private static final By CIRCLE_EDIT_BTN =
            By.xpath("(//button[contains(@class,'ant-btn-circle') and contains(@class,'ant-btn-default') and .//span[normalize-space(text())='Edit']])[1]");

    // STEP 12 — Update button (same locator re-used for Escalation Report section)
    // Reuses UPDATE_BTN defined at STEP 9.

    // STEP 13 — Ticket Assign card
    private static final By TICKET_ASSIGN_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Ticket Assign']/ancestor::div[@class='ant-card-body']");

    // STEP 14 — New button (blue border countdata)
    private static final By NEW_BTN =
            By.xpath("//button[@class='countdata' and .//h3/text()='New' or //button[@class='countdata' and .//h3[contains(.,'New')]]]");

    // STEP 15 — Close button (red border countdata)
    private static final By CLOSE_BTN =
            By.xpath("//button[@class='countdata' and .//h3[contains(.,'Close')]]");

    // STEP 16 — Reopen button (blue border countdata)
    private static final By REOPEN_BTN =
            By.xpath("//button[@class='countdata' and .//h3[contains(.,'Reopen')]]");

    // STEP 17 — In Progress button (orange border countdata)
    private static final By IN_PROGRESS_BTN =
            By.xpath("//button[@class='countdata' and .//h3[contains(.,'In Progress')]]");

    // STEP 18 — Resolved button (green border countdata)
    private static final By RESOLVED_BTN =
            By.xpath("//button[@class='countdata' and .//h3[contains(.,'Resolved')]]");

    // STEP 19 — Download Excel button (Ticket Assign section — with download icon)
    private static final By TICKET_DOWNLOAD_EXCEL_BTN =
            By.xpath("//button[contains(@class,'ant-btn-primary') and .//span[@aria-label='download'] and .//span[normalize-space(text())='Download Excel']]");

    // STEP 20 — Set Web URL card
    private static final By SET_WEB_URL_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Set Web URL']/ancestor::div[@class='ant-card-body']");

    // STEP 21 — Add WEB URL button (outlined, plus-circle icon)
    private static final By ADD_WEB_URL_BTN =
            By.xpath("//button[contains(@class,'ant-btn-default') and contains(@class,'ant-btn-variant-outlined') and .//span[normalize-space(text())='Add WEB URL']]");

    // STEP 22 — Web URL input field
    private static final By WEB_URL_INPUT =
            By.id("web_url");

    // STEP 23 — Submit button (primary solid)
    private static final By SUBMIT_BTN =
            By.xpath("//button[@type='submit' and contains(@class,'ant-btn-primary') and .//span[normalize-space(text())='Submit']]");

    // STEP 24 — Add Raise Query Department card
    private static final By ADD_RAISE_QUERY_DEPT_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Add Raise Query Department']/ancestor::div[@class='ant-card-body']");

    // STEP 25 — Edit button for Raise Query Dept (circle, outlined, edit icon + 'Edit')
    // Reuses CIRCLE_EDIT_BTN defined at STEP 11.

    // STEP 26 — Update button for Raise Query Dept
    // Reuses UPDATE_BTN defined at STEP 9.

    // STEP 27 — Add Student Query Type card
    private static final By ADD_STUDENT_QUERY_TYPE_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='Add Student Query Type']/ancestor::div[@class='ant-card-body']");

    // STEP 28 — Student Query Type input field
    private static final By STUDENT_QUERY_TYPE_INPUT =
            By.id("problem_type");

    // STEP 29 — Update button for Student Query Type (submit, primary)
    // Reuses UPDATE_BTN defined at STEP 9.

    // STEP 30 — FAQ card
    private static final By FAQ_CARD =
            By.xpath("//div[@class='textData' and normalize-space(text())='FAQ']/ancestor::div[@class='ant-card-body']");

    // STEP 31 — Create Category link (anchor with 'Create Category' text)
    private static final By CREATE_CATEGORY_LINK =
            By.xpath("//a[@href='/help-support/help-support-category' and contains(.,'Create Category')]");

    // STEP 32 — Add Category button (outlined, plus-circle icon)
    private static final By ADD_CATEGORY_BTN =
            // Note: span text has a leading space ' Add Category' — use contains() to avoid exact-match failure
            By.xpath("//button[contains(@class,'ant-btn-default') and contains(@class,'ant-btn-variant-outlined') and .//span[contains(text(),'Add Category')]]");

    // STEP 33 — Category Name input field (FAQ section)
    private static final By CATEGORY_NAME_INPUT =
            By.id("helptitle");

    // STEP 34 — Add button inside FAQ category modal (primary solid)
    private static final By FAQ_ADD_BTN =
            // Scoped inside ant-form-item-control-input-content to avoid collision with other Add buttons on the page
            By.xpath("//div[contains(@class,'ant-form-item-control-input-content')]//button[@type='submit' and contains(@class,'ant-btn-primary') and .//span[normalize-space(text())='Add']]");

    // ── Modal selectors (used by guard helper) ────────────────────────────────
    private static final By MODAL_WRAP       = By.cssSelector("div.ant-modal-wrap");
    private static final By MODAL_CLOSE_BTN  = By.cssSelector("div.ant-modal-wrap button.ant-modal-close");
    private static final By MODAL_CANCEL_BTN =
            By.xpath("//div[@class='ant-modal-footer']//button[.//span[normalize-space(text())='Cancel']]");

    // ── Driver & Wait ─────────────────────────────────────────────────────────
    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    public HelpAndSupportPage(WebDriver driver) {
        this.driver    = driver;
        this.wait      = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPER — Dismiss any open Ant Design modal before navigating away.
    //  Strategy: Cancel btn → X close btn → ESC key (in that order).
    // ══════════════════════════════════════════════════════════════════════════
    public void closeModalIfPresent() {
        try {
            List<WebElement> modals = driver.findElements(MODAL_WRAP);
            if (!modals.isEmpty() && modals.get(0).isDisplayed()) {
                System.out.println("[HelpAndSupportPage] ⚠ Modal overlay detected — closing before navigation...");

                List<WebElement> cancelBtns = driver.findElements(MODAL_CANCEL_BTN);
                if (!cancelBtns.isEmpty() && cancelBtns.get(0).isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancelBtns.get(0));
                    System.out.println("[HelpAndSupportPage] ✔ Modal closed via Cancel button.");
                } else {
                    List<WebElement> closeBtns = driver.findElements(MODAL_CLOSE_BTN);
                    if (!closeBtns.isEmpty()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtns.get(0));
                        System.out.println("[HelpAndSupportPage] ✔ Modal closed via X button.");
                    } else {
                        driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);
                        System.out.println("[HelpAndSupportPage] ✔ Modal dismissed via ESC key.");
                    }
                }
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_WRAP));
                System.out.println("[HelpAndSupportPage] ✔ Modal overlay cleared.");
            }
        } catch (Exception ex) {
            System.out.println("[HelpAndSupportPage] No blocking modal found (safe to continue): " + ex.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 1 — Click Help and Support menu (modal-safe)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickHelpAndSupportMenu() {
        try {
            closeModalIfPresent();
            WebElement menu = wait.until(
                    ExpectedConditions.elementToBeClickable(HELP_SUPPORT_MENU_ITEM));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
            System.out.println("[HelpAndSupportPage] STEP 1 ✔ Help and Support menu clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 1 – Click Help And Support Menu", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 1 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 1 – Click Help And Support Menu", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 2 — Click Question Report Email Ids card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickQuestionReportEmailIdsCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(QUESTION_REPORT_EMAIL_IDS_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 2 ✔ Question Report Email Ids card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 2 – Click Question Report Email Ids Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 2 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 2 – Click Question Report Email Ids Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 3 — Click Add button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAddButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(ADD_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 3 ✔ Add button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 3 – Click Add Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 3 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 3 – Click Add Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 4 — Click Question Report card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickQuestionReportCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(QUESTION_REPORT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 4 ✔ Question Report card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 4 – Click Question Report Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 4 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 4 – Click Question Report Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 5 — Click Download Excel button (Question Report)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickDownloadExcelButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(DOWNLOAD_EXCEL_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 5 ✔ Download Excel button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 5 – Click Download Excel Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 5 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 5 – Click Download Excel Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 6 — Click User AI Feedback card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickUserAiFeedbackCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(USER_AI_FEEDBACK_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 6 ✔ User AI Feedback card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 6 – Click User AI Feedback Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 6 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 6 – Click User AI Feedback Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 7 — Click Assign Users List card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAssignUsersListCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(ASSIGN_USERS_LIST_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 7 ✔ Assign Users List card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 7 – Click Assign Users List Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 7 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 7 – Click Assign Users List Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 8 — Click Edit button (outlined default variant)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickEditButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(EDIT_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 8 ✔ Edit button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 8 – Click Edit Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 8 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 8 – Click Edit Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 9 — Click Update button (submit, primary solid)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickUpdateButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(UPDATE_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 9 ✔ Update button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 9 – Click Update Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 9 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 9 – Click Update Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 10 — Click Escalation Report card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickEscalationReportCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(ESCALATION_REPORT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 10 ✔ Escalation Report card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 10 – Click Escalation Report Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 10 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 10 – Click Escalation Report Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 11 — Click Edit button (circle variant — Escalation Report / Raise Query Dept)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickCircleEditButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(CIRCLE_EDIT_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 11 ✔ Circle Edit button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 11 – Click Circle Edit Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 11 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 11 – Click Circle Edit Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 12 — Click Ticket Assign card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickTicketAssignCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(TICKET_ASSIGN_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 12 ✔ Ticket Assign card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 12 – Click Ticket Assign Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 12 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 12 – Click Ticket Assign Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 13 — Click New button (blue border countdata)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickNewButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(NEW_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 13 ✔ New button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 13 – Click New Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 13 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 13 – Click New Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 14 — Click Close button (red border countdata)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickCloseButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(CLOSE_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 14 ✔ Close button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 14 – Click Close Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 14 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 14 – Click Close Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 15 — Click Reopen button (blue border countdata)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickReopenButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(REOPEN_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 15 ✔ Reopen button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 15 – Click Reopen Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 15 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 15 – Click Reopen Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 16 — Click In Progress button (orange border countdata)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickInProgressButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(IN_PROGRESS_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 16 ✔ In Progress button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 16 – Click In Progress Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 16 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 16 – Click In Progress Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 17 — Click Resolved button (green border countdata)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickResolvedButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(RESOLVED_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 17 ✔ Resolved button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 17 – Click Resolved Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 17 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 17 – Click Resolved Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 18 — Click Download Excel button (Ticket Assign — with download icon)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickTicketDownloadExcelButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(TICKET_DOWNLOAD_EXCEL_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 18 ✔ Ticket Download Excel button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 18 – Click Ticket Download Excel Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 18 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 18 – Click Ticket Download Excel Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 19 — Click Set Web URL card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickSetWebUrlCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(SET_WEB_URL_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 19 ✔ Set Web URL card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 19 – Click Set Web URL Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 19 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 19 – Click Set Web URL Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 20 — Click Add WEB URL button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAddWebUrlButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(ADD_WEB_URL_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 20 ✔ Add WEB URL button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 20 – Click Add WEB URL Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 20 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 20 – Click Add WEB URL Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 21 — Enter Web URL
    // ══════════════════════════════════════════════════════════════════════════
    public void enterWebUrl(String url) {
        try {
            WebElement input = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(WEB_URL_INPUT));
            input.clear();
            input.sendKeys(url);
            System.out.println("[HelpAndSupportPage] STEP 21 ✔ Web URL entered: " + url);
            ReportManager.logStep("HelpAndSupport", "STEP 21 – Enter Web URL: " + url, true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 21 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 21 – Enter Web URL", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 22 — Click Submit button
    // ══════════════════════════════════════════════════════════════════════════
    public void clickSubmitButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(SUBMIT_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 22 ✔ Submit button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 22 – Click Submit Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 22 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 22 – Click Submit Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 23 — Click Add Raise Query Department card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAddRaiseQueryDepartmentCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(ADD_RAISE_QUERY_DEPT_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 23 ✔ Add Raise Query Department card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 23 – Click Add Raise Query Department Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 23 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 23 – Click Add Raise Query Department Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 24 — Click Add Student Query Type card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAddStudentQueryTypeCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(ADD_STUDENT_QUERY_TYPE_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 24 ✔ Add Student Query Type card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 24 – Click Add Student Query Type Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 24 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 24 – Click Add Student Query Type Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 25 — Enter Student Query Type
    // ══════════════════════════════════════════════════════════════════════════
    public void enterStudentQueryType(String queryType) {
        try {
            WebElement input = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(STUDENT_QUERY_TYPE_INPUT));
            input.clear();
            input.sendKeys(queryType);
            System.out.println("[HelpAndSupportPage] STEP 25 ✔ Student Query Type entered: " + queryType);
            ReportManager.logStep("HelpAndSupport", "STEP 25 – Enter Student Query Type: " + queryType, true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 25 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 25 – Enter Student Query Type", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 26 — Click FAQ card
    // ══════════════════════════════════════════════════════════════════════════
    public void clickFaqCard() {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(FAQ_CARD));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            System.out.println("[HelpAndSupportPage] STEP 26 ✔ FAQ card clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 26 – Click FAQ Card", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 26 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 26 – Click FAQ Card", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 27 — Click Create Category link
    // ══════════════════════════════════════════════════════════════════════════
    public void clickCreateCategoryLink() {
        try {
            WebElement link = wait.until(
                    ExpectedConditions.elementToBeClickable(CREATE_CATEGORY_LINK));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
            System.out.println("[HelpAndSupportPage] STEP 27 ✔ Create Category link clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 27 – Click Create Category Link", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 27 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 27 – Click Create Category Link", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 28 — Click Add Category button (FAQ section)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickAddCategoryButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(ADD_CATEGORY_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 28 ✔ Add Category button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 28 – Click Add Category Button", true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 28 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 28 – Click Add Category Button", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 29 — Enter Category Name (FAQ section)
    // ══════════════════════════════════════════════════════════════════════════
    public void enterCategoryName(String name) {
        try {
            WebElement input = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(CATEGORY_NAME_INPUT));
            input.clear();
            input.sendKeys(name);
            System.out.println("[HelpAndSupportPage] STEP 29 ✔ Category name entered: " + name);
            ReportManager.logStep("HelpAndSupport", "STEP 29 – Enter Category Name: " + name, true);
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 29 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 29 – Enter Category Name", false);
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STEP 30 — Click Add button inside FAQ category form (submit, primary)
    // ══════════════════════════════════════════════════════════════════════════
    public void clickFaqAddButton() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.elementToBeClickable(FAQ_ADD_BTN));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("[HelpAndSupportPage] STEP 30 ✔ FAQ Add button clicked.");
            ReportManager.logStep("HelpAndSupport", "STEP 30 – Click FAQ Add Button", true);
            try { shortWait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_WRAP)); }
            catch (Exception ignored) { /* safe — modal may already be gone */ }
        } catch (Exception e) {
            System.err.println("[HelpAndSupportPage] STEP 30 ✘ " + e.getMessage());
            ReportManager.logStep("HelpAndSupport", "STEP 30 – Click FAQ Add Button", false);
            throw e;
        }
    }
}
