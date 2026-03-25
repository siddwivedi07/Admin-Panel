package com.dams.report;

import com.dams.core.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * ReportManager
 * ─────────────────────────────────────────────────────────────────────────────
 * Generates a custom HTML report matching the DAMS Admin Selenium Report design:
 *
 *   LEFT SIDEBAR  : DAMS logo | module list with green/red dot | Light toggle
 *   SUMMARY CARDS : Total Tests | Passed | Failed
 *   TEST BLOCK    : testName (Xs) PASS/FAIL badge
 *   TABLE COLUMNS : TC | PHASE | STEP | STATUS | SCREENSHOT
 *   ROWS          : TC_1…TC_5 (login steps) + TC_6 (overall execution with View btn)
 */
public class ReportManager {

    private static final Logger log = LogManager.getLogger(ReportManager.class);
    private static ReportManager instance;

    // ── Row model ─────────────────────────────────────────────────────────────
    public static class StepRow {
        public final String tc;
        public final String phase;
        public final String step;
        public final boolean passed;
        public final String screenshotB64;

        public StepRow(String tc, String phase, String step, boolean passed, byte[] screenshot) {
            this.tc = tc;
            this.phase = phase;
            this.step = step;
            this.passed = passed;
            this.screenshotB64 = (screenshot != null)
                    ? Base64.getEncoder().encodeToString(screenshot) : null;
        }
    }

    // ── State ─────────────────────────────────────────────────────────────────
    private final List<StepRow> rows = new ArrayList<>();
    private String currentTestName   = "adminLoginTest";
    private long   testStartMs       = System.currentTimeMillis();
    private boolean overallPassed    = true;

    // ── Singleton ─────────────────────────────────────────────────────────────
    private ReportManager() {}
    public static synchronized ReportManager getInstance() {
        if (instance == null) instance = new ReportManager();
        return instance;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void init() {
        rows.clear();
        overallPassed = true;
        testStartMs   = System.currentTimeMillis();
        log.info("ReportManager initialised.");
    }

    public void startTest(String testName) {
        this.currentTestName = testName;
        this.testStartMs     = System.currentTimeMillis();
        log.info("Test started: {}", testName);
    }

    /** Log an individual step row (TC_1 … TC_5). No screenshot = dash. */
    public void logStep(String tc, String phase, String step, boolean passed) {
        logStep(tc, phase, step, passed, null);
    }

    public void logStep(String tc, String phase, String step, boolean passed, byte[] screenshot) {
        if (!passed) overallPassed = false;
        rows.add(new StepRow(tc, phase, step, passed, screenshot));
        log.info("[{}] {} | {} | {}", passed ? "PASS" : "FAIL", tc, phase, step);
    }

    /**
     * Add the TC_6 "Execution" summary row (always has a screenshot / View button).
     */
    public void endTest(boolean passed, byte[] screenshot) {
        if (!passed) overallPassed = false;
        rows.add(new StepRow("TC_6", "Execution", currentTestName, passed, screenshot));
        log.info("Test ended: {} → {}", currentTestName, passed ? "PASS" : "FAIL");
    }

    public void endTest(boolean passed) {
        endTest(passed, null);
    }

    /** Write the HTML report to disk. Call once after all tests. */
    public void flush() {
        String path = ConfigReader.getInstance().getReportPath();
        ensureDir(path);
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(buildHtml());
            log.info("DAMS HTML report written → {}", path);
        } catch (IOException e) {
            log.error("Failed to write report: {}", e.getMessage(), e);
        }
    }

    // ── HTML generation ───────────────────────────────────────────────────────

