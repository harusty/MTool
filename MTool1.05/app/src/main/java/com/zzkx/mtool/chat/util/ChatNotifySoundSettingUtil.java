package com.zzkx.mtool.chat.util;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;

import java.util.ArrayList;

/**
 * Created by sshss on 2018/1/12.
 */

public class ChatNotifySoundSettingUtil {
    public static void setSilentMode(boolean change) {
        SPUtil.putBoolean(Const.CHAT_SILENT_MODE, change);
    }

    public static boolean getSilentMode() {
        return SPUtil.getBoolean(Const.CHAT_SILENT_MODE, false);
    }
    public static void setShowMessageInfo(boolean change) {
        SPUtil.putBoolean(Const.SHOW_NOTIFY_INFO, change);
    }

    public static boolean getShowMessageInfo() {
        return SPUtil.getBoolean(Const.SHOW_NOTIFY_INFO, true);
    }
    public static void setSilentMode(String hxId, boolean change) {
        String json = SPUtil.getString(Const.SILENT_CACHE, "[]");
        ArrayList cacheList = Json_U.fromJson(json, ArrayList.class);
        if (change) {
            cacheList.remove(hxId);
            cacheList.add(hxId);
        } else {
            cacheList.remove(hxId);
        }
        SPUtil.putString(Const.SILENT_CACHE, Json_U.toJson(cacheList));
    }

    public static boolean getSilentMode(String hxId) {
        String json = SPUtil.getString(Const.SILENT_CACHE, "[]");
        ArrayList cacheList = Json_U.fromJson(json, ArrayList.class);
        return cacheList.contains(hxId);
    }


}
