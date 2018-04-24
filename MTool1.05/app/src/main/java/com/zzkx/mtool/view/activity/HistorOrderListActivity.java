package com.zzkx.mtool.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.HistoryOrderBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.HistoryOrderPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.adapter.HistoryOrderAdapter;
import com.zzkx.mtool.view.iview.IHistoryOrderListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/15.
 */

public class HistorOrderListActivity extends BaseActivity implements IHistoryOrderListView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private HistoryOrderPresenter mPresenter;
    private HistoryOrderAdapter mAdapter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle(getString(R.string.history_order));
        mPresenter = new HistoryOrderPresenter(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        HeaderUtil.addHeader(this, mListView, 20);
    }

    @Override
    public void initNet() {
        mPresenter.getHistory();
    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void onReload() {
        onRefresh();
    }

    @Override
    public void showData(List<HistoryOrderBean.DataBean> data) {
        if (mAdapter == null) {
            mAdapter = new HistoryOrderAdapter(this, data);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position -= mListView.getHeaderViewsCount();
                    if (position < 0)
                        return;
                    HistoryOrderBean.DataBean dataBean = mAdapter.getItem(position);
                    HistoryOrderBean.DiningType orderDining = dataBean.orderDining;
                    switch (orderDining.diningType) {
                        case CartCacheUtil.TYPE_OUT:
                            startActivity(new Intent(getContext(), OrderDetailActivity.class)
                                    .putExtra(Const.TYPE, CartCacheUtil.TYPE_OUT)
                                    .putExtra(Const.ID, dataBean.id));
                            break;
                        case CartCacheUtil.TYPE_IN:
                            startActivity(new Intent(getContext(), OrderDetailActivity.class)
                                    .putExtra(Const.TYPE, CartCacheUtil.TYPE_IN)
                                    .putExtra(Const.ID, dataBean.id)
                            );
                            break;

                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
