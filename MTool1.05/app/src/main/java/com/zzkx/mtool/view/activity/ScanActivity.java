package com.zzkx.mtool.view.activity;

import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.util.CheckPermissionUtils;
import com.zzkx.mtool.util.ToastUtils;

/**
 * Created by sshss on 2017/10/14.
 */

public class ScanActivity extends BaseActivity {
    private CaptureFragment mCaptureFragment;

    @Override
    public int getContentRes() {
        return R.layout.activity_scan;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

        mCaptureFragment = new CaptureFragment();
        mCaptureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                ToastUtils.showToast("result: " + result);
                finish();
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.showToast("解析失败，请重试");
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCaptureFragment).commit();
    }

    @Override
    public void onReload() {

    }
}
