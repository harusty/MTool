/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzkx.mtool.chat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.GroupChatExtBean;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.adapter.GroupAdapter;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.activity.FriendListTagSelectActivity;
import com.zzkx.mtool.view.activity.GroupMemberManageActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends BaseActivity {
    public static final String TAG = "GroupsActivity";
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupsActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final int ACTION_SELECT = 1;
    public static final int ACTION_SELECT2 = 2;


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private View mHeaderView;
    private int mAction;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public int getContentRes() {
        return R.layout.em_fragment_groups;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("群  聊");
        mAction = getIntent().getIntExtra(Const.ACTION, 0);
        instance = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        groupListView = (ListView) findViewById(R.id.list);
        initGroupList();
        if (mAction == ACTION_SELECT || mAction == ACTION_SELECT2) {
            HeaderUtil.addHeader(this, groupListView, 20);
        } else {
            mHeaderView = View.inflate(this, R.layout.header_group_list, null);
            mHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
                    startActivityForResult(new Intent(GroupsActivity.this, FriendListTagSelectActivity.class)
                            .putExtra(Const.ACTION, FriendListTagSelectActivity.ACTION_CREATE_GROUP), FriendListTagSelectActivity.ACTION_CREATE_GROUP);
                }
            });
            groupListView.addHeaderView(mHeaderView);
        }
        //show group list
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //pull down to refresh
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mAction == ACTION_SELECT) {
                    startActivityForResult(new Intent(GroupsActivity.this, GroupMemberManageActivity.class)
                            .putExtra("action", "add_2_tag")
                            .putExtra("userId", groupAdapter.getItem(position - 1).getGroupId())
                            .putExtra(Const.ID, getIntent().getStringExtra(Const.ID)), 99//标签id
                    );
                } else if (mAction == ACTION_SELECT2) {
                    Intent intent = new Intent();
                    intent.putExtra(Const.ID, groupAdapter.getItem(position - 1).getGroupId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    // enter group chat
                    Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
                    // it is group chat
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstantSub.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 1).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }
        });
        groupListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        registerGroupChangeReceiver();
    }


    private void initGroupList() {
        List<EMGroup> filterList = EMClient.getInstance().groupManager().getAllGroups();
        if (grouplist == null)
            grouplist = new ArrayList<>();
        else
            grouplist.clear();
        for (EMGroup group : filterList) {
            String description = group.getDescription();
            if (!TextUtils.isEmpty(description)) {
                System.out.println("group activity:" + group.getDescription());
                try {
                    GroupChatExtBean extBean = Json_U.fromJson(description, GroupChatExtBean.class);
                    if (extBean.groupType == EaseConstant.ORDER_CHAT) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

            }
            grouplist.add(group);
        }
    }

    void registerGroupChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EaseConstantSub.ACTION_GROUP_CHANAGED);
         broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(EaseConstantSub.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(GroupsActivity.this).equals(GroupsActivity.class.getName())) {
                        refresh();
                    }
                }
            }
        };
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            if (requestCode == FriendListTagSelectActivity.ACTION_CREATE_GROUP) {
                initGroupList();
                groupAdapter.notifyDataSetChanged();
            } else {
                setResult(Const.RESULT_SUCESS_CODE);
                finish();
            }
        }
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        initGroupList();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onReload() {

    }
}
