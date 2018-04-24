package com.zzkx.mtool.util;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zzkx.mtool.MyApplication;

/**
 * Created by sshss on 2017/10/17.
 */

public class LocateUtil {
    private AMapLocationListener mLoactionListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private static LocateUtil sInstance;

    private LocateUtil(AMapLocationListener listener) {
        if (mlocationClient == null) {
            mLocationOption = new AMapLocationClientOption();
            mlocationClient = new AMapLocationClient(MyApplication.getContext());
            if (listener != null)
                mlocationClient.setLocationListener(listener);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationCacheEnable(false);
            mlocationClient.setLocationOption(mLocationOption);
        }
    }

    public void setListener(AMapLocationListener listener) {
        mLoactionListener = listener;
        mlocationClient.setLocationListener(listener);
    }

    public void stopLocation() {
        mLoactionListener = null;
        mlocationClient.stopLocation();
    }

    public void locate() {
        mlocationClient.startLocation();
    }

    public static LocateUtil getInstance() {
        return getInstance(null);
    }

    public static LocateUtil getInstance(AMapLocationListener listener) {
        if (sInstance == null) {
            sInstance = new LocateUtil(listener);
        } else if (listener != null) {
            sInstance.setListener(listener);
        }
        return sInstance;
    }
}