    private String buildHtml() {
        long elapsedSec = (System.currentTimeMillis() - testStartMs) / 1000;
        int passed = overallPassed ? 1 : 0;
        int failed = overallPassed ? 0 : 1;
        String passBadge = overallPassed ? "pass" : "fail";
        String passText  = overallPassed ? "PASS" : "FAIL";
        String dotClass  = overallPassed ? "dot" : "dot fail";

        // Build modal HTML for screenshots
        StringBuilder modals = new StringBuilder();
        int mi = 0;
        for (StepRow r : rows) {
            if (r.screenshotB64 != null) {
                modals.append("<div class=\"modal-overlay\" id=\"modal").append(mi)
                      .append("\" onclick=\"closeModal(this)\">\n")
                      .append("  <div class=\"modal-box\">\n")
                      .append("    <button class=\"modal-close\" ")
                      .append("onclick=\"closeModal(document.getElementById('modal").append(mi).append("'))\">✕</button>\n")
                      .append("    <img class=\"modal-img\" src=\"data:image/png;base64,")
                      .append(r.screenshotB64).append("\" alt=\"screenshot\"/>\n")
                      .append("  </div>\n</div>\n");
            }
            mi++;
        }

        // Build table rows
        StringBuilder tableRows = new StringBuilder();
        int ri = 0;
        for (StepRow r : rows) {
            String badge = r.passed ? "pass" : "fail";
            String label = r.passed ? "PASS" : "FAIL";
            String screenshotCell;
            if (r.screenshotB64 != null) {
                screenshotCell = "<button class=\"view-btn\" onclick=\"openModal('modal" + ri + "')\">View</button>";
            } else {
                screenshotCell = "<span style=\"color:var(--muted)\">–</span>";
            }
            tableRows.append("<tr>")
                     .append("<td>").append(esc(r.tc)).append("</td>")
                     .append("<td>").append(esc(r.phase)).append("</td>")
                     .append("<td>").append(esc(r.step)).append("</td>")
                     .append("<td><span class=\"step-badge ").append(badge).append("\">").append(label).append("</span></td>")
                     .append("<td>").append(screenshotCell).append("</td>")
                     .append("</tr>\n");
            ri++;
        }

        return "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "<head>\n"
            + "<meta charset=\"UTF-8\"/>\n"
            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/>\n"
            + "<title>DAMS Admin Selenium Report</title>\n"
            + "<style>\n"
            + css()
            + "</style>\n"
            + "</head>\n"
            + "<body id=\"body\">\n\n"
            // Sidebar
            + "<aside class=\"sidebar\">\n"
            + "  <div class=\"sidebar-logo\">DAMS</div>\n"
            + "  <div class=\"sidebar-section\">\n"
            + "    <div class=\"sidebar-label\">MODULES</div>\n"
            + "    <div class=\"sidebar-module\">\n"
            + "      <span class=\"" + dotClass + "\"></span>\n"
            + "      <span>" + esc(currentTestName) + "</span>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "  <div class=\"toggle-row\">\n"
            + "    <label class=\"toggle\">\n"
            + "      <input type=\"checkbox\" id=\"themeToggle\" onchange=\"toggleTheme()\"/>\n"
            + "      <span class=\"slider\"></span>\n"
            + "    </label>\n"
            + "    <span class=\"toggle-label\">Light</span>\n"
            + "  </div>\n"
            + "</aside>\n\n"
            // Main
            + "<main class=\"main\">\n"
            + "  <div class=\"main-header\">\n"
            + "    <h1 class=\"main-title\">DAMS Admin Selenium Report</h1>\n"
            + "    <div class=\"brand-logo\"><span class=\"brand-dams\">DAMS</span><span class=\"brand-alpha\">α</span></div>\n"
            + "  </div>\n\n"
            // Summary cards
            + "  <div class=\"cards\">\n"
            + "    <div class=\"card\"><div class=\"card-label\">TOTAL TESTS</div><div class=\"card-val\">1</div></div>\n"
            + "    <div class=\"card\"><div class=\"card-label\">PASSED</div><div class=\"card-val green\">" + passed + "</div></div>\n"
            + "    <div class=\"card\"><div class=\"card-label\">FAILED</div><div class=\"card-val red\">" + failed + "</div></div>\n"
            + "  </div>\n\n"
            // Test block
            + "  <div class=\"test-block\">\n"
            + "    <div class=\"test-block-hdr\">\n"
            + "      <span class=\"test-name\">" + esc(currentTestName)
            + " <span class=\"elapsed\">(" + elapsedSec + "s)</span></span>\n"
            + "      <span class=\"badge " + passBadge + "\">" + passText + "</span>\n"
            + "    </div>\n"
            + "    <table>\n"
            + "      <thead><tr>\n"
            + "        <th>TC</th><th>PHASE</th><th>STEP</th><th>STATUS</th><th>SCREENSHOT</th>\n"
            + "      </tr></thead>\n"
            + "      <tbody>\n"
            + tableRows
            + "      </tbody>\n"
            + "    </table>\n"
            + "  </div>\n\n"
            + "  <div class=\"footer\">Created by Siddharth Wivedi – DAMS Selenium Automation</div>\n"
            + "</main>\n\n"
            + modals
            + "<script>\n"
            + "function toggleTheme(){document.getElementById('body').classList.toggle('dark');}\n"
            + "function openModal(id){document.getElementById(id).classList.add('active');}\n"
            + "function closeModal(el){if(el.classList.contains('modal-overlay'))el.classList.remove('active');}\n"
            + "</script>\n"
            + "</body>\n</html>";
    }

