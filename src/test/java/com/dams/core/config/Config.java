package com.dams.core.config;

public class Config {

    public static final String BASE_URL = "https://devadmin.damsdelhi.com/";

    public static final String DEFAULT_OTP = "1980";

    public static String getEmail() {
        String env = System.getenv("ADMIN_EMAIL");
        return (env != null && !env.isEmpty()) ? env : "ashutosh.mago@damsdelhi.com";
    }

    public static String getPassword() {
        String env = System.getenv("ADMIN_PASSWORD");
        return (env != null && !env.isEmpty()) ? env : "Ashutosh@123";
    }

    public static String getOtp() {
        String envOtp = System.getenv("ADMIN_LOGIN");
        return (envOtp != null && !envOtp.isEmpty())
                ? envOtp
                : DEFAULT_OTP;
    }
}
