package com.zzkx.mtool.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.BaseAdapter;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseListBean;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.util.CheckLoadMoreUtil;
import com.zzkx.mtool.view.customview.LoadMoreListView;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/6/30.
 */

public abstract class BaseListFragment<T> extends BaseFragment
        implements LoadMoreListView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener, IListView {
    public LoadMoreListView mListView;
    public SwipeRefreshLayout mSwipLayout;
    public CheckLoadMoreUtil mCheckLoadMoreUtil;
    public BaseAdapter mAdapter;
    public List<T> mTotalData = new ArrayList<>();
    public boolean mIsRefresh = true;
    private BaseListPresenter mPresenter;

    /**
     * 获取适配器
     *
     * @return
     */
    public abstract BaseAdapter getAdapter();

    public abstract BaseListPresenter getPresenter();


    @Override
    public int getContentRes() {
        return R.layout.layout_load_more_list;
    }

    public List<T> getTotalData() {
        return mTotalData;
    }

    @Override
    public void initViews() {
        mListView = (LoadMoreListView) mBaseView.findViewById(R.id.lv_list);
        mListView.setOnLoadMoreListener(this);
        mSwipLayout = (SwipeRefreshLayout) mBaseView.findViewById(R.id.sr_layout);
        mSwipLayout.setOnRefreshListener(this);
        mSwipLayout.setColorSchemeResources(R.color.colorPrimary);
        mCheckLoadMoreUtil = new CheckLoadMoreUtil(mListView) {
            @Override
            public void updateAdapter() {
                if (mAdapter == null) {
                    mAdapter = getAdapter();
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onEmpty() {
                showEmpty();
            }

        };
        mSwipLayout.setRefreshing(true);
        mPresenter = getPresenter();
    }

    public void showList(BaseListBean bean) {
        mCheckLoadMoreUtil.check(bean, mTotalData, mIsRefresh);
    }



    @Override
    public void onRefresh() {
        mIsRefresh = true;
        mStateView.setCurrentState(StateView.ResultState.SUCESS);
        mPresenter.refreshData();

    }

    @Override
    public void onLoadMore() {
        mIsRefresh = false;
        mPresenter.loadMoreData();
    }

    @Override
    public void onReload() {
        mIsRefresh = true;
        mPresenter.refreshData();
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
        mListView.setFooterGone();
    }

    @Override
    public void showRefreshComplete() {
        mStateView.setCurrentState(StateView.ResultState.SUCESS);
        mSwipLayout.setRefreshing(false);
    }

    @Override
    public void showReload() {
        mListView.showReload();
    }
}
