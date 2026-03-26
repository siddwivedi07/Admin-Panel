package com.dams.core;

// ══════════════════════════════════════════════════════════════════════════════
//  ConfigReader.java  —  DAMS Core Layer
//  ──────────────────────────────────────────────────────────────────────────
//  ✅ Kya karta hai?
//     config.properties file se saari values padhta hai.
//     Pura framework iska use karta hai — koi hardcoded value nahi.
//
//  ✅ Kyu zaruri hai?
//     Ek jagah change karo → poore framework mein reflect hoga.
//     DRY Principle: Don't Repeat Yourself
//
//  ✅ Kaise use karo?
//     String url = ConfigReader.get("url");
// ══════════════════════════════════════════════════════════════════════════════

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Singleton Properties object — ek baar load, baar baar use
    private static Properties properties;

    // Static block — class load hote hi config padh lo
    static {
        loadConfig();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Config file load karna
    // ──────────────────────────────────────────────────────────────────────────
    private static void loadConfig() {
        properties = new Properties();
        String configPath = "src/main/resources/config.properties";

        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            System.out.println("[CONFIG] ✔ config.properties loaded from: " + configPath);
        } catch (IOException e) {
            System.err.println("[CONFIG] ❌ Could not load config.properties: " + e.getMessage());
            throw new RuntimeException("Config file not found: " + configPath, e);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Public getter — key se value lo
    // ──────────────────────────────────────────────────────────────────────────
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("[CONFIG] ❌ Key not found in config.properties: " + key);
        }
        return value.trim();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Convenience methods — seedha naam se lo (readability ke liye)
    // ──────────────────────────────────────────────────────────────────────────
    public static String getUrl()           { return get("url"); }
    public static String getUsername()      { return get("username"); }
    public static String getPassword()      { return get("password"); }
    public static String getOtp()           { return get("otp"); }
    public static String getBrowser()       { return get("browser"); }
    public static boolean isHeadless()      { return Boolean.parseBoolean(get("headless")); }
    public static int getImplicitWait()     { return Integer.parseInt(get("implicit.wait")); }
    public static int getExplicitWait()     { return Integer.parseInt(get("explicit.wait")); }
    public static int getPageLoadTimeout()  { return Integer.parseInt(get("page.load.timeout")); }
    public static String getScreenshotPath(){ return get("screenshot.path"); }
    public static String getReportPath()    { return get("report.path"); }
    public static String getLogPath()       { return get("log.path"); }
    public static String getReportName()    { return get("report.name"); }
    public static String getReportTitle()   { return get("report.title"); }
}
