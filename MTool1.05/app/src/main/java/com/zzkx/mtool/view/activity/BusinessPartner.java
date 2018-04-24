package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.ToastUtils;

/**
 * Created by sshss on 2017/12/14.
 */

public class BusinessPartner extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.activity_business_partner;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("我要合作");
        findViewById(R.id.tv_shop).setOnClickListener(this);
        findViewById(R.id.tv_provider).setOnClickListener(this);
        findViewById(R.id.tv_ruzhu).setOnClickListener(this);
        findViewById(R.id.tv_daili).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shop:
                startActivity(new Intent(this, H5ShowActivity.class).putExtra(Const.URL, "http://59.110.1.160/index.html"));
                break;
            case R.id.tv_provider:
                startActivity(new Intent(this, H5ShowActivity.class).putExtra(Const.URL, "http://59.110.1.160/distributors.html"));
                break;
            case R.id.tv_ruzhu:
                ToastUtils.showToast("暂不支持");
                break;
            case R.id.tv_daili:
                ToastUtils.showToast("暂不支持");
                break;
        }
    }
}
