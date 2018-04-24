package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;

/**
 * Created by sshss on 2017/12/1.
 */

public class AttentionActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.activity_attention;
    }

    @Override
    public void initViews() {
        findViewById(R.id.layout_shop).setOnClickListener(this);
        setMainTitle("关  注");
        setMainMenuEnable();
        findViewById(R.id.layout_shop).setOnClickListener(this);
        findViewById(R.id.layout_user).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_shop:
                startActivity(new Intent(this, AttentionListActivity.class));
                break;
            case R.id.layout_user:
                startActivity(new Intent(this, AttentionListActivity.class).putExtra(Const.TYPE, AttentionListActivity.TYPE_USER));
                break;
        }
    }
}
