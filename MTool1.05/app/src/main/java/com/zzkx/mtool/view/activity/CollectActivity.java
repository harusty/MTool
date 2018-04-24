package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/9/30.
 */

public class CollectActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.activity_collect;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("我的收藏");
        findViewById(R.id.layout_shop_collect).setOnClickListener(this);
        findViewById(R.id.layout_good_collect).setOnClickListener(this);
        findViewById(R.id.layout_state_collect).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        switch (v.getId()) {
            case R.id.layout_shop_collect:
                intent.setClass(this, ShopCollectionListActivity.class);
                break;
            case R.id.layout_good_collect:
                intent.setClass(this, MenuCollectionListActivity.class);
                break;
            case R.id.layout_state_collect:
                intent.setClass(this, StateCollectionListActivity.class);
                break;
        }
        startActivityForResult(intent, 99);
    }
}