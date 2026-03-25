package com.dams.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader
 * ─────────────────────────────────────────────────────────────────────────────
 * Singleton utility that loads src/main/resources/config.properties once and
 * exposes typed getters for every key the framework needs.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE = "config.properties";

    private static ConfigReader instance;
    private final Properties props = new Properties();

    // ── Constructor ──────────────────────────────────────────────────────────
    private ConfigReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on the classpath.");
            }
            props.load(is);
            log.info("config.properties loaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    // ── Singleton accessor ───────────────────────────────────────────────────
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    // ── Generic getter ───────────────────────────────────────────────────────
    public String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing property in config.properties: " + key);
        }
        return value.trim();
    }

    // ── Typed convenience getters ────────────────────────────────────────────
    public String getBrowser()          { return get("browser"); }
    public boolean isHeadless()         { return Boolean.parseBoolean(get("headless")); }
    public String getUrl()              { return get("url"); }
    public String getEmail()            { return get("email"); }
    public String getPassword()         { return get("password"); }
    public String getOtp()              { return get("otp"); }
    public int getImplicitWait()        { return Integer.parseInt(get("implicit.wait")); }
    public int getExplicitWait()        { return Integer.parseInt(get("explicit.wait")); }
    public int getPageLoadTimeout()     { return Integer.parseInt(get("page.load.timeout")); }
    public String getReportPath()       { return get("report.path"); }
    public String getScreenshotPath()   { return get("screenshot.path"); }
}
