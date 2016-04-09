package org.alexiwilius.ranti_app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AlexiWilius on 27.12.2014.
 */
public class Cache {

    private static SharedPreferences settings;

    private static String sharedPreferencesKey;

    public static void init(Context context, String sharedPreferencesKey) {
        Cache.sharedPreferencesKey = sharedPreferencesKey;
        settings = context.getSharedPreferences(sharedPreferencesKey, 0);
    }

    public static boolean exists(String key) {
        return getPreservedClassName(key) != null;
    }

    public static void set(String key, Object value) {
        SharedPreferences.Editor editor = settings.edit();
        Class keyClass;
        if (value instanceof String) {
            editor.putString(key, (String) value);
            keyClass = String.class;
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
            keyClass = Integer.class;
        } else if (value instanceof Float || value instanceof Double) {
            editor.putFloat(key, (Float) value);
            keyClass = Float.class;
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
            keyClass = Boolean.class;
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
            keyClass = Long.class;
        } else {
            editor.putString(key, String.valueOf(value));
            keyClass = String.class;
        }

        // Apply the edits!
        editor.apply();

        setPreservedClassName(key, keyClass);
    }

    public static Object get(String key) {
        return get(key, null);
    }

    public static Object get(String key, Object defaultValue) {
        String className = getPreservedClassName(key);

        if (className == null)
            return defaultValue;

        switch (className) {
            case "java.lang.String":
                String s = settings.getString(key, null);
                String text = s != null ? s.toString() : "";
                text = text.trim().equals("") ? null : text.trim();
                return text;
            case "java.lang.Integer":
                return settings.getInt(key, Integer.MIN_VALUE);
            case "java.lang.Float":
                return settings.getFloat(key, Float.MIN_VALUE);
            case "java.lang.Boolean":
                return settings.getBoolean(key, Boolean.FALSE);
            case "java.lang.Long":
                return settings.getLong(key, Long.MIN_VALUE);
            default:
                return defaultValue;
        }
    }

    private static void setPreservedClassName(String key, Class keyClass) {
        settings.edit().putString(getPreservedClassKey(key), keyClass.getName()).apply();
    }

    private static String getPreservedClassName(String key) {
        return settings.getString(getPreservedClassKey(key), null);
    }

    private static String getPreservedClassKey(String key) {
        return key + ".class";
    }
}
