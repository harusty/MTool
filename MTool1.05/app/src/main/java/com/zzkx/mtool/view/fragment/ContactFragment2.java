package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.ui.BlacklistActivity;
import com.zzkx.mtool.chat.ui.GroupsActivity;
import com.zzkx.mtool.view.activity.AllHelperActivity;
import com.zzkx.mtool.view.activity.AttentionActivity;
import com.zzkx.mtool.view.activity.FriendListActivity;
import com.zzkx.mtool.view.activity.TagsActivity;

/**
 * Created by sshss on 2017/9/19.
 */

public class ContactFragment2 extends BaseFragment implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.fragment_contact2;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mBaseView.findViewById(R.id.layout_friend).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_group).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_black_list).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_all_helper).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_attention).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_tags).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_friend:
                startActivity(new Intent(getActivity(), FriendListActivity.class));
                break;
            case R.id.layout_group:
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            case R.id.layout_black_list:
                startActivity(new Intent(getActivity(), BlacklistActivity.class));
                break;
            case R.id.layout_all_helper:
                startActivity(new Intent(getActivity(), AllHelperActivity.class));
                break;
            case R.id.layout_attention:
                startActivity(new Intent(getActivity(), AttentionActivity.class));
                break;
            case R.id.layout_tags:
                startActivity(new Intent(getActivity(), TagsActivity.class));
                break;
        }
    }
}
