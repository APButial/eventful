package com.btp.appfx.service;

import java.util.prefs.Preferences;

public class AppDataPath {
    private static final String KEY = "app_data_path";
    private static final Preferences prefs = Preferences.userNodeForPackage(AppDataPath.class);

    public static void savePath(String path) {
        prefs.put(KEY, path);
    }

    public static String loadPath() {
        return prefs.get(KEY, null);
    }

    public static void clear() {
        prefs.remove(KEY);
    }
}
