package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MsgSearchPresenter;
import com.zzkx.mtool.view.activity.AllMsgResultActivity;
import com.zzkx.mtool.view.adapter.MessageSearchAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IMessageSearchView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/15.
 */

public class MessageSearchFragment extends SearchFragment implements IMessageSearchView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private MsgSearchPresenter mMessageSearchPresenter;
    private MessageSearchAdapter mAdapter;
    private String mKey;

    @Override
    public void search(String key) {
        mKey = key;
        mMessageSearchPresenter.searchMain(key, 3);
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mMessageSearchPresenter = new MsgSearchPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = mAdapter.getItem(position);
                mMessageSearchPresenter.onItemClick(item);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessageSearchPresenter.onDestroy();
    }

    @Override
    public void onReload() {

    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void showSearchResult(List<Object> searchResult) {
        if (mAdapter == null) {
            mAdapter = new MessageSearchAdapter(searchResult);
            mAdapter.setOnMoreListener(new MessageSearchAdapter.onMoreListener() {
                @Override
                public void onMoreClick(int itemViewType) {
                    startActivity(new Intent(getActivity(), AllMsgResultActivity.class)
                            .putExtra(Const.TYPE, itemViewType)
                            .putExtra(Const.KEY_WORD, mKey)
                    );
                }
            });
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }
}
