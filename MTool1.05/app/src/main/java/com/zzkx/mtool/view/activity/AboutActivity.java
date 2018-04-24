package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2018/1/18.
 */

public class AboutActivity extends BaseActivity {
    @Override
    public int getContentRes() {
        return R.layout.activity_about;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("关于MTool");
    }

    @Override
    public void onReload() {

    }
}
