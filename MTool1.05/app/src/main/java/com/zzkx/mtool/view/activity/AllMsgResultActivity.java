package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MsgSearchPresenter;
import com.zzkx.mtool.view.adapter.MessageSearchAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IMessageSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/11/16.
 */

public class AllMsgResultActivity extends SearchAllActivity implements IMessageSearchView {
    private int mType;
    private MsgSearchPresenter mMsgSearchPresenter;
    private String mkey;
    private List<Object> mTotal = new ArrayList<>();
    private MessageSearchAdapter mAdapter;

    @Override
    public void initViews() {
        super.initViews();
        mType = getIntent().getIntExtra(Const.TYPE, MsgSearchPresenter.TYPE_RECENT);
        switch (mType) {
            case MsgSearchPresenter.TYPE_RECENT:
                setMainTitle("最近使用过的");
                break;
            case MsgSearchPresenter.TYPE_GROUP:
                setMainTitle("群  聊");
                break;
            case MsgSearchPresenter.TYPE_SINGLE:
                setMainTitle("聊天记录");
                break;
        }
        mkey = getIntent().getStringExtra(Const.KEY_WORD);
        mMsgSearchPresenter = new MsgSearchPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = mAdapter.getItem(position);
                mMsgSearchPresenter.onItemClick(item);
            }
        });
    }

    @Override
    public void search(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            mkey = keyword;
            mMsgSearchPresenter.searchSingle(mkey, EMClient.getInstance().chatManager().getAllConversations().size(), 1, mType);
        }
    }

    @Override
    public void initNet() {
        mInput.setText(mkey);
        mMsgSearchPresenter.searchSingle(mkey, EMClient.getInstance().chatManager().getAllConversations().size(), 1, mType);
    }


    @Override
    public void onReload() {

    }

    @Override
    public void showSearchResult(List<Object> searchResult) {
        mTotal.clear();
        mTotal.addAll(searchResult);
        if (mAdapter == null) {
            mAdapter = new MessageSearchAdapter(mTotal);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showEmpty() {

        mStateView.setCurrentState(StateView.ResultState.EMPTY);
        if (mAdapter != null) {
            mTotal.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}