    private String css() {
        return """
          *,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
          :root{
            --bg:#f0f2f6;--sidebar:#fff;--card:#fff;--text:#111827;
            --muted:#6b7280;--border:#e5e7eb;
            --blue:#2563eb;--blue2:#1d4ed8;
            --green:#16a34a;--green-bg:#dcfce7;
            --red:#dc2626;--red-bg:#fee2e2;
            --th-bg:#2563eb;--th-text:#fff;
          }
          body.dark{
            --bg:#0f172a;--sidebar:#1e293b;--card:#1e293b;--text:#f1f5f9;
            --muted:#94a3b8;--border:#334155;--th-bg:#1d4ed8;
          }
          body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;
               background:var(--bg);color:var(--text);display:flex;min-height:100vh;}

          /* Sidebar */
          .sidebar{width:220px;min-width:220px;background:var(--sidebar);
                   border-right:1px solid var(--border);padding:28px 20px;
                   display:flex;flex-direction:column;gap:20px;}
          .sidebar-logo{font-size:22px;font-weight:800;color:var(--blue);letter-spacing:1px;}
          .sidebar-section{display:flex;flex-direction:column;gap:10px;}
          .sidebar-label{font-size:10px;font-weight:700;letter-spacing:1.5px;
                         text-transform:uppercase;color:var(--muted);}
          .sidebar-module{display:flex;align-items:center;gap:8px;font-size:13px;font-weight:500;}
          .dot{width:8px;height:8px;border-radius:50%;background:#22c55e;flex-shrink:0;}
          .dot.fail{background:#ef4444;}
          .toggle-row{display:flex;align-items:center;gap:10px;margin-top:auto;}
          .toggle-label{font-size:13px;color:var(--muted);}
          .toggle{position:relative;width:40px;height:22px;cursor:pointer;}
          .toggle input{opacity:0;width:0;height:0;}
          .slider{position:absolute;inset:0;background:#cbd5e1;border-radius:22px;transition:background .3s;}
          .slider::before{content:'';position:absolute;width:16px;height:16px;left:3px;top:3px;
                          background:#fff;border-radius:50%;transition:transform .3s;}
          .toggle input:checked+.slider{background:var(--blue);}
          .toggle input:checked+.slider::before{transform:translateX(18px);}

          /* Main */
          .main{flex:1;padding:40px 48px;overflow-x:auto;}
          .main-header{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:32px;}
          .main-title{font-size:28px;font-weight:700;}
          .brand-logo{display:flex;flex-direction:column;align-items:center;font-weight:800;font-size:13px;letter-spacing:1px;}
          .brand-alpha{font-size:30px;font-style:italic;line-height:1;}

          /* Cards */
          .cards{display:flex;gap:20px;margin-bottom:32px;}
          .card{background:var(--card);border:1px solid var(--border);border-radius:12px;
                padding:24px 40px;text-align:center;
                box-shadow:0 1px 3px rgba(0,0,0,.06);}
          .card-label{font-size:11px;font-weight:600;letter-spacing:1.2px;
                      text-transform:uppercase;color:var(--muted);margin-bottom:8px;}
          .card-val{font-size:36px;font-weight:700;}
          .card-val.green{color:var(--green);}
          .card-val.red{color:var(--red);}

          /* Test block */
          .test-block{background:var(--card);border:1px solid var(--border);
                      border-radius:12px;overflow:hidden;box-shadow:0 1px 3px rgba(0,0,0,.06);}
          .test-block-hdr{padding:18px 24px;display:flex;align-items:center;gap:12px;
                          border-bottom:1px solid var(--border);}
          .test-name{font-size:16px;font-weight:600;}
          .elapsed{color:var(--muted);font-size:14px;}
          .badge{padding:3px 12px;border-radius:20px;font-size:11px;font-weight:700;
                 letter-spacing:.5px;text-transform:uppercase;}
          .badge.pass{background:var(--green-bg);color:var(--green);}
          .badge.fail{background:var(--red-bg);color:var(--red);}

          /* Table */
          table{width:100%;border-collapse:collapse;}
          thead tr{background:var(--th-bg);}
          thead th{color:var(--th-text);font-size:12px;font-weight:600;letter-spacing:1px;
                   text-transform:uppercase;padding:14px 20px;text-align:left;}
          tbody tr{border-bottom:1px solid var(--border);}
          tbody tr:last-child{border-bottom:none;}
          tbody tr:hover{background:rgba(37,99,235,.04);}
          tbody td{padding:16px 20px;font-size:14px;vertical-align:middle;}
          .step-badge{display:inline-block;padding:3px 12px;border-radius:20px;
                      font-size:11px;font-weight:700;letter-spacing:.5px;text-transform:uppercase;}
          .step-badge.pass{background:var(--green-bg);color:var(--green);}
          .step-badge.fail{background:var(--red-bg);color:var(--red);}
          .view-btn{padding:6px 16px;background:var(--blue);color:#fff;border:none;
                    border-radius:6px;font-size:12px;font-weight:600;cursor:pointer;}
          .view-btn:hover{background:var(--blue2);}

          /* Footer */
          .footer{margin-top:40px;text-align:center;font-size:12px;color:var(--muted);}

          /* Modal */
          .modal-overlay{display:none;position:fixed;inset:0;background:rgba(0,0,0,.6);
                         z-index:1000;align-items:center;justify-content:center;}
          .modal-overlay.active{display:flex;}
          .modal-box{background:var(--card);border-radius:12px;padding:16px;
                     max-width:90vw;max-height:90vh;overflow:auto;position:relative;}
          .modal-close{position:absolute;top:8px;right:12px;background:none;border:none;
                       font-size:22px;cursor:pointer;color:var(--text);}
          .modal-img{max-width:100%;display:block;border-radius:8px;margin-top:24px;}
        """;
    }
 
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private void ensureDir(String filePath) {
        try {
            Path p = Paths.get(filePath).getParent();
            if (p != null) Files.createDirectories(p);
        } catch (IOException e) {
            log.warn("Could not create dir: {}", e.getMessage());
        }
    }
}
