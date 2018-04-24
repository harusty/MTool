package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.domain.AtSupportMessage;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.adapter.AtListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/1.
 */

public class StateNotifyFragment extends BaseFragment {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private String mAction;
    private List<AtSupportMessage> allMsg;
    private AtListAdapter mAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initNet();
        }
    };
    private EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            mHandler.sendEmptyMessage(99);
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mAction = arguments.getString(Const.ACTION);
        }
        HeaderUtil.addHeader(getActivity(), mListView, 20);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position -= mListView.getHeaderViewsCount();
                if (position >= 0) {
                    AtSupportMessage myEMMessage = allMsg.get(position);
                    DemoHelper.getInstance().updateActionMessageRead(myEMMessage.id);
                    initNet();
                    startActivity(new Intent(getActivity(), StateDetailActivity.class).putExtra(Const.ID,myEMMessage.stateId));
                }
            }
        });

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }

    @Override
    public void initNet() {
        if (allMsg != null)
            allMsg.clear();
        else
            allMsg = new ArrayList<>();
        switch (mAction) {
            case Const.STATE_ACTION_AT:
                allMsg.addAll(DemoHelper.getInstance().getAtMessages());
                break;
            case Const.STATE_ACTION_SUPPORT:
                allMsg.addAll(DemoHelper.getInstance().getSupportMessages());
                break;
        }
        if (allMsg != null && allMsg.size() > 0) {
            System.out.println("change   " + allMsg.get(0).readFlag);
            if (mAdapter == null) {
                mAdapter = new AtListAdapter(allMsg);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onReload() {

    }
}
