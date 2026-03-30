package com.dams.report;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportManager {
    public static class Step {
        public String tc;
        public String phase;
        public String stepName;
        public String status;
        public String screenshot;

        public Step(String tc, String phase, String stepName, String status, String screenshot) {
            this.tc = tc;
            this.phase = phase;
            this.stepName = stepName;
            this.status = status;
            this.screenshot = screenshot;
        }
    }

    public static class TestResultData {
        public String testName;
        public long startTime;
        public long endTime;
        public String status;
        public List<Step> steps = new ArrayList<>();
    }

    private static Map<String, TestResultData> testResults = new LinkedHashMap<>();
    private static TestResultData currentTest;
    private static int stepCounter = 1;

    private static final String reportFile =
            "DAMS_Selenium_Report_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date()) + ".html";
    public static void startTest(String testName) {
        currentTest = new TestResultData();
        currentTest.testName = testName;
        currentTest.startTime = System.currentTimeMillis();
        stepCounter = 1;
        testResults.put(testName, currentTest);
    }
    public static void endTest(String status) {
        currentTest.status = status;
        currentTest.endTime = System.currentTimeMillis();
    }
    public static void logStep(String phase, String stepName, boolean pass, String screenshotPath) {
        String tcId = "TC_" + stepCounter++;
        Step step = new Step(tcId, phase, stepName, pass ? "PASS" : "FAIL", screenshotPath);
        currentTest.steps.add(step);
    }

    public static void logStep(String phase, String stepName, boolean pass) {
        logStep(phase, stepName, pass, null);
    }
    /** Entry point for: mvn exec:java@run-report */
    public static void main(String[] args) {
        flush();
    }

    public static void flush() {
        try {
            new java.io.File("artifacts").mkdirs();
            int total = testResults.size();
            int passed = 0;
            int failed = 0;
            for (TestResultData t : testResults.values()) {
                if ("PASS".equals(t.status)) passed++;
                else failed++;
            }

            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html><html data-theme='light'><head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1'>")
                .append("<title>DAMS Selenium Report</title>")
                .append("<link href='https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&display=swap' rel='stylesheet'>")

                .append("<style>")
                .append("*{margin:0;padding:0;box-sizing:border-box}")

                /* ── LIGHT THEME VARIABLES ── */
                .append(":root,[data-theme='light']{")
                .append("--bg:#f4f6fb;")
                .append("--sidebar-bg:#ffffff;")
                .append("--sidebar-border:#e2e8f0;")
                .append("--card-bg:#ffffff;")
                .append("--text-primary:#0f172a;")
                .append("--text-muted:#64748b;")
                .append("--nav-hover-bg:#f1f5f9;")
                .append("--nav-hover-color:#2563eb;")
                .append("--divider:#e2e8f0;")
                .append("--table-hover:#f8fafc;")
                .append("--table-border:#e2e8f0;")
                .append("--th-bg:#2f60d1;")
                .append("--toggle-bg:#e2e8f0;")
                .append("--toggle-knob:#ffffff;")
                .append("--toggle-icon-sun:block;")
                .append("--toggle-icon-moon:none;")
                .append("}")

                /* ── DARK THEME VARIABLES ── */
                .append("[data-theme='dark']{")
                .append("--bg:#0d1117;")
                .append("--sidebar-bg:#161b22;")
                .append("--sidebar-border:#30363d;")
                .append("--card-bg:#161b22;")
                .append("--text-primary:#e6edf3;")
                .append("--text-muted:#8b949e;")
                .append("--nav-hover-bg:#21262d;")
                .append("--nav-hover-color:#58a6ff;")
                .append("--divider:#30363d;")
                .append("--table-hover:#1c2128;")
                .append("--table-border:#30363d;")
                .append("--th-bg:#1f3a6e;")
                .append("--toggle-bg:#58a6ff;")
                .append("--toggle-knob:#0d1117;")
                .append("--toggle-icon-sun:none;")
                .append("--toggle-icon-moon:block;")
                .append("}")

                .append("body{font-family:'DM Sans',sans-serif;background:var(--bg);color:var(--text-primary);transition:background .3s,color .3s}")

                /* SIDEBAR */
                .append(".sidebar{position:fixed;left:0;top:0;width:260px;height:100vh;background:var(--sidebar-bg);border-right:1px solid var(--sidebar-border);padding:30px 22px;overflow-y:auto;transition:background .3s,border-color .3s}")
                .append(".sidebar h2{color:#2563eb;font-size:22px;font-weight:700;margin-bottom:25px;letter-spacing:1px}")
                .append("[data-theme='dark'] .sidebar h2{color:#58a6ff}")
                .append(".divider{height:1px;background:var(--divider);margin:15px 0}")
                .append(".section-title{color:var(--text-muted);font-size:12px;letter-spacing:3px;margin-bottom:15px;font-weight:700}")
                .append(".nav-link{display:flex;align-items:center;gap:10px;padding:10px 12px;border-radius:8px;margin-bottom:8px;font-size:14px;text-decoration:none;color:var(--text-primary);transition:all .2s ease}")
                .append(".nav-link:hover{background:var(--nav-hover-bg);color:var(--nav-hover-color)}")

                .append(".dot{width:8px;height:8px;border-radius:50%}")
                .append(".dot-pass{background:#22c55e}")
                .append(".dot-fail{background:#ef4444}")
                .append(".dot-neutral{background:#3b82f6}")

                /* DARK THEME TOGGLE */
                .append(".theme-toggle{display:flex;align-items:center;gap:10px;margin-top:20px;cursor:pointer;padding:10px 12px;border-radius:8px;transition:background .2s}")
                .append(".theme-toggle:hover{background:var(--nav-hover-bg)}")
                .append(".toggle-track{width:44px;height:24px;background:var(--toggle-bg);border-radius:12px;position:relative;transition:background .3s;flex-shrink:0}")
                .append(".toggle-knob{position:absolute;top:3px;left:3px;width:18px;height:18px;background:var(--toggle-knob);border-radius:50%;transition:transform .3s,background .3s}")
                .append("[data-theme='dark'] .toggle-knob{transform:translateX(20px)}")
                .append(".toggle-label{font-size:13px;color:var(--text-muted);font-weight:500;user-select:none}")
                .append(".icon-sun{display:var(--toggle-icon-sun)}")
                .append(".icon-moon{display:var(--toggle-icon-moon)}")

                /* MAIN */
                .append(".main{margin-left:280px;padding:60px 70px}")

                /* TOP */
                .append(".top-bar{display:flex;justify-content:space-between;align-items:center;margin-bottom:40px}")
                .append(".top-bar h1{font-size:32px;font-weight:700}")
                .append(".logo{height:55px}")

                /* STATS */
                .append(".stats{display:flex;gap:30px;margin-bottom:40px}")
                .append(".stat{background:var(--card-bg);padding:25px 40px;border-radius:14px;min-width:200px;text-align:center;box-shadow:0 6px 20px rgba(0,0,0,0.05);transition:background .3s}")
                .append("[data-theme='dark'] .stat{box-shadow:0 6px 20px rgba(0,0,0,0.3)}")
                .append(".stat h3{font-size:13px;text-transform:uppercase;color:var(--text-muted);letter-spacing:1px}")
                .append(".stat p{font-size:30px;font-weight:700;margin-top:8px}")

                /* CARD */
                .append(".card{background:var(--card-bg);border-radius:16px;padding:30px;margin-bottom:40px;box-shadow:0 8px 25px rgba(0,0,0,0.05);transition:background .3s}")
                .append("[data-theme='dark'] .card{box-shadow:0 8px 25px rgba(0,0,0,0.4)}")
                .append(".card h2{font-size:18px;margin-bottom:20px}")

                /* TABLE */
                .append("table{width:100%;border-collapse:collapse}")
                .append("th{background:var(--th-bg);color:white;font-size:12px;text-transform:uppercase;padding:12px;transition:background .3s}")
                .append("td{padding:12px;border-bottom:1px solid var(--table-border);transition:border-color .3s}")
                .append("tr:hover{background:var(--table-hover)}")

                /* BADGE */
                .append(".pass{background:#dcfce7;color:#16a34a;padding:5px 12px;border-radius:20px;font-size:12px;font-weight:600;display:inline-block}")
                .append(".fail{background:#fee2e2;color:#dc2626;padding:5px 12px;border-radius:20px;font-size:12px;font-weight:600;display:inline-block}")
                .append("[data-theme='dark'] .pass{background:#14532d;color:#4ade80}")
                .append("[data-theme='dark'] .fail{background:#450a0a;color:#f87171}")

                /* BUTTON */
                .append(".view-btn{background:#2f60d1;color:white;padding:5px 12px;border-radius:6px;font-size:12px;text-decoration:none}")
                .append(".view-btn:hover{background:#1d4ed8}")
                .append("[data-theme='dark'] .view-btn{background:#1f3a6e;color:#93c5fd}")
                .append("[data-theme='dark'] .view-btn:hover{background:#2563eb;color:#ffffff}")

                .append(".footer{text-align:center;margin-top:60px;padding:20px;font-size:13px;color:var(--text-muted)}")
                .append("</style></head><body>");

            /* SIDEBAR */
            html.append("<div class='sidebar'>")
                .append("<h2>DAMS</h2>")
                .append("<div class='divider'></div>")
                .append("<div class='section-title'>MODULES</div>");
            for (TestResultData test : testResults.values()) {
                String anchor = test.testName.replaceAll("[^a-zA-Z0-9]", "-");
                String dotClass = "PASS".equals(test.status)
                        ? "dot-pass"
                        : "FAIL".equals(test.status)
                        ? "dot-fail"
                        : "dot-neutral";
                html.append("<a class='nav-link' href='#")
                        .append(anchor)
                        .append("'>")
                        .append("<span class='dot ")
                        .append(dotClass)
                        .append("'></span>")
                        .append(test.testName)
                        .append("</a>");
            }

            /* DARK MODE TOGGLE inside sidebar */
            html.append("<div class='divider'></div>")
                .append("<div class='theme-toggle' onclick='toggleTheme()' title='Toggle Dark Mode'>")
                .append("<div class='toggle-track'><div class='toggle-knob'></div></div>")
                .append("<span class='toggle-label'>")
                .append("<span class='icon-sun'>☀️ Light</span>")
                .append("<span class='icon-moon'>🌙 Dark</span>")
                .append("</span>")
                .append("</div>")
                .append("</div>");

            /* MAIN */
            html.append("<div class='main'>")
                .append("<div class='top-bar'>")
                .append("<h1>DAMS Admin Selenium Report</h1>")
                .append("<img src='https://cdn.damsdelhi.com/react_emed_web/dams-icon/logo.png' class='logo'/>")
                .append("</div>");

            html.append("<div class='stats'>")
                .append("<div class='stat'><h3>Total Tests</h3><p>").append(total).append("</p></div>")
                .append("<div class='stat'><h3>Passed</h3><p style='color:#16a34a'>").append(passed).append("</p></div>")
                .append("<div class='stat'><h3>Failed</h3><p style='color:#dc2626'>").append(failed).append("</p></div>")
                .append("</div>");

            for (TestResultData test : testResults.values()) {
                long duration = (test.endTime - test.startTime) / 1000;
                String anchor = test.testName.replaceAll("[^a-zA-Z0-9]", "-");
                html.append("<div class='card' id='").append(anchor).append("'>")
                        .append("<h2>")
                        .append(test.testName)
                        .append(" (").append(duration).append("s)")
                        .append(" <span class='")
                        .append("PASS".equals(test.status) ? "pass'>PASS" : "fail'>FAIL")
                        .append("</span></h2>");
                html.append("<table>")
                        .append("<tr><th>TC</th><th>Phase</th><th>Step</th><th>Status</th><th>Screenshot</th></tr>");

                for (Step s : test.steps) {

                    html.append("<tr>")
                            .append("<td>").append(s.tc).append("</td>")
                            .append("<td>").append(s.phase).append("</td>")
                            .append("<td>").append(s.stepName).append("</td>")
                            .append("<td><span class='")
                            .append("PASS".equals(s.status) ? "pass'>" : "fail'>")
                            .append(s.status)
                            .append("</span></td>")
                            .append("<td>");

                    if (s.screenshot != null && !s.screenshot.trim().isEmpty()) {
                        html.append("<a class='view-btn' href='")
                                .append(s.screenshot)
                                .append("' target='_blank'>View</a>");
                    } else {
                        html.append("-");
                    }
                    html.append("</td></tr>");
                }
                html.append("</table></div>");
            }
            html.append("<div class='footer'>Created by Ashutosh Mago – Junior AWS DevOps Engineer</div>")
                .append("</div>");

            /* DARK MODE SCRIPT */
            html.append("<script>")
                .append("(function(){")
                .append("var saved=localStorage.getItem('dams-theme');")
                .append("if(saved){document.documentElement.setAttribute('data-theme',saved);}")
                .append("})()")
                .append("</script>")
                .append("<script>")
                .append("function toggleTheme(){")
                .append("var html=document.documentElement;")
                .append("var current=html.getAttribute('data-theme');")
                .append("var next=current==='dark'?'light':'dark';")
                .append("html.setAttribute('data-theme',next);")
                .append("localStorage.setItem('dams-theme',next);")
                .append("}")
                .append("</script>")
                .append("</body></html>");

            FileWriter writer = new FileWriter(reportFile);
            writer.write(html.toString());
            writer.close();
            System.out.println("Final Suite Report Created: " + reportFile);
        } catch (Exception e) {
            System.out.println("Report generation failed: " + e.getMessage());
        }
    }
}
