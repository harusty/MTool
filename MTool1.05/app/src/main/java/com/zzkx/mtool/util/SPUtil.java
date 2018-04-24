package com.zzkx.mtool.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zzkx.mtool.MyApplication;

public class SPUtil {
    private static SharedPreferences sp;

    private static Context getContext() {
        return MyApplication.getContext();
    }

    //L.K.X start
    public static void putInt(String key, int value) {
        SharedPreferences.Editor edit = initSp();
        edit.putInt(key, value).commit();
    }

    public static int getInt(String key, int defaultValue) {
        if (sp == null) {
            sp = getContext().getSharedPreferences("lkxcatch", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        if (sp == null) {
            sp = getContext().getSharedPreferences("lkxcatch", Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defaultValue);
    }

    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor edit = initSp();
        edit.putBoolean(key, value).commit();
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        if (sp == null) {
            sp = getContext().getSharedPreferences("lkxcatch", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = initSp();
        edit.putString(key, value).commit();
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor edit = initSp();
        edit.putLong(key, value).commit();
    }

    private static SharedPreferences.Editor initSp() {
        if (sp == null) {
            sp = getContext().getSharedPreferences("lkxcatch", Context.MODE_PRIVATE);
        }
        return sp.edit();
    }

    public static String getString(String key, String defaultValue) {
        if (sp == null) {
            sp = getContext().getSharedPreferences("lkxcatch", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defaultValue);
    }

    public static void clear() {
        SharedPreferences.Editor edit = initSp();
        edit.clear();
        edit.commit();
    }


}
