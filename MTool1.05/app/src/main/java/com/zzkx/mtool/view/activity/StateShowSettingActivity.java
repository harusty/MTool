package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.view.customview.SimpleDialog;

/**
 * Created by sshss on 2017/11/1.
 */

public class StateShowSettingActivity extends BaseActivity implements View.OnClickListener {
    private SimpleDialog mDialogTagSelec;

    @Override
    public int getContentRes() {
        return R.layout.activity_state_shown_setting;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("谁可以看");
        findViewById(R.id.layout_select_show).setOnClickListener(this);
        findViewById(R.id.layout_select_dont_show).setOnClickListener(this);
        mDialogTagSelec = new SimpleDialog(this, R.layout.dialog_select_friend_tag);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select_dont_show:
                startActivity(new Intent(this, FriendListActivity.class));
                break;
            case R.id.layout_select_show:
                mDialogTagSelec.show();
                break;
        }
    }
}
