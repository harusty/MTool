package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.EaseUserListBean;
import com.zzkx.mtool.chat.ui.ContactListFragment;
import com.zzkx.mtool.config.Const;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/9/19.
 */

public class FriendListActivity extends BaseActivity {
    private ContactListFragment mContactListFragment;
    public static final int ACTION_MINGPIAN = 0;
    public static final int ACTION_SINGLE_SELECT = 1;
    public static final int ACTION_MULTI_SELECT = 2;
    private int mAction;

    @Override
    public int getContentRes() {
        return R.layout.activity_friend_list;
    }

    @Override
    public void initViews() {
        setMainTitle("好    友");
        setMainMenuEnable();
        mAction = getIntent().getIntExtra(Const.ACTION, ACTION_MINGPIAN);

        mContactListFragment = new ContactListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fr_container, mContactListFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.SELECT_MODE, true);
        mContactListFragment.setArguments(bundle);
        mContactListFragment.setOnCustomItemClickListener(new ContactListFragment.CustomItemClickListener() {
            @Override
            public void onClick(EaseUser user) {
                switch (mAction) {
                    case ACTION_MINGPIAN:
                        handleMingpian(user);
                        break;
                    case ACTION_SINGLE_SELECT:
                        break;
                    case ACTION_MULTI_SELECT:
                        handleMultiSelect(user);
                        break;
                }
            }
        });
//        setSecMenu(new int[]{R.mipmap.ic_add_friend, R.mipmap.ic_zoom_menu}
//                , new String[]{ "搜        索"}
//                , new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = (int) v.getTag();
//                        switch (position) {
//                            case 0:
////                                startActivity(new Intent(FriendListActivity.this, AddContactActivity.class));
//                                startActivity(new Intent(FriendListActivity.this,MessageSearchActivity.class));
//                                break;
//                            case 1:
//
//                                break;
//                        }
//                    }
//                });
    }

    private ArrayList<EaseUser> mSelecteList;

    private void handleMultiSelect(EaseUser user) {
        if (mSelecteList == null)
            mSelecteList = new ArrayList<>();
        user.cusSelected = !user.cusSelected;
        if (user.cusSelected) {
            mSelecteList.add(user);
        } else {
            mSelecteList.remove(user);
        }

        if (mSelecteList.size() > 0) {
            setTitleCheckVisible(View.VISIBLE);
        } else {
            setTitleCheckVisible(View.GONE);
        }
        mContactListFragment.notifyAdapter();

    }

    @Override
    public void onFinishAct() {
        for (EaseUser user : mSelecteList) {
            user.cusSelected = false;
        }
        Intent intent = new Intent();
        EaseUserListBean easeUserListBean = new EaseUserListBean();
        easeUserListBean.data = mSelecteList;
        intent.putExtra("data", Json_U.toJson(easeUserListBean));
        setResult(Activity.RESULT_OK, intent);
        super.onFinishAct();
    }

    public void handleMingpian(EaseUser user) {
        Intent intent = new Intent();
        ChatShareBean chatShareBean = new ChatShareBean();
        chatShareBean.type = 0;
        chatShareBean.id = user.getUsername();
        chatShareBean.picUrl = user.getAvatar();
        chatShareBean.title = user.getNickname();
        intent.putExtra(Const.SHARE_INFO, Json_U.toJson(chatShareBean));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onReload() {

    }
}
