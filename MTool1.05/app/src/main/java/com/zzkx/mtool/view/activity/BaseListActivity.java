package com.zzkx.mtool.view.activity;

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
 * Created by sshss on 2017/6/28.
 * <p>
 * 关于列表数据的加载更多，下拉刷新，下拉刷新异常，加载更多异常的处理都封装在此。
 * 只暴露获取适配器的方法getAdapter()和getPresenter()方法
 * 关于其他非列表接口的访问处理在，下级activity或fragment处理。
 * <p>
 * 第一次加载，如果失败显示 stateView的error，加载更多是出现异常，在footer显示error,并提示点击重新请求。
 */

public abstract class BaseListActivity<T> extends BaseActivity
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
        mListView = (LoadMoreListView) findViewById(R.id.lv_list);
        mListView.setOnLoadMoreListener(this);
        mSwipLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        mSwipLayout.setOnRefreshListener(this);

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
    public void showProgress(boolean toShow) {
        if (!toShow)
            showRefreshComplete();
        mStateView.setCurrentState(toShow ? StateView.ResultState.LOADING : StateView.ResultState.SUCESS);
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void showRefreshComplete() {
        mSwipLayout.setRefreshing(false);
    }

    @Override
    public void showReload() {
        mListView.showReload();
    }
}
