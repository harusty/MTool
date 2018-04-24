package com.zzkx.mtool.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by sshss on 2018/1/31.
 */

public class CachePathUtil {

    public static File getTmpImagePath(Context context) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/"  + context.getPackageName(), "/tmp_images/");
    }

    public static void clearTmpImages(Context context) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/"  + context.getPackageName(), "/tmp_images/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
                for (File subFile : files) {
                    if (subFile.exists())
                        subFile.delete();
                }
        }
    }
}
