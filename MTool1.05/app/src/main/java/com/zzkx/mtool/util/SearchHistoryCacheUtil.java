package com.zzkx.mtool.util;

import com.zzkx.mtool.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/9/18.
 */

public class SearchHistoryCacheUtil {
    private static final String HIS_CACHE = "search_history.obj";
    private static HashMap<Integer, List<String>> sCache;

    public static List<String> getHistory(int searchType) {

        if (sCache == null)
            sCache = getCache();

        if (sCache != null) {
            return sCache.get(searchType);
        } else {
            return null;
        }
    }


    public static HashMap<Integer, List<String>> getCache() {
        HashMap<Integer, List<String>> map = null;
        try {
            File file = new File(MyApplication.getContext().getCacheDir(), HIS_CACHE);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            map = (HashMap) in.readObject();
            in.close();
        } catch (Exception e) {
            handleException(e);
        }
        return map;
    }

    private static void handleException(Exception e) {
        File file = new File(MyApplication.getContext().getCacheDir(), HIS_CACHE);
        if (file.exists()) {
            file.delete();
        }
        e.printStackTrace();
    }

    public static void putKeyword(String keyword, int searchType) {
        List<String> history = getHistory(searchType);
        if (sCache == null) {
            sCache = getCache();
        }
        if (sCache == null)
            sCache = new HashMap<>();
        if (history == null) {
            history = new ArrayList<>();
            history.add(keyword);
            sCache.put(searchType, history);
            cacheHistory(searchType);
        } else {
            if (!history.contains(keyword)) {
                history.add(keyword);
                sCache.put(searchType, history);
                cacheHistory(searchType);
            }
        }
    }

    private static void cacheHistory(int searchType) {
        File file = new File(MyApplication.getContext().getCacheDir().getAbsolutePath(), HIS_CACHE);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(sCache);
            out.close();
        } catch (Exception e) {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            handleException(e);
        }
    }

    public static void clear(int searchType) {
        if (sCache == null) {
            sCache = getCache();
        }
        if (sCache != null) {
            List<String> strings = sCache.get(searchType);
            if (strings != null && strings.size() > 0) {
                sCache.remove(searchType);
                cacheHistory(searchType);
            }
        }
    }
}
