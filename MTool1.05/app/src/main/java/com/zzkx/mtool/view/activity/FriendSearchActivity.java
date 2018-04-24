package com.zzkx.mtool.view.activity;

import android.content.Context;
import android.text.TextUtils;

import com.zzkx.mtool.bean.SearchContactBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ContactSearchPresenter;
import com.zzkx.mtool.view.adapter.ContactSearchAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IContactSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/11/20.
 */

public class FriendSearchActivity extends SearchAllActivity implements IContactSearchView {

    private ContactSearchPresenter mPresenter;
    private List<Object> mTotal = new ArrayList<>();
    private ContactSearchAdapter mContactSearchAdapter;
    private String mKeyWord;

    @Override
    public void initViews() {
        super.initViews();
        setMainTitle("好  友");
        mPresenter = new ContactSearchPresenter(this);
        mPresenter.setSearchType(Const.FRIEND);
        mKeyWord = getIntent().getStringExtra(Const.KEY_WORD);
        mInput.setText(mKeyWord);
    }

    @Override
    public void initNet() {
        mPresenter.search(mKeyWord);
    }

    @Override
    public void search(String keyword) {

        if (!TextUtils.isEmpty(keyword)) {
            mPresenter.search(keyword);
            mKeyWord = keyword;
        }
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showData(SearchContactBean bean) {
        mTotal.clear();
        mTotal.addAll(bean.cusData);
        if (mContactSearchAdapter == null) {
            mContactSearchAdapter = new ContactSearchAdapter(mTotal);
            mListView.setAdapter(mContactSearchAdapter);
        } else {
            mContactSearchAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
