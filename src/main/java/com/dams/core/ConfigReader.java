package com.dams.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration values from config.properties file.
 * Singleton pattern ensures only one instance is created.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties;

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Returns the singleton instance of ConfigReader.
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Loads properties from the config file.
     */
    private void loadProperties() {
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
            log.info("Configuration loaded successfully from: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("Failed to load configuration file: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load config.properties: " + e.getMessage());
        }
    }

    public String getAppUrl()           { return getProperty("app.url"); }
    public String getEmail()            { return getProperty("app.email"); }
    public String getPassword()         { return getProperty("app.password"); }
    public String getBrowser()          { return getProperty("browser"); }
    public boolean isHeadless()         { return Boolean.parseBoolean(getProperty("headless")); }
    public int getImplicitWait()        { return Integer.parseInt(getProperty("implicit.wait")); }
    public int getExplicitWait()        { return Integer.parseInt(getProperty("explicit.wait")); }
    public int getPageLoadTimeout()     { return Integer.parseInt(getProperty("page.load.timeout")); }
    public boolean isScreenshotOnFail() { return Boolean.parseBoolean(getProperty("screenshot.on.failure")); }
    public String getScreenshotDir()    { return getProperty("screenshot.dir"); }
    public String getReportDir()        { return getProperty("report.dir"); }
    public String getReportName()       { return getProperty("report.name"); }

    /**
     * Generic property getter with null-check.
     */
    private String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config.properties", key);
        }
        return value;
    }
}

