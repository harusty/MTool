package com.zzkx.mtool.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.zzkx.mtool.bean.ImageFloder;
import com.zzkx.mtool.bean.LocalImageBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sshss on 2018/1/19.
 */

public class LocalImageFetcher {
    private static LocalImageFetcher sInstance;
    private HashSet<String> mDirPaths;
    private List<ImageFloder> mImageFloders;
    private List<LocalImageBean> mAllImgPathes;
    private Handler mHandler;
    private Thread mThread;

    private LocalImageFetcher() {
        mDirPaths = new HashSet<String>();
        mImageFloders = new ArrayList<>();
        mAllImgPathes = new ArrayList<>();
        mHandler = new Handler();
    }

    public static LocalImageFetcher getInstance() {
        if (sInstance == null) {
            sInstance = new LocalImageFetcher();
        } else {
            if (sInstance.mDirPaths != null)
                sInstance.mDirPaths.clear();
            if (sInstance.mImageFloders != null)
                sInstance.mImageFloders.clear();
            if (sInstance.mAllImgPathes != null)
                sInstance.mAllImgPathes.clear();
        }
        return sInstance;
    }

    public void getAllImage(final Context context, final OnScanFinishListener listener) {
        mThread = new Thread() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    File file = new File(path);
//                    Calendar cd = Calendar.getInstance();
//                    cd.setTimeInMillis(file.lastModified());
//                    System.out.println(cd.getTime());
                    // 获取该图片的父路径名
                    File parentFile = file.getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    if (parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }) == null) {
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                }
                Collections.reverse(mImageFloders);
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了

                for (int i = 0; i < mImageFloders.size(); i++) {
                    String dir = mImageFloders.get(i).getDir();
                    File parrentFile = new File(dir);
                    String[] arr = parrentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });
                    List<LocalImageBean> tmpList = new ArrayList<LocalImageBean>();
                    for (int j = 0; j < arr.length; j++) {
                        tmpList.add(new LocalImageBean(mImageFloders.get(i).getDir() + "/" + arr[j]));
                    }
                    mAllImgPathes.addAll(tmpList);
                }
                // 通知Handler扫描图片完成
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onScanFinish(mAllImgPathes);
                    }
                });
            }
        };
        mThread.start();
    }

    public void onDestroy() {
        if (mThread != null) {
            try {
                mThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnScanFinishListener {
        void onScanFinish(List<LocalImageBean> allImgPathes);
    }
}
