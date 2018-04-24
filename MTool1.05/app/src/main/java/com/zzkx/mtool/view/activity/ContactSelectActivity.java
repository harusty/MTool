package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.ui.GroupsActivity;
import com.zzkx.mtool.config.Const;

/**
 * Created by sshss on 2017/12/4.
 */

public class ContactSelectActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getContentRes() {
        return R.layout.activity_contact_select;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("添加新成员");
        findViewById(R.id.layout_friend).setOnClickListener(this);
        findViewById(R.id.layout_attention).setOnClickListener(this);
        findViewById(R.id.layout_group).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_friend:
                startActivityForResult(new Intent(ContactSelectActivity.this, FriendListTagSelectActivity.class)
                        .putExtra(Const.ID, getIntent().getStringExtra(Const.ID)), 99);//标签id

                break;
            case R.id.layout_attention:

                startActivityForResult(new Intent(ContactSelectActivity.this, AttentionListActivity.class)
                                .putExtra(Const.ID, getIntent().getStringExtra(Const.ID))
                                .putExtra(Const.TYPE, 1)
                                .putExtra(Const.ACTION, AttentionListActivity.ACTION_SELECT)
                        , 99);//标签id
                break;
            case R.id.layout_group:
                startActivityForResult(new Intent(ContactSelectActivity.this, GroupsActivity.class)
                                .putExtra(Const.ID, getIntent().getStringExtra(Const.ID))//标签id
                                .putExtra(Const.ACTION, GroupsActivity.ACTION_SELECT)
                        , 99);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }
}
