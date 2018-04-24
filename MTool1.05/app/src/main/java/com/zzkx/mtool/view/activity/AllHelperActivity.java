package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/9/22.
 */

public class AllHelperActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.activity_all_helper;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("所有助手");
        findViewById(R.id.layout_order_helper).setOnClickListener(this);
        findViewById(R.id.layout_state_helper).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_order_helper:
                startActivity(new Intent(this, OrderHelperActivity.class));
                break;
            case R.id.layout_state_helper:
                startActivity(new Intent(this, StateHelperActivity.class));
                break;

        }
    }
}
