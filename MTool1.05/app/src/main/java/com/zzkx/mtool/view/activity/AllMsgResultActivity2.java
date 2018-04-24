package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MsgSearchPresenter;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.adapter.MessageSearchAdapter;
import com.zzkx.mtool.view.customview.LoadMoreListView;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IMessageSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/16.
 */

public class AllMsgResultActivity2 extends BaseActivity implements IMessageSearchView, LoadMoreListView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    LoadMoreListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private int mType;
    private MsgSearchPresenter mMsgSearchPresenter;
    private String mkey;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private static final int PGE_COUNT = 15;
    private List<Object> mTotal = new ArrayList<>();
    private MessageSearchAdapter mAdapter;

    @Override
    public int getContentRes() {
        return R.layout.layout_load_more_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();

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
        mListView.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        HeaderUtil.addHeader(this, mListView, 20);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    position -= 1;
                    Object item = mAdapter.getItem(position);
                    mMsgSearchPresenter.onItemClick(item);
                }
            }
        });
    }

    @Override
    public void initNet() {
        isRefresh = true;
        pageNum = 1;
        mMsgSearchPresenter.searchSingle(mkey, PGE_COUNT, pageNum, mType);
    }

    @Override
    public void showProgress(boolean toShow) {
        if (pageNum == 1)
            mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showSearchResult(List<Object> searchResult) {
        if (isRefresh)
            mTotal.clear();

        if (searchResult.size() == 0) {
            mListView.setLoading(false);
        } else {
            mListView.setLoading(!mMsgSearchPresenter.isSearchAll());

            mTotal.addAll(searchResult);
            if (mAdapter == null) {
                mAdapter = new MessageSearchAdapter(mTotal);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        isRefresh = false;
        mMsgSearchPresenter.searchSingle(mkey, PGE_COUNT, pageNum, mType);
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
