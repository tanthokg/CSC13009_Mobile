package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig {
    private SharedPreferences sharedPreferences;
    private static AppConfig instance;
    private static final String DARK_MODE = "dark_mode";
    private static final String TRASH_MODE = "trash_mode";

    public static AppConfig getInstance(Context context) {
        if (null == instance)
            instance = new AppConfig(context);
        return instance;
    }

    private AppConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE);
    }

   /* private void initConfig() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DARK_MODE, false);
        editor.putBoolean(TRASH_MODE, false);
        editor.apply();
    }*/

    public boolean getDarkMode() {
        return sharedPreferences.getBoolean(DARK_MODE, false);
    }

    public boolean getTrashMode() {
        return sharedPreferences.getBoolean(TRASH_MODE, false);
    }

    public void setDarkMode(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(DARK_MODE);
        editor.putBoolean(DARK_MODE, value);
        editor.apply();
    }

    public void setTrashMode(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TRASH_MODE);
        editor.putBoolean(TRASH_MODE, value);
        editor.apply();
    }
}
