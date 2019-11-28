package com.delcache.backend.common.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static Map<String, String[]> actionNoLoginList = new HashMap<String, String[]>() {
        {
            put("/", new String[]{"login", "logout", "captcha", "404", "500", "error"});
        }
    };
    public static Map<String, String[]> actionWhiteList = new HashMap<String, String[]>() {
        {
            put("system/admin", new String[]{"'profile'", "change-password", "change-profile"});
        }
    };
}
