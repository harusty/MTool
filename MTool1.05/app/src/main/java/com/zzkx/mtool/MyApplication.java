package com.zzkx.mtool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.VcPlayerLog;
import com.amap.api.services.core.LatLonPoint;
import com.mob.MobSDK;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.DemoModel;
import com.zzkx.mtool.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/6/26.
 */

public class MyApplication extends Application {
    public static Context applicationContext;
    private static MyApplication instance;

    public static String currentUserNick = "";
    public static LatLonPoint nearbyShopLatLonPoint;
    private List<Activity> mActivities = new ArrayList<>();
    private CurrentCityInfo mCurrentCityInfo;

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        MobSDK.init(this);
// ============== fabric start
//		Fabric.with(this, new Crashlytics());
// ============== fabric end
        applicationContext = this;
        instance = this;
        DemoHelper.getInstance().init(applicationContext);
        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        initAliPlayer();
    }


    private void initAliPlayer() {
        VcPlayerLog.enableLog();
        //初始化播放器
        final String businessId = "";
        AliVcMediaPlayer.init(getApplicationContext(), businessId);

        //设置保存密码。此密码如果更换，则之前保存的视频无法播放
//        AliyunDownloadConfig config = new AliyunDownloadConfig();
//        config.setSecretImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aliyun/encryptedApp.dat");
//        config.setDownloadPassword("123456789");
        //设置保存路径。请确保有SD卡访问权限。
//        config.setDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test_save/");
        //设置同时下载个数
//        config.setMaxNums(2);
//        AliyunDownloadManager.getInstance(this).setDownloadConfig(config);

    }

    @Override
    public void onTerminate() {
        DemoModel demoModel = new DemoModel(this);
        demoModel.setContactSynced(false);
        super.onTerminate();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(BaseActivity baseActivity) {
        mActivities.add(baseActivity);
    }

    public void removeAvctivity(BaseActivity actvity) {
        mActivities.remove(actvity);
    }

    public void clearActivity() {
        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);
            if (activity != null && !activity.getClass().getName().contains("MainActivity")) {
                activity.finish();
            }
        }
    }

    public CurrentCityInfo getCurrentCity() {
        return mCurrentCityInfo;
    }
    public void setCurrentCityInfo(CurrentCityInfo info){
        mCurrentCityInfo = info;
    }
}
