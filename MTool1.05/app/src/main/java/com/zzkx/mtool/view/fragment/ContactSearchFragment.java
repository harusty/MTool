package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.SearchContactBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ContactSearchPresenter;
import com.zzkx.mtool.view.activity.FriendSearchActivity;
import com.zzkx.mtool.view.adapter.ContactSearchAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IContactSearchView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/15.
 */

public class ContactSearchFragment extends SearchFragment implements IContactSearchView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private String mKey;
    private ContactSearchPresenter mPresenter;
    private ContactSearchAdapter mAdapter;

    @Override
    public void search(String key) {
        mKey = key;
        mPresenter.search(key);
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mPresenter = new ContactSearchPresenter(this);
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onReload() {
        search(mKey);
    }

    @Override
    public void showData(SearchContactBean bean) {
        if (mAdapter == null) {
            mAdapter = new ContactSearchAdapter(bean.cusData);
            mAdapter.setOnMoreListener(new ContactSearchAdapter.onMoreListener() {
                @Override
                public void onMoreClick(int itemViewType) {
                    startActivity(new Intent(getActivity(), FriendSearchActivity.class).putExtra(Const.KEY_WORD, mKey));
                }
            });
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPresenter.onItemClick(mAdapter.getItem(position),mAdapter.getItemViewType(position));
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }
}
