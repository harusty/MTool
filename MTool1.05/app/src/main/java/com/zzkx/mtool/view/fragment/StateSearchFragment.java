package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.StateSearchPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IStateSearcView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/24.
 */

public class StateSearchFragment extends SearchFragment implements IStateSearcView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private StateSearchPresenter mStateSearchPresenter;
    private StateListAdapter stateListAdapter;
    private boolean mIsSupported;

    @Override
    public void search(String key) {
        mStateSearchPresenter.search(key, mIsSupported);
        if (mIsSupported) {
            SearchHistoryCacheUtil.putKeyword(key, Const.SEARCH_TYPE_SUPPORTED);
        } else {
            SearchHistoryCacheUtil.putKeyword(key, Const.SEARCH_TYPE_STATE);
        }
        ((SearchActivity) getActivity()).refreshHistoryLayout(key);

    }

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mStateSearchPresenter = new StateSearchPresenter(this);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getActivity(), 10));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StateListBean.DataBean item = stateListAdapter.getItem(position);
                startActivity(new Intent(getContext(), StateDetailActivity.class).putExtra(Const.ID, item.id));
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsSupported = arguments.getBoolean(Const.FLAG);
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showError(ErrorBean errorBean) {
        if (getActivity() != null) {
            showProgress(false);
            ToastUtils.showToast(getString(R.string.netErroRetry));
        }
    }

    @Override
    public void showData(List<StateListBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
        stateListAdapter = new StateListAdapter(getActivity(), data, null);
        mListView.setAdapter(stateListAdapter);
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }
}
